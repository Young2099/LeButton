package so.chinaso.com.voicemodule;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import so.chinaso.com.voicemodule.adapter.AutoPollAdapter;
import so.chinaso.com.voicemodule.adapter.VoiceAdapter;
import so.chinaso.com.voicemodule.entity.RawMessage;
import so.chinaso.com.voicemodule.voice.AIUIRepository;
import so.chinaso.com.voicemodule.voice.AIUIView;
import so.chinaso.com.voicemodule.widget.AutoPollRecyclerView;
import so.chinaso.com.voicemodule.widget.CircleButtonView;
import so.chinaso.com.voicemodule.widget.VoiceLineView;


/**
 * author: zhanghe
 * created on: 2018/8/5 16:41
 * description:语音界面
 * 使用的对应的是讯飞应用里面添加的国搜工具
 * AppId: 5b695d90
 */

public class VoiceActivity extends AppCompatActivity implements AIUIView, View.OnClickListener {
    private static String TAG = VoiceActivity.class.getSimpleName();

    private CircleButtonView mStartRecord;
    private AIUIRepository aiuiRepository;
    private VoiceLineView mVoline;
    private RecyclerView mRecyclerView;
    private AutoPollRecyclerView hotWordRecycler;
    private boolean mVadBegin = false;
    private List<String> list;
    private List<RawMessage> currentList = new ArrayList<>();
    ImageView imageHelp;
    ImageView btn_back;
    private VoiceAdapter adapter;
    NestedScrollView mScrollView;
    LinearLayout ll_content;
    private boolean isFlag;
    private float dy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        initLayout();
        business();

    }

    @SuppressLint("ClickableViewAccessibility")
    protected void business() {
        aiuiRepository = new AIUIRepository(this);
        aiuiRepository.attach(this);
        aiuiRepository.initAIUIAgent();
        aiuiRepository.initWords();
        AutoPollAdapter voiceAdapter = new AutoPollAdapter(list);
        hotWordRecycler.setLayoutManager(new LinearLayoutManager(VoiceActivity.this));
        hotWordRecycler.setAdapter(voiceAdapter);
        hotWordRecycler.setNestedScrollingEnabled(false);
        voiceAdapter.notifyDataSetChanged();
        mStartRecord.setOnLongClickListener(new CircleButtonView.OnLongClickListener() {
            @Override
            public void onLongClick() {
                aiuiRepository.stopCloudTTS();
                aiuiRepository.startVoice();
            }

            @Override
            public void onNoMinRecord(int currentTime) {

            }

            @Override
            public void onRecordFinishedListener() {

            }
        });

        imageHelp.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        mStartRecord.setOnClickListener(new CircleButtonView.OnClickListener() {
            @Override
            public void onClick() {
                if (!mVadBegin) {
                    Toast.makeText(VoiceActivity.this, "你好像未说话", 0).show();
                }
                aiuiRepository.stopAudio();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //当前RecyclerView显示出来的最后一个的item的position
                int lastPosition = -1;

                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }
                    isFlag = lastPosition == recyclerView.getLayoutManager().getItemCount() - 1;

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

        });

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mRecdy = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float d = event.getY() - mRecdy;
                        if (d < -500 && isFlag) {
                            mRecyclerView.setVisibility(View.GONE);
                            mScrollView.setVisibility(View.VISIBLE);
                            imageHelp.setClickable(false);
                            int resId = R.anim.layout_animation_fall_down;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(VoiceActivity.this, resId);
                            hotWordRecycler.setLayoutAnimation(animation);
                            hotWordRecycler.scheduleLayoutAnimation();
                            ll_content.scrollTo(0,0);
                        }
                        break;
                }
                return false;
            }
        });
        hotWordRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dy = motionEvent.getY();
                        Log.e(TAG, "onTouch: down ");
                        break;
                }
                return false;
            }
        });
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float d = event.getY() - dy;
                        Log.e(TAG, "onTouch: " + d);
                        if (d > 600) {
//                            int resId = R.anim.layout_animation_fall_down;
//                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(VoiceActivity.this, resId);
//                            ll_content.setLayoutAnimation(animation);
//                            ll_content.scheduleLayoutAnimation();
//                            ll_content.setVisibility(View.GONE);
//                            ll_content.scrollTo(0,-(ll_content.getHeight()));
                            Log.e(TAG, "dy: " + ll_content.getHeight());
//                            mScrollView.setVisibility(View.GONE);
                            ll_content.scrollTo(0,-ll_content.getHeight());
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mScrollView.setVisibility(View.GONE);
                        }else {
                            ll_content.scrollTo(0, (int) -d);

                        }
                        break;
                }
                return false;
            }

        });
    }

    /**
     * 初始化Layout。
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initLayout() {
        mStartRecord = findViewById(R.id.voice_circle);
        mVoline = findViewById(R.id.voice_voiceLine);
        imageHelp = findViewById(R.id.voice_help);
        btn_back = findViewById(R.id.voice_btn_back);
        mRecyclerView = findViewById(R.id.voice_recycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        hotWordRecycler = findViewById(R.id.voice_hot_word_recy);
        mScrollView = findViewById(R.id.scorllview);
        ll_content = findViewById(R.id.ll_content);


    }

    private float mRecdy;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aiuiRepository.detachView();

    }


    private void bindMsg(String _appName) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> mAllApps = this.getPackageManager().queryIntentActivities(mainIntent, 0);
        String pkg = "";
        String cls = "";
        for (ResolveInfo element : mAllApps) {
            pkg = element.activityInfo.packageName;
            cls = element.activityInfo.name;
            String appName = element.loadLabel(this.getPackageManager()).toString();
            if (appName.equals(_appName)) {
                ComponentName componet = new ComponentName(pkg, cls);
                Intent i = new Intent();
                i.setComponent(componet);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        }
    }

    @Override
    public void showInitMessage(RawMessage rawMessage) {
        currentList.add(rawMessage);
        adapter = new VoiceAdapter(currentList, VoiceActivity.this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showVolume(final int arg2) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mVoline.setVolume(arg2);
            }
        });
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setVadBegin(boolean b) {
        mVadBegin = b;
    }

    @Override
    public void showHotWord(List<String> list) {
        this.list = list;
    }

    @Override
    public void showVoice(final RawMessage rawMessage) {
        Intent intent = new Intent();
        switch (rawMessage.getIntent()) {
            case "GUOSOU.open_web":
                Class clazz = null;
                try {
                    clazz = Class.forName("com.chinaso.domino.activity.WebUrlActivity");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                assert clazz != null;
                intent.setClass(this, clazz);
                intent.putExtra("search_words", rawMessage.getMessage());
                startActivity(intent);
                break;
            case "GUOSOU.chinaso_search":
                Class webdetailClazz = null;
                try {
                    webdetailClazz = Class.forName("com.chinaso.domino.activity.WebDetailActivity");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                assert webdetailClazz != null;
                intent.setClass(this, webdetailClazz);
                intent.putExtra("search_words", rawMessage.getMessage());
                startActivity(intent);
                break;
            case "app":
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bindMsg(rawMessage.getMessage());
                    }
                }).start();
                break;
            default:
                adapter.add(rawMessage);
                adapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.voice_help) {
            mRecyclerView.setVisibility(View.GONE);
            imageHelp.setClickable(false);
            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(VoiceActivity.this, resId);
            hotWordRecycler.setLayoutAnimation(animation);
            hotWordRecycler.scheduleLayoutAnimation();
        } else if (i == R.id.voice_btn_back) {
            finish();
        }
    }


}
