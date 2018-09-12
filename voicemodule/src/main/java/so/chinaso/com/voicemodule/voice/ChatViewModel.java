package so.chinaso.com.voicemodule.voice;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.Nullable;

import com.iflytek.aiui.AIUIEvent;
import com.iflytek.location.result.GPSLocResult;
import com.iflytek.location.result.NetLocResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import so.chinaso.com.voicemodule.entity.DynamicEntityData;
import so.chinaso.com.voicemodule.entity.RawMessage;

/**
 * Created by yf on 2018/9/12.
 */
public class ChatViewModel extends ViewModel {
    private ContactRepository contactRepository;
    private AIUIRepository mRepository;
    private Context mContext;
    private LocationRepo mLocRepo;


    private boolean mStartLocate = false;
    private final Timer mLocateTimer = new Timer();

    public void init(Context context) {
        mContext = context;
        mRepository = new AIUIRepository(context);
        contactRepository = new ContactRepository(context);
        mLocRepo = new LocationRepo(context);
        mRepository.initAIUIAgent();
    }

    public LiveData<List<RawMessage>> getInteractMessages() {
        return mRepository.getInteractMsg();
    }

    //发送文本交互消息
    public void sendText(String msg) {
        mRepository.sendText(msg);
    }

    //AIUI开始录音
    public void startRecord() {
        mRepository.startVoice();
    }

    //AIUI停止录音
    public void stopRecord() {
        mRepository.stopAudio();
    }

    public void stopTTS() {
        mRepository.stopCloudTTS();
    }

//    //生效动态实体
//    public void putPersParam(String key, String value) {
//        try {
//            mPersParams.put(key, value);
//            mRepository.setPersParams(mPersParams);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    //模拟AIUI结果信息，用于展示如欢迎语或者操作结果信息
    public void fakeAIUIResult(RawMessage rawMessage) {
        mRepository.addMessageToDB(rawMessage);
    }

    //更新消息列表中特定的消息内容
//    public void updateMessage(RawMessage msg) {
//        mRepository.updateMessage(msg);
//    }

//    上传动态实体数据
//    public void syncDynamicData(DynamicEntityData data) {
//        mRepository.syncDynamicEntity(data);
//    }

    //查询动态实体上传状态
//    public void queryDynamicStatus(String sid) {
//        mRepository.queryDynamicSyncStatus(sid);
//    }

    //上传所见即可说数据
//    public void syncSpeakableData(SpeakableSyncData data) {
//        mRepository.syncSpeakableData(data);
//    }

    public List<String> getContacts() {
        return contactRepository.getContacts();
    }

//    public LiveData<Settings> getSettings() {
//        return mSettingsRepo.getSettings();
//    }

//    public LiveData<Boolean> getTTSEnableState() {
//        return mSettingsRepo.getTTSEnableState();
//    }

    public LiveData<AIUIEvent> getVADEvent() {
        return mRepository.getVADEvent();
    }

    public LiveData<AIUIEvent> getStateEvent() {
        return mRepository.getStateEvent();
    }

    public Context getContext() {
        return mContext;
    }

//    public String readAssetFile(String filename){
//        return mStorage.readAssetFile(filename);
//    }

    public void useLocationData() {
        mStartLocate = true;

        mLocRepo.getGPSLoc().observeForever(new Observer<GPSLocResult>() {
            @Override
            public void onChanged(@Nullable GPSLocResult gpsLoc) {
                if (mStartLocate) {
                    mStartLocate = false;
                    mLocRepo.stopLocate();
                    mRepository.setLoc(gpsLoc.getLon(), gpsLoc.getLat());

                    String location = String.format("GPS location lon %f lat %f", gpsLoc.getLon(), gpsLoc.getLat());
                    Map<String, String> data = new HashMap<>();
                    data.put("gpsLoc", location);
//                    mRepository.fakeAIUIResult(0, "fake.Loc", "已获取使用最新的GPS位置", null, data);
                }
            }
        });

        mLocateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mStartLocate) {
                    mLocRepo.stopLocate();
                    mLocRepo.getNetLoc().observeForever(new Observer<NetLocResult>() {
                        @Override
                        public void onChanged(@Nullable NetLocResult netLoc) {
                            if (mStartLocate) {
                                mStartLocate = false;
                                mLocRepo.stopLocate();
                                mRepository.setLoc(netLoc.getLon(), netLoc.getLat());

                                String location = String.format("net location city %s, lon %f lat %f", netLoc.getCity(), netLoc.getLon(), netLoc.getLat());
                                Map<String, String> data = new HashMap<>();
                                data.put("netLoc", location);
//                                mRepository.fakeAIUIResult(0, "fake.Loc", "已获取使用最新的网络位置信息", null, data);
                            }
                        }
                    });
                }
            }
        }, 5000);
    }

    //    public void useNewAppID(String appid, String key, String scene) {
//        mSettingsRepo.config(appid, key, scene);
//    }

    public List<String> getVoiceWord() {
        List<String> list = new ArrayList<>();
        list.add("今天天气怎么样");
        list.add("西瓜用英文怎么说");
        list.add("九九乘法表");
        list.add("朗读一首李白的诗");
        list.add("安静的静怎么写");
        list.add("魑魅魍魉是啥意思");
        list.add("给中国移动打电话");
        list.add("周杰伦是谁");
        list.add("我要查清华大学的分数线");
        list.add("给我来个段子");
        list.add("北京有哪些大学");
        list.add("历史上的今天发生了什么");
        list.add("我要学英语");
        list.add("难过的反义词");
        list.add("关于励志的经典语句");
        list.add("给我来个演说");
        list.add("来一句英语");
        return list;
    }
}
