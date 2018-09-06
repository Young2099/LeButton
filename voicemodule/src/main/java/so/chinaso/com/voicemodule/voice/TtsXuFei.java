//package so.chinaso.com.voicemodule.voice;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Environment;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.style.BackgroundColorSpan;
//import android.util.Log;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//
//import com.iflytek.cloud.ErrorCode;
//import com.iflytek.cloud.InitListener;
//import com.iflytek.cloud.SpeechConstant;
//import com.iflytek.cloud.SpeechError;
//import com.iflytek.cloud.SpeechEvent;
//import com.iflytek.cloud.SpeechSynthesizer;
//import com.iflytek.cloud.SynthesizerListener;
//
//import static android.content.Context.MODE_PRIVATE;
//
///**
// * Created by yf on 2018/8/8.
// */
//public class TtsXuFei {
//    private static String TAG = TtsXuFei.class.getSimpleName();
//    // 语音合成对象
//    private SpeechSynthesizer mTts;
//
//    // 默认发音人
//    private String voicer = "xiaoyan";
//
//    private String[] mCloudVoicersEntries;
//    private String[] mCloudVoicersValue;
//    String texts = "";
//
//    // 缓冲进度
//    private int mPercentForBuffering = 0;
//    // 播放进度
//    private int mPercentForPlaying = 0;
//
//    // 云端/本地单选按钮
//    private RadioGroup mRadioGroup;
//    // 引擎类型
//    private String mEngineType = SpeechConstant.TYPE_CLOUD;
//
//    private Toast mToast;
//    private SharedPreferences mSharedPreferences;
//    private Context context;
//    private String text;
//    private AIUIView mView;
//
//    public TtsXuFei(AIUIView aiuiView) {
//        mView = aiuiView;
//    }
//
//    public void initContent(Context context, String message) {
//        // 初始化合成对象
//        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
//        setParam();
//        this.context = context;
//        mSharedPreferences = context.getSharedPreferences("VOICE", MODE_PRIVATE);
//        text = message;
//    }
//
//    public void start() {
//        int code = mTts.startSpeaking(text, mTtsListener);
////			/**
////			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
////			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
////			*/
//			/*String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);*/
//
//        if (code != ErrorCode.SUCCESS) {
//            showTip("语音合成失败,错误码: " + code);
//        }
//    }
//
//    /**
//     * 初始化监听。
//     */
//    private InitListener mTtsInitListener = new InitListener() {
//        @Override
//        public void onInit(int code) {
//            Log.d(TAG, "InitListener init() code = " + code);
//            if (code != ErrorCode.SUCCESS) {
//                showTip("初始化失败,错误码：" + code);
//            } else {
//                // 初始化成功，之后可以调用startSpeaking方法
//                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
//                // 正确的做法是将onCreate中的startSpeaking调用移至这里
//            }
//        }
//    };
//
//    /**
//     * 合成回调监听。
//     */
//    private SynthesizerListener mTtsListener = new SynthesizerListener() {
//
//        @Override
//        public void onSpeakBegin() {
//            showTip("开始播放");
//        }
//
//        @Override
//        public void onSpeakPaused() {
//            showTip("暂停播放");
//        }
//
//        @Override
//        public void onSpeakResumed() {
//            showTip("继续播放");
//        }
//
//        @Override
//        public void onBufferProgress(int percent, int beginPos, int endPos,
//                                     String info) {
//            // 合成进度
//            mPercentForBuffering = percent;
////            showTip(String.format(getString(R.string.tts_toast_format),
////                    mPercentForBuffering, mPercentForPlaying));
//        }
//
//        @Override
//        public void onSpeakProgress(int percent, int beginPos, int endPos) {
//            // 播放进度
//            mPercentForPlaying = percent;
////            showTip(String.format(getString(R.string.tts_toast_format),
////                    mPercentForBuffering, mPercentForPlaying));
//
//            SpannableStringBuilder style = new SpannableStringBuilder(texts);
//            if (!"henry".equals(voicer) || !"xiaoyan".equals(voicer) ||
//                    !"xiaoyu".equals(voicer) || !"catherine".equals(voicer))
//                endPos++;
//            Log.e(TAG, "beginPos = " + beginPos + "  endPos = " + endPos);
//            style.setSpan(new BackgroundColorSpan(Color.RED), beginPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//
//        @Override
//        public void onCompleted(SpeechError error) {
//            if (error == null) {
//                showTip("播放完成");
//            } else if (error != null) {
//                showTip(error.getPlainDescription(true));
//            }
//        }
//
//        @Override
//        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
//            // 若使用本地能力，会话id为null
//            Log.e(TAG, "TTS Demo onEvent >>>" + eventType);
//            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
//                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
//                Log.d(TAG, "session id =" + sid);
//            }
//        }
//    };
//
//    private void showTip(final String str) {
//        mToast.setText(str);
//        mToast.show();
//    }
//
//    /**
//     * 参数设置
//     *
//     * @return
//     */
//    private void setParam() {
//        // 清空参数
//        mTts.setParameter(SpeechConstant.PARAMS, null);
//        // 根据合成引擎设置相应参数
//        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
//            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//            //onevent回调接口实时返回音频流数据
//            //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
//            // 设置在线合成发音人
//            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
//            //设置合成语速
//            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
//            //设置合成音调
//            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
//            //设置合成音量
//            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
//        } else {
//            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
//            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
//            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
//            /**
//             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
//             * 开发者如需自定义参数，请参考在线合成参数设置
//             */
//        }
//        //设置播放器音频流类型
//        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
//        // 设置播放合成音频打断音乐播放，默认为true
//        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
//
//        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.pcm");
//    }
//
//    public void destroy() {
//        if (null != mTts) {
//            mTts.stopSpeaking();
//            // 退出时释放连接
//            mTts.destroy();
//        }
//    }
//
//    public void detachView() {
//        destroy();
//        mView = null;
//    }
//}
//
