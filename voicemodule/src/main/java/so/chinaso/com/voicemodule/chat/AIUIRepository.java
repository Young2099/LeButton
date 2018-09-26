package so.chinaso.com.voicemodule.chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import so.chinaso.com.voicemodule.db.MessageDao;
import so.chinaso.com.voicemodule.entity.DynamicEntityData;
import so.chinaso.com.voicemodule.entity.RawMessage;
import so.chinaso.com.voicemodule.entity.VoiceEntity;
import so.chinaso.com.voicemodule.intent.player.AIUIPlayer;

/**
 * Created by yf on 2018/8/9.
 */
public class AIUIRepository extends ViewModel {
    private static final String TAG = AIUIRepository.class.getSimpleName();


    //AIUI当前状态
    private int mCurrentState = AIUIConstant.STATE_IDLE;
    //当前AIUI使用的配置
    private JSONObject mLastConfig;
    private String mMscCfg;
    private AIUIAgent mAIUIAgent = null;
    private Context mContext;
    //是否检测到前端点，提示 ’为说话‘ 时判断使用
    private JSONObject mPersParams;

    //处理PGS听写(流式听写）的队列
    private String voiceWords;

    //vad事件
    private MutableLiveData<AIUIEvent> mVADEvent = new MutableLiveData<>();
    //唤醒和休眠事件
    private MutableLiveData<AIUIEvent> mStateEvent = new SingleLiveEvent<>();
    //上传联系人
    private ContactRepository contactRepository;
    private MessageDao mMessageDao;
    private AIUIPlayer mPlayer;


    public AIUIRepository(Context context, MessageDao dao, AIUIPlayer player) {
        mContext = context;
        mMessageDao = dao;
        mPlayer = player;
        initAIUIAgent();
    }




    public void startVoice() {
        startRecordAudio();
    }

    private void startRecordAudio() {
        sendMessage(new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, "data_type=audio,sample_rate=16000", null));
    }


    private void sendMessage(AIUIMessage message) {
        if (mAIUIAgent != null) {
            //确保AIUI处于唤醒状态
            if (mCurrentState != AIUIConstant.STATE_WORKING) {
                mAIUIAgent.sendMessage(new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null));
            }
            mAIUIAgent.sendMessage(message);
        }
    }


    /**
     * 读取配置
     */
    private String getAIUIParams() {
        String params = "";

        AssetManager assetManager = mContext.getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return params;
    }

    private void initContract() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<String> contacts = contactRepository.getContacts();

                StringBuilder contactJson = new StringBuilder();
                for (String contact : contacts) {
                    String[] nameNumber = contact.split("\\$\\$");
                    contactJson.append(String.format("{\"name\": \"%s\", \"phoneNumber\": \"%s\" }\n",
                            nameNumber[0], nameNumber[1]));
                }
