package so.chinaso.com.voicemodule.entity;

/**
 * 动态实体数据
 */

public class DynamicEntityData {
    public String resName;
    public String idName;
    public String idValue;
    public String syncData;

    public DynamicEntityData(String resName, String idName, String idValue, String syncData) {
        this.resName = resName;
        this.idName = idName;
        this.idValue = idValue;
        this.syncData = syncData;
    }
}
