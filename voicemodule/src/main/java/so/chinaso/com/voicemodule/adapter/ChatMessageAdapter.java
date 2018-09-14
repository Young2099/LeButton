package so.chinaso.com.voicemodule.adapter;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import so.chinaso.com.voicemodule.R;
import so.chinaso.com.voicemodule.db.MessageDB;
import so.chinaso.com.voicemodule.entity.PoetryEntity;
import so.chinaso.com.voicemodule.entity.RawMessage;
import so.chinaso.com.voicemodule.intent.RestaurantHandler;
import so.chinaso.com.voicemodule.intent.StoryHandler;
import so.chinaso.com.voicemodule.intent.WeatherHandler;
import so.chinaso.com.voicemodule.intent.player.AIUIPlayer;
import so.chinaso.com.voicemodule.intent.player.PoetryHandler;
import so.chinaso.com.voicemodule.voice.PlayerViewModel;

/**
 * Created by yf on 2018/8/27.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageHolder> {
    private static final int WEATHER = 1;
    private static final int NORMAL = 2;
    private static final int RESTAURANT = 3;
    private static final int POETRY = 4;
    private static final int WEB = 5;
    private static final int SEARCH = 6;
    private static final int LAUNCH_APP = 7;
    private static final int STORY = 8;
    private List<RawMessage> list;
    private Context mContext;
    private PlayerViewModel playerViewModel;

    public ChatMessageAdapter(Context context, PlayerViewModel playerViewModel) {
        mContext = context;
        this.playerViewModel = playerViewModel;
    }

    @Override
    public ChatMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatMessageHolder viewHolder = null;
        switch (viewType) {
            case NORMAL:
            case WEB:
            case SEARCH:
            case STORY:
            case LAUNCH_APP:
                viewHolder = new ChatMessageHolder(parent, R.layout.item_voice);
                break;
            case WEATHER:
            case RESTAURANT:
                viewHolder = new ChatMessageHolder(parent, R.layout.item_total_weather);
                break;
            case POETRY:
                viewHolder = new ChatMessageHolder(parent, R.layout.item_poetry);
                break;

        }
        return viewHolder;
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ChatMessageHolder holder, final int position) {
        if (list.get(position) == null) {
            return;
        }
        switch (getItemViewType(position)) {
            case WEATHER:
                setWeather(holder, list.get(position));
                break;
            case NORMAL:
                if (position == 0) {
                    holder.voice_layout.setVisibility(View.GONE);
                } else {
                    holder.voice_layout.setVisibility(View.VISIBLE);
                    holder.voice.setText(list.get(position).getVoice());
                }
                holder.message.setText(list.get(position).getMessage());
                break;
            case RESTAURANT:
                setRestaurant(list.get(position), holder);
                break;
            case POETRY:
                setPoetry(list.get(position), holder);
                break;
            case WEB:
                setShow(holder, list.get(position));
                if (list.get(position).isLaunch()) {
                    getWebIntent(list.get(position));
                }
                break;
            case SEARCH:
                setShow(holder, list.get(position));
                if (list.get(position).isLaunch()) {
                    getSearchIntent(list.get(position));
                }
                break;
            case LAUNCH_APP:
                setShow(holder, list.get(position));
                if (list.get(position).isLaunch()) {
                    getAppLaunch(list.get(position));
                }
                break;
            case STORY:
                holder.voice.setText(list.get(position).getVoice());
                holder.message.setText(list.get(position).getMessage());
                if (list.get(position).isLaunch()) {
                    getStory(list.get(position).getMsgData());
                    updateMessage(list.get(position));
                }
                break;
        }

////                    Intent intent = new Intent();
////                    intent.setAction(Intent.ACTION_DIAL);
////                    intent.setData(Uri.parse("tel:" + message));
////                    startActivity(intent);
//                    break;


    }

    private void getStory(byte[] msgData) {
        if (msgData == null) {
            return;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new String(msgData));
            List<AIUIPlayer.SongInfo> songList = new ArrayList<>();
            JSONArray list = jsonObject.optJSONArray("result");
            if (list != null) {
                for (int index = 0; index < list.length(); index++) {
                    JSONObject audio = list.optJSONObject(index);
                    String audioPath = audio.optString("playUrl");
                    String songName = audio.optString("name");
                    String author = audio.optString("author");
                    songList.add(new AIUIPlayer.SongInfo(author, songName, audioPath));
                    if (songList.size() != 0) {
                        playerViewModel.playList(songList);
                        Log.e("TAG", "getStory: " + songList.get(0).songName);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getAppLaunch(final RawMessage rawMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bindMsg(rawMessage.getMessage());
            }
        }).start();
        updateMessage(rawMessage);
    }

    private void getSearchIntent(RawMessage rawMessage) {
        Class webdetailClazz = null;
        try {
            webdetailClazz = Class.forName("com.chinaso.domino.activity.WebDetailActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startWeb(rawMessage, webdetailClazz);
    }

    private void getWebIntent(RawMessage rawMessage) {
        Class clazz = null;
        try {
            clazz = Class.forName("com.chinaso.domino.activity.WebUrlActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startWeb(rawMessage, clazz);
    }

    private void startWeb(RawMessage rawMessage, Class clazz) {
        assert clazz != null;
        Intent intent = new Intent(mContext, clazz);
        intent.putExtra("search_words", rawMessage.getMessage());
        mContext.startActivity(intent);
        updateMessage(rawMessage);
    }

    private void setShow(ChatMessageHolder holder, RawMessage rawMessage) {
        holder.message_layout.setVisibility(View.GONE);
        holder.voice.setText(rawMessage.getVoice());
        holder.voice_layout.setVisibility(View.VISIBLE);
    }

    @SuppressLint("CheckResult")
    private void updateMessage(final RawMessage rawMessage) {
        rawMessage.setLaunch(false);
        Completable
                .complete()
                .observeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() {
                        MessageDB.getInstance(mContext).messageDao().updateMessage(rawMessage);
                    }
                });
    }

    private void bindMsg(String _appName) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> mAllApps = mContext.getPackageManager().queryIntentActivities(mainIntent, 0);
        String pkg = "";
        String cls = "";
        for (ResolveInfo element : mAllApps) {
            pkg = element.activityInfo.packageName;
            cls = element.activityInfo.name;
            String appName = element.loadLabel(mContext.getPackageManager()).toString();
            if (appName.equals(_appName)) {
                ComponentName componet = new ComponentName(pkg, cls);
                Intent i = new Intent();
                i.setComponent(componet);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        }
    }

    private void setPoetry(RawMessage rawMessage, ChatMessageHolder holder) {
//        String answer = rawMessage.getMessage().replaceAll("\\[[a-zA-Z0-9]{2}\\]", "");
        holder.voice.setText(rawMessage.getVoice());
        if (rawMessage.getMsgData() == null) {
            return;
        }
        List<PoetryEntity> poetryEntities = new PoetryHandler().getFormatContent(rawMessage.getMsgData());
        StringBuilder stringBuilder = new StringBuilder();
        String p = poetryEntities.get(0).getShowContent().replaceAll("\\[[a-zA-Z0-9]{2}\\]", "");
        String[] poetry = p.split("。", p.length() - 2);
        for (int i = 0; i < poetry.length;
             i++) {
            if (i == poetry.length - 1) {
                stringBuilder.append(poetry[i].replaceAll("\\s*", ""));
            } else {
                stringBuilder.append(poetry[i].replaceAll("\\s*", "")).append("。").append("\n");
            }
        }

        holder.poetry_content.setText(stringBuilder);
        holder.poetry_dynasty.setText("(" + poetryEntities.get(0).getDynasty() + ") " + poetryEntities.get(0).getAuthor());
        holder.poetry_title.setText(poetryEntities.get(0).getTitle());
    }


    private void setRestaurant(RawMessage rawMessage, ChatMessageHolder holder) {
        if (rawMessage.getMsgData() == null) {
            return;
        }
        holder.voice.setText(rawMessage.getVoice());
        holder.message.setText(rawMessage.getMessage());
        holder.mWeatherRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        holder.mWeatherRecycler.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        RestaurantAdapter adapter = new RestaurantAdapter(mContext, new RestaurantHandler().getFormatContent(rawMessage.getMsgData()));
        holder.mWeatherRecycler.setAdapter(adapter);
    }


    private void setWeather(ChatMessageHolder holder, RawMessage rawMessage) {
        if (rawMessage.getMsgData() == null) {
            return;
        }
        holder.voice.setText(rawMessage.getVoice());
        holder.message.setText(rawMessage.getMessage());
        holder.mWeatherRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        WeatherAdapter adapter = new WeatherAdapter(mContext, new WeatherHandler().getFormatContent(rawMessage.getMsgData()));
        holder.mWeatherRecycler.setAdapter(adapter);
    }


    @Override
    public int getItemViewType(int position) {
        int type = 0;
        switch (list.get(position).getIntent()) {
            case "weather":
                type = WEATHER;
                break;
            case "restaurantSearch":
                type = RESTAURANT;
                break;
            case "poetry":
                type = POETRY;
                break;
            case "GUOSOU.open_web":
                type = WEB;
                break;
            case "GUOSOU.chinaso_search":
                type = SEARCH;
                break;
            case "app":
                type = LAUNCH_APP;
                break;
            case "story":
                type = STORY;
                break;
            default:
                type = NORMAL;
                break;
        }

        return type;
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<RawMessage> list) {
        this.list = list;
    }
}
