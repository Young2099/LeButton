package so.chinaso.com.voicemodule.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by yf on 2018/8/28.
 */
@Entity
public class RawMessage {
    //说的话
    private String voice = "";
    //返回的message
    private String message = "";
    //跳转的意图
    private String intent;
    //example 天气的数据
    private byte[] msgData;
    //需要跳转的值，如url和search,word;
    private String value;
    private boolean isLaunch = true;

    @PrimaryKey
    public long timestamp;

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getMsgData() {
        return msgData;
    }

    public void setMsgData(byte[] jsonObject) {
        this.msgData = jsonObject;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "RawMessageCache{" +
                "voice='" + voice + '\'' +
                ", message='" + message + '\'' +
                ", intent='" + intent + '\'' +
                ", msgData=" + msgData +
                ", value='" + value + '\'' +
                '}';
    }

    public boolean isLaunch() {
        return isLaunch;
    }

    public void setLaunch(boolean launch) {
        isLaunch = launch;
    }
}