//                syncDynamicData(new DynamicEntityData(
//                        "IFLYTEK.telephone_contact", "uid", "", contactJson.toString()));
//                putPersParam("uid", "");
            }
        }.start();
    }

    public void getContracts() {
        initContract();
    }

    public void stopTTS() {
        stopCloudTTS();
    }

    //AIUI事件监听器
    private AIUIListener mAIUIListener = new AIUIListener() {

        @Override
        public void onEvent(AIUIEvent event) {
            switch (event.eventType) {
                case AIUIConstant.EVENT_WAKEUP: {
                    mStateEvent.postValue(event);

                }
                break;

                case AIUIConstant.EVENT_SLEEP: {
                    mStateEvent.postValue(event);

                }
                break;

                case AIUIConstant.EVENT_STATE: {
                    mCurrentState = event.arg1;
                }
                break;

                case AIUIConstant.EVENT_RESULT: {
                    processResult(event);
                }
                break;

                case AIUIConstant.EVENT_TTS: {
//                    processTTS(event);
                }
                break;

                case AIUIConstant.EVENT_ERROR: {
                    //向消息列表中添加AIUI错误消息
                    Map<String, String> semantic = new HashMap<>();
                    semantic.put("errorInfo", event.info);

                }
                break;
                case AIUIConstant.EVENT_CMD_RETURN: {
//                    processCmdReturnEvent(aiuiEvent);
                }

                case AIUIConstant.EVENT_CONNECTED_TO_SERVER: {
//                    mUID.postValue(aiuiEvent.data.getString("uid"));
                }
                break;

                case AIUIConstant.EVENT_VAD: {
                    mVADEvent.postValue(event);

                }

                break;
            }
        }

    };


    /**
     * 处理AIUI结果事件（听写结果和语义结果）
     *
     * @param event 结果事件
     */
    private void processResult(AIUIEvent event) {
        try {
            JSONObject bizParamJson = new JSONObject(event.info);
            JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
            JSONObject params = data.getJSONObject("params");
            JSONObject content = data.getJSONArray("content").getJSONObject(0);

            long rspTime = event.data.getLong("eos_rslt", -1);  //响应时间
            if (content.has("cnt_id")) {
                String cnt_id = content.getString("cnt_id");
                JSONObject cntJson = new JSONObject(new String(event.data.getByteArray(cnt_id), "utf-8"));

                String sub = params.optString("sub");
                if ("nlp".equals(sub)) {
                    JSONObject semanticResult = cntJson.optJSONObject("intent");
                    if (semanticResult != null && semanticResult.length() != 0) {
                        //解析得到语义结果，将语义结果作为消息插入到消息列表中
                        Log.e(TAG, "processResult: " + semanticResult.toString());
                        getJsonString(semanticResult.toString());
                    }
                } else if ("iat".equals(sub)) {
                    //解析听写结果更新当前语音消息的听写内容
                    updateVoiceMessageFromIAT(cntJson);
                } else if ("itrans".equals(sub)) {
                    String sid = event.data.getString("sid", "");
                    updateMessageFromItrans(sid, params, cntJson, rspTime);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


    private void updateVoiceMessageFromIAT(JSONObject cntJson) {
        Log.e(TAG, "updateVoiceMessageFromIAT: " + cntJson);
        JSONObject text = cntJson.optJSONObject("text");
        // 解析拼接此次听写结果
        StringBuilder iatText = new StringBuilder();
        JSONArray words = text.optJSONArray("ws");
        boolean lastResult = text.optBoolean("ls");
        for (int index = 0; index < words.length(); index++) {
            JSONArray charWord = words.optJSONObject(index).optJSONArray("cw");
            for (int cIndex = 0; cIndex < charWord.length(); cIndex++) {
                iatText.append(charWord.optJSONObject(cIndex).opt("w"));
            }
        }
        if (!TextUtils.isEmpty(iatText)) {
            voiceWords = iatText.toString();
        }

    }

    private void updateMessageFromItrans(String sid, JSONObject params, JSONObject cntJson, long rspTime) {
        String text = "";
        try {
            cntJson.put("sid", sid);

            JSONObject transResult = cntJson.optJSONObject("trans_result");
            if (transResult != null && transResult.length() != 0) {
                text = transResult.optString("dst");
            }

            if (TextUtils.isEmpty(text)) {
                return;
            }

            int rstId = params.optInt("rstid");
            if (rstId == 1) {
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(cntJson);
//                anlaysize(jsonArray.toString().getBytes());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void stopAudio() {
        sendMessage(new AIUIMessage(AIUIConstant.CMD_STOP_RECORD, 0, 0, "data_type=audio,sample_rate=16000", null));

    }

    public void stopCloudTTS() {
        sendMessage(new AIUIMessage(AIUIConstant.CMD_TTS, AIUIConstant.CANCEL, 0, "", null));
    }


    private void stopRecordAudio() {
        if (null != mAIUIAgent) {
            sendMessage(new AIUIMessage(AIUIConstant.CMD_STOP, 0, 0, null, null));
            mAIUIAgent.destroy();
        }
    }

    public void initAIUIAgent() {
        if (null == mAIUIAgent) {
            Log.i(TAG, "create aiui agent");
            //创建AIUIAgent
            mAIUIAgent = AIUIAgent.createAgent(mContext,getAIUIParams(), mAIUIListener);
        }

        if (null == mAIUIAgent) {
            final String strErrorTip = "语音服务出错！";
        }

    }

    private void fakeAIUIResult(int i, String s, String text, Object o, Map<String, String> data) {

    }

    /**
     * 手动设置位置信息
     *
     * @param lng
     * @param lat
     */
    public void setLoc(double lng, double lat) {
        try {
            JSONObject audioParams = new JSONObject();
            audioParams.put("msc.lng", String.valueOf(lng));
            audioParams.put("msc.lat", String.valueOf(lat));

            JSONObject params = new JSONObject();
            params.put("audioparams", audioParams);

            //完成设置后，在随后的每次会话都会携带该位置信息
            setParams(params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setParams(String params) {
        sendMessage(new AIUIMessage(AIUIConstant.CMD_SET_PARAMS, 0, 0, params, null));
    }

    /**
     * processResult:
     * {"rc":0,
     * "semantic":[{"intent":"INSTRUCTION","slots":[{"name":"insType","value":"CONFIRM"}]}],
     * "service":"telephone","uuid":"cida1144f9a@dx00db0ecde832010005",
     * "text":"是",
     * "state":{"fg::telephone::default::default":{"insType":"1","operation":"1","state":"default"}},
     * "used_state":{"insType":"1","operation":"1","state":"default","state_key":"fg::telephone::default::default"},
     * "dialog_stat":"dataInvalid","save_history":true,"sid":"cida1144f9a@dx00db0ecde832010005"}
     * <p>
     * RC O -> 成功
     * RC 1-> 成功
     * 2 ->  无效请求
     * 3 ->  服务器内部出错
     * 4 ->  服务器不理解或不能处理该文本
     *
     * @param semanticResult
     */
    private void getJsonString(String semanticResult) {
        VoiceEntity voiceEntity = new VoiceEntity(semanticResult, voiceWords);
        addMessageToDB(voiceEntity.getRawMessage());
    }

    public void addMessageToDB(final RawMessage msg) {
        Completable
                .complete()
                .observeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "run: " + msg);
                        mMessageDao.addMessage(msg);
                    }
                });
    }

    public LiveData<List<RawMessage>> getInteractMsg() {
        return mMessageDao.getAllMessage();
    }


    public void sendText(String msg) {
//        if(mAppendVoiceMsg != null){
//            //更新上一条未完成的语音消息内容
//            updateMessage(mAppendVoiceMsg);
//            mAppendVoiceMsg = null;
//        }
        //pers_param用于启用动态实体和所见即可说功能
        String params = "data_type=text,pers_param={\"appid\":\"\",\"uid\":\"\"}";
        sendMessage(new AIUIMessage(AIUIConstant.CMD_WRITE, 0, 0,
                params, msg.getBytes()));
        voiceWords = msg;
    }

    public LiveData<AIUIEvent> getVADEvent() {
        return mVADEvent;
    }

    public LiveData<AIUIEvent> getStateEvent() {
        return mStateEvent;
    }

    private void syncDynamicData(DynamicEntityData data) {
        Log.e(TAG, "syncDynamicData: " + mAIUIAgent);
        try {
            // 构造动态实体数据
            JSONObject syncSchemaJson = new JSONObject();
            JSONObject paramJson = new JSONObject();

            paramJson.put("id_name", data.idName);
            paramJson.put("id_value", data.idValue);
            paramJson.put("res_name", data.resName);

            syncSchemaJson.put("param", paramJson);
            syncSchemaJson.put("data", Base64.encodeToString(
                    data.syncData.getBytes(), Base64.DEFAULT | Base64.NO_WRAP));

            // 传入的数据一定要为utf-8编码
            byte[] syncData = syncSchemaJson.toString().getBytes("utf-8");

            AIUIMessage syncAthenaMessage = new AIUIMessage(AIUIConstant.CMD_SYNC,
                    AIUIConstant.SYNC_DATA_SCHEMA, 0, "", syncData);
//            sendMessage(syncAthenaMessage);
            mAIUIAgent.sendMessage(syncAthenaMessage);
        } catch (Exception e) {
            e.printStackTrace();
//            addMessageToDB(new RawMessageCache(AIUI, TEXT,
//                    String.format("上传动态实体数据出错 %s", e.getMessage()).getBytes()));
            Log.e(TAG, "syncDynamicData: 上传动态实体数据出错" + e.getMessage().getBytes());
        }
    }

    //生效动态实体
    public void putPersParam(String key, String value) {
        try {
            mPersParams = new JSONObject();
            mPersParams.put(key, value);
            setPersParams(mPersParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置个性化(动态实体和所见即可说)生效参数
     *
     * @param persParams
     */
    public void setPersParams(JSONObject persParams) {
        try {
            //参考文档动态实体生效使用一节
            JSONObject params = new JSONObject();
            JSONObject audioParams = new JSONObject();
            audioParams.put("pers_param", persParams.toString());
            params.put("audioparams", audioParams);

            sendMessage(new AIUIMessage(AIUIConstant.CMD_SET_PARAMS, 0, 0, params.toString(), null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//
//    /**
//     * 处理AIUI云端tts事件
//     *
//     * @param event 结果事件
//     */
//    private void processTTS(AIUIEvent event) {
//        switch (event.arg1) {
//            case AIUIConstant.TTS_SPEAK_COMPLETED:
//                if (mCurrentSettings.tts) {
//                    mPlayer.playPreSongList();
//                }
//                break;
//
//            default:
//                break;
//        }
//
//    }

}
