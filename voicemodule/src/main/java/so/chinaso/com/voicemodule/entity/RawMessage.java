package so.chinaso.com.voicemodule.entity;


import com.google.gson.JsonObject;

/**
 * Created by yf on 2018/8/28.
 */
public class RawMessage {
    //说的话
    private String voice = "";
    //返回的message
    private String message = "";
    //跳转的意图
    private String intent;
    //example 天气的数据
    private JsonObject jsonObject;
    //需要跳转的值，如url和search,word;
    private String value;

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

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RawMessage{" +
                "voice='" + voice + '\'' +
                ", message='" + message + '\'' +
                ", intent='" + intent + '\'' +
                ", jsonObject=" + jsonObject +
                ", value='" + value + '\'' +
                '}';
    }
}
