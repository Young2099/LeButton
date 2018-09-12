package so.chinaso.com.voicemodule;

import android.annotation.SuppressLint;
import android.arch.core.util.Function;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import so.chinaso.com.voicemodule.adapter.AutoPollAdapter;
import so.chinaso.com.voicemodule.adapter.VoiceAdapter;
import so.chinaso.com.voicemodule.db.MessageDB;
import so.chinaso.com.voicemodule.db.MessageDao;
import so.chinaso.com.voicemodule.entity.RawMessage;
import so.chinaso.com.voicemodule.voice.AIUIRepository;
import so.chinaso.com.voicemodule.voice.AIUIView;
import so.chinaso.com.voicemodule.voice.ChatViewModel;
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

public class VoiceActivity extends AppCompatActivity implements View.OnClickListener, ItemClickItem {
    private static String TAG = VoiceActivity.class.getSimpleName();
    public static final Pattern emptyPattern = Pattern.compile("^\\s+$", Pattern.DOTALL);
    private CircleButtonView mStartRecord;
    //    private AIUIRepository aiuiRepository;
    private ChatViewModel chatViewModel;
    private VoiceLineView mVoline;
    private RecyclerView mVoiceRecy;
    private AutoPollRecyclerView hotWordRecycler;
    private boolean isTouch = false;
    private boolean mVadBegin = false;
    private ImageView imageHelp;
    private TextView textWord;
    private ImageView btn_back;
    private VoiceAdapter mVoiceAdapter;
    private NestedScrollView mScrollView;
    private AutoPollAdapter autoPollAdapter;

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
        mVoiceRecy = findViewById(R.id.voice_recycleView);
        hotWordRecycler = findViewById(R.id.voice_hot_word_recy);
        mScrollView = findViewById(R.id.scorllview);
    }

    protected void business() {
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        chatViewModel.init(this);
        chatViewModel.useLocationData();
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
        mVoiceRecy.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mVoiceAdapter = new VoiceAdapter(this);
        mStartRecord.setOnLongClickListener(new CircleButtonView.OnLongClickListener() {
            @Override
            public void onLongClick() {
                mVadBegin = false;
                chatViewModel.stopTTS();
                chatViewModel.startRecord();
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
                chatViewModel.stopRecord();

            }
        });
        imageHelp.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        autoPollAdapter = new AutoPollAdapter(chatViewModel.getVoiceWord());
        hotWordRecycler.setLayoutManager(new LinearLayoutManager(VoiceActivity.this));
        hotWordRecycler.setAdapter(autoPollAdapter);
        autoPollAdapter.notifyDataSetChanged();
        autoPollAdapter.setClickListener(this);
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
                            mVoiceRecy.setVisibility(View.VISIBLE);
                            isTouch = false;
                            imageHelp.setClickable(true);
                            textWord.setVisibility(View.INVISIBLE);
                            mScrollView.setVisibility(View.GONE);
                        } else {
                            isTouch = false;
                        }
                        break;
                }
                return isTouch;
            }

        });

        Transformations.map(chatViewModel.getInteractMessages(), new Function<List<RawMessage>, List<RawMessage>>() {
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
                    chatViewModel.fakeAIUIResult(rawMessage1);
                    showVoiceList(list);
                } else {
                    showVoiceList(rawMessageList);
                }
            }
        });
        chatViewModel.getVADEvent().observe(this, new Observer<AIUIEvent>() {
            @Override
            public void onChanged(@Nullable AIUIEvent aiuiEvent) {
                if (aiuiEvent.eventType == AIUIConstant.EVENT_VAD) {
                    switch (aiuiEvent.arg1) {
                        case AIUIConstant.VAD_BOS:
                            mVadBegin = true;
                            break;

                        //前端点超时消息
                        case 3:
                        case AIUIConstant.VAD_EOS: {
                            //唤醒状态下检测到后端点自动进入待唤醒模式
//                            if(mState == Constant.STATE_WAKEUP) {
//                                onWaitingWakeUp();
//                            }
                            break;
                        }

                        //音量消息
                        case AIUIConstant.VAD_VOL: {
                            showVolume(aiuiEvent.arg2);
                        }
                    }
                }
            }
        });
    }


    private void showVoiceList(List<RawMessage> rawMessageList) {
        mVoiceAdapter.setList(rawMessageList);
        mVoiceRecy.setAdapter(mVoiceAdapter);
        mVoiceRecy.scrollToPosition(mVoiceAdapter.getItemCount() - 1);
    }

    private float dy;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hotWordRecycler != null) {
            hotWordRecycler.stop();
        }

        MessageDB.getInstance(this).messageDao().deleteMessage();

    }


    public void showVolume(final int arg2) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mVoline.setVolume(arg2);
            }
        });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.voice_help) {
            mVoiceRecy.setVisibility(View.GONE);
            textWord.setVisibility(View.VISIBLE);
            hotWordRecycler.setVisibility(View.VISIBLE);
//            hotWordRecycler.start();
            imageHelp.setClickable(false);
            mScrollView.setVisibility(View.VISIBLE);
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

    @Override
    public void clickListener(String voice) {
        hotWordRecycler.setVisibility(View.GONE);
        mVoiceRecy.setVisibility(View.VISIBLE);
        isTouch = false;
        imageHelp.setClickable(true);
        textWord.setVisibility(View.INVISIBLE);
        mScrollView.setVisibility(View.GONE);
        doSend(voice);
    }

    private void doSend(String msg) {
        if (!TextUtils.isEmpty(msg) && !emptyPattern.matcher(msg).matches()) {
            chatViewModel.sendText(msg);
//            mChatBinding.editText.setText("");
        } else {
            Toast.makeText(this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
        }
    }


}
