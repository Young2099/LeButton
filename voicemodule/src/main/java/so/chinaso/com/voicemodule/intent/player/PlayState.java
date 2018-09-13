package so.chinaso.com.voicemodule.intent.player;

/**
 * 播放状态
 */
public class PlayState {
    public boolean active;
    public boolean playing;
    //当前播放内容的标题信息
    public String info;

    public PlayState(boolean active, boolean playing, String info) {
        this.active = active;
        this.playing = playing;
        this.info = info;
    }
}
