package so.chinaso.com.voicemodule;

import android.annotation.SuppressLint;
import android.arch.core.util.Function;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
    private boolean isTouch = false;
    private boolean mVadBegin = false;
    private List<String> list;
    private List<RawMessage> currentList = new ArrayList<>();
    ImageView imageHelp;
    TextView textWord;
    ImageView btn_back;
    private VoiceAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        initLayout();
        business();
    }

    private void initLayout() {
        mStartRecord = findViewById(R.id.voice_circle);
        mVoline = findViewById(R.id.voice_voiceLine);
        imageHelp = findViewById(R.id.voice_help);
        textWord = findViewById(R.id.voice_close_word);
        btn_back = findViewById(R.id.voice_btn_back);
        mRecyclerView = findViewById(R.id.voice_recycleView);
        hotWordRecycler = findViewById(R.id.voice_hot_word_recy);
    }

    protected void business() {
        aiuiRepository = ViewModelProviders.of(this).get(AIUIRepository.class);
        aiuiRepository.attach(this, this);
        aiuiRepository.initAIUIAgent();
        aiuiRepository.initWords();
        aiuiRepository.useLocationData();
        initAdapter();
    }

    /**
     * 初始化Layout。
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        final AutoPollAdapter autoPollAdapter = new AutoPollAdapter(list);
        hotWordRecycler.setAdapter(autoPollAdapter);
        autoPollAdapter.notifyDataSetChanged();
        hotWordRecycler.setLayoutManager(new LinearLayoutManager(VoiceActivity.this));
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
        mStartRecord.setUpListener(new CircleButtonView.UpListener() {
            @Override
            public void onUp() {
                if (!mVadBegin) {
                    Toast.makeText(VoiceActivity.this, "您好像并没有开始说话", Toast.LENGTH_LONG).show();
                }
                aiuiRepository.stopAudio();

            }
        });
        imageHelp.setOnClickListener(this);
        btn_back.setOnClickListener(this);

//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                //当前RecyclerView显示出来的最后一个的item的position
//                int lastPosition = -1;
//
//                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                    if (layoutManager instanceof LinearLayoutManager) {
//                        lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//                    }
//                    isFlag = lastPosition == recyclerView.getLayoutManager().getItemCount() - 1;
//
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//            }
//
//        });
//
//        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        dy = event.getY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float d = event.getY() - dy;
//
//                        if (d < -500 && isFlag) {
//                            mRecyclerView.scrollBy(0, (int) dy);
//                            mRecyclerView.requestLayout();
//                            final AutoPollAdapter voiceAdapter = new AutoPollAdapter(list);
//                            mRecyclerView.setVisibility(View.GONE);
//                            hotWordRecycler.setLayoutManager(new LinearLayoutManager(VoiceActivity.this));
//                            hotWordRecycler.setVisibility(View.VISIBLE);
//                            hotWordRecycler.start();
//                            int resId = R.anim.layout_animation_fall_down;
//                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(VoiceActivity.this, resId);
//                            hotWordRecycler.setLayoutAnimation(animation);
//                            hotWordRecycler.setAdapter(voiceAdapter);
//                            voiceAdapter.notifyDataSetChanged();
//                            hotWordRecycler.scheduleLayoutAnimation();
//                        }
//                        break;
//                }
//                return false;
//            }
//        });

        hotWordRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dy = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float d = event.getY() - dy;
                        Log.e(TAG, "onTouch: " + d);
                        if (d > 600) {
                            hotWordRecycler.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            isTouch = false;
                            imageHelp.setClickable(true);
                            textWord.setVisibility(View.INVISIBLE);
                        } else {
                            isTouch = false;
                        }
                        break;
                }
                return isTouch;
            }

        });

        adapter = new VoiceAdapter(this);
        Transformations.map(aiuiRepository.getInteractMsg(), new Function<List<RawMessage>, List<RawMessage>>() {
            @Override
            public List<RawMessage> apply(List<RawMessage> input) {
                return new ArrayList<>(input);
            }
        }).observe(this, new Observer<List<RawMessage>>() {
            @Override
            public void onChanged(@Nullable List<RawMessage> rawMessageList) {
                assert rawMessageList != null;
                if (rawMessageList.size() == 0) {
                    List<RawMessage> list = new ArrayList<>();
                    RawMessage rawMessage1 = new RawMessage();
                    rawMessage1.setVoice("你好，young");
                    rawMessage1.setValue("init");
                    rawMessage1.setMessage("你好，young");
                    rawMessage1.setIntent("initwords");
                    rawMessage1.setTimestamp(System.currentTimeMillis());
                    list.add(rawMessage1);
                    aiuiRepository.addMessageToDB(rawMessage1);
                    showVoiceList(list);
                } else {
                    showVoiceList(rawMessageList);
                }
            }
        });
    }


    private void showVoiceList(List<RawMessage> rawMessageList) {
        adapter.setList(rawMessageList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    private float dy;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aiuiRepository.detachView();
        if (hotWordRecycler != null) {
            hotWordRecycler.stop();
        }

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
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.voice_help) {
            mRecyclerView.setVisibility(View.GONE);
            textWord.setVisibility(View.VISIBLE);
            hotWordRecycler.setVisibility(View.VISIBLE);
//            hotWordRecycler.start();
            imageHelp.setClickable(false);
            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(VoiceActivity.this, resId);
            hotWordRecycler.setLayoutAnimation(animation);
            hotWordRecycler.scheduleLayoutAnimation();

        } else if (i == R.id.voice_close_word) {
            hotWordRecycler.setVisibility(View.GONE);
            imageHelp.setClickable(true);

        } else if (i == R.id.voice_btn_back) {
            finish();
        }
    }
}
