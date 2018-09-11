package so.chinaso.com.voicemodule.entity;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Created by yf on 2018/9/3.
 */
public class VoiceEntity {
    private RawMessage rawMessage;

    //如天气返回的数据
    public VoiceEntity(String semanticResult, String voiceWords) {
        JsonObject result = null;
        JsonObject object = new JsonParser().parse(semanticResult).getAsJsonObject();
        String service = null;
        if (object.has("service")) {
            service = object.get("service").getAsString();
        }
        if (("2").equals(object.get("rc").getAsString()) || ("4").equals(object.get("rc").getAsString())) {
            setListMessage(voiceWords, "对不起主人，不能识别", "not_know", null);
        } else {
            //基本回答的结果
            String answer = null;
            if (object.has("answer")) {
                JsonObject jsonObject = object.getAsJsonObject("answer");
                answer = jsonObject.get("text").getAsString();
            }
            //如天气回答的data数据
            if (object.has("data")) {
                result = object.getAsJsonObject("data");
            }
            //解析关键字段，semantic里面的如自定义动态数据网站url
            if (object.has("semantic")) {
                JsonArray data = object.get("semantic").getAsJsonArray();
                String normValue = null;
                String value = null;
                String intent = null;
                for (JsonElement jsonElement : data) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    JsonArray slots = jsonObject.getAsJsonArray("slots");
                    intent = jsonObject.get("intent").getAsString();
                    for (int i = 0; i < slots.size(); i++) {
                        JsonObject object1 = slots.get(0).getAsJsonObject();
                        value = object1.get("value").getAsString();
                        if (object1.has("normValue")) {
                            normValue = object1.get("normValue").getAsString();
                        }
                    }
                }

                Log.e("TAG", "getJsonString: " + intent + "service:" + service + "value:" + value);
                /**
                 * 需要特殊处理
                 * GUOSOU.chinaso_search -> service 自定义字段，讯飞国搜客户端跳转网页
                 *  app -> service
                 *  telephone ->service
                 *
                 */
                if ("GUOSOU.open_web".equals(service) && "open_web".equals(intent)) {
                    setListMessage(voiceWords, normValue, service, null);
                } else if ("app".equals(service) || "LAUNCH".equals(intent)) {
                    setListMessage(voiceWords, value, service, null);
                } else {
                    setListMessage(voiceWords, answer, service, result);
                }

//                if ("telephone".equals(service) && "CONFIRM".equals(value) && "INSTRUCTION".equals(intent)) {
//                    getSearchMessage(voiceWords, value, service);
//                }


            } else {
                Log.e("TAG", "VoiceEntity: "+answer );
                setListMessage(voiceWords, answer, service, result);
            }
        }
    }

    /**
     * @param words   人的声音
     * @param value   返回值
     * @param service //意图
     * @param result  // 返回的结果
     */
    private void setListMessage(String words, String value, String service, JsonObject result) {
        rawMessage = new RawMessage();
        rawMessage.setIntent(service);
        rawMessage.setVoice(words);
        rawMessage.setMessage(value);
        if (result != null) {
            rawMessage.setMsgData(result.toString().getBytes());
        }
        rawMessage.setTimestamp(System.currentTimeMillis());
    }


    public RawMessage getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(RawMessage rawMessage) {
        this.rawMessage = rawMessage;
    }
}
