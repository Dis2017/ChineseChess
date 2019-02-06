package chinesechess.game.disstudio.top.chinesechess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import chinesechess.game.disstudio.top.chinesechess.AI.AI;
import chinesechess.game.disstudio.top.chinesechess.Bean.Line;
import chinesechess.game.disstudio.top.chinesechess.Bean.Message;
import chinesechess.game.disstudio.top.chinesechess.Game.Game;
import chinesechess.game.disstudio.top.chinesechess.Game.OnActionableTypeChangeListener;
import chinesechess.game.disstudio.top.chinesechess.Game.OnStatusChangeListener;
import chinesechess.game.disstudio.top.chinesechess.Other.BaseActivity;
import chinesechess.game.disstudio.top.chinesechess.Other.MyApplication;
import chinesechess.game.disstudio.top.chinesechess.Other.Utils;
import chinesechess.game.disstudio.top.chinesechess.View.GameView;

public class GameActivity extends BaseActivity implements View.OnClickListener, OnStatusChangeListener, OnActionableTypeChangeListener {

    public static final String NAME_GAME_TYPE = "GameActivityGameType";
    public static final String NAME_OFFLINE_GAME= "OfflineGame";
    public static final String NAME_AI_GAME= "AIGame";

    public static final int WHAT_BINDED = 1;
    public static final int WHAT_ACCEPT = 2;
    public static final int WHAT_MOVE = 3;
    public static final int WHAT_REGRET = 4;
    public static final int WHAT_CLOSE = 5;
    public static final int WHAT_NAME = 6;
    public static final int WHAT_REFUSE = 7;

    private GameView mGameView;
    private Button mRegretGame1Btn, mRegretGame2Btn;
    private TextView mSelfNameTv, mOpponentNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGameView = findViewById(R.id.game_gv);
        mRegretGame1Btn = findViewById(R.id.game_regret_game_1_btn);
        mRegretGame2Btn = findViewById(R.id.game_regret_game_2_btn);
        mSelfNameTv = findViewById(R.id.game_self_name_tv);
        mOpponentNameTv = findViewById(R.id.game_opponent_name_tv);

        //设置Typeface
        Utils.setTypeface(mRegretGame1Btn, 20);
        Utils.setTypeface(mRegretGame2Btn, 20);
        Utils.setTypeface(mSelfNameTv);
        Utils.setTypeface(mOpponentNameTv);

        //设置OnClickListener
        mRegretGame1Btn.setOnClickListener(this);
        mRegretGame2Btn.setOnClickListener(this);

        //初始化
        mSelfNameTv.setText(MyApplication.getPlayerName());
        mRegretGame2Btn.setVisibility(View.GONE);
        mRegretGame2Btn.setRotationY(180);
        mRegretGame2Btn.setRotationX(180);
        mGameView.InitGame(getIntent().getIntExtra(NAME_GAME_TYPE, 0));
        mGameView.post(new Runnable() {
            @Override
            public void run() {
                mGameView.getGame().addStatusChangeListener(GameActivity.this);
                mGameView.getGame().addOnActionableTypeChangeListener(GameActivity.this);
                mGameView.bindActivity(GameActivity.this);
                //线下双人
                if (getIntent().getBooleanExtra(NAME_OFFLINE_GAME, false)) {
                    mGameView.getGame().start();
                    mGameView.setCanSelectOtherChess(true);
                    mRegretGame2Btn.setVisibility(View.VISIBLE);
                    mSelfNameTv.setVisibility(View.GONE);
                    mOpponentNameTv.setVisibility(View.GONE);
                }
                //人机对战
                else if (getIntent().getBooleanExtra(NAME_AI_GAME, false)) {
                    mGameView.getGame().start();
                    mSelfNameTv.setVisibility(View.GONE);
                    mOpponentNameTv.setVisibility(View.GONE);
                }
                //联机对战
                else {
                    MyApplication.getClient().bindGameView(mGameView);
                    mOpponentNameTv.setText(MyApplication.getClient().getName());
                }
                }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_regret_game_1_btn: {
                mGameView.getGame().unMove();
                if (MyApplication.getClient() != null) {
                    Message msg = new Message();
                    msg.what = WHAT_REGRET;
                    MyApplication.getClient().sendMessage(msg);
                }
                mGameView.invalidate();
                break;
            }
            case R.id.game_regret_game_2_btn: {
                mGameView.getGame().unMove();
                mGameView.invalidate();
                break;
            }
        }
    }

    @Override
    public void onStatusChange(int status) {
        switch (status) {
            case Game.STATIC_CHI: {
                MyApplication.playSound(R.raw.eat);
                break;
            }
            case Game.STATIC_JIANGJUN: {
                MyApplication.playSound(R.raw.jiangjun);
                break;
            }
            case Game.STATIC_MOVE: {
                MyApplication.playSound(R.raw.go);
                break;
            }
            case Game.STATIC_SONGJIANG: {
                MyApplication.showToast("这么走会送将");
                break;
            }
            case Game.STATIC_JUESHA: {
                break;
            }
        }
    }

    @Override
    public void onActionableTypeChange(final int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (type == mGameView.getGame().getGameType()) {
                    mRegretGame1Btn.setEnabled(true);
                    mRegretGame2Btn.setEnabled(false);

                } else {
                    mRegretGame1Btn.setEnabled(false);
                    mRegretGame2Btn.setEnabled(true);
                }
            }
        });
        if (type != mGameView.getGame().getGameType()) {
            //发送步骤
            sendLine(mGameView.getGame().getLastLine());
            //通知AI行动
            if (getIntent().getBooleanExtra(NAME_AI_GAME, false)) {
                AI.action(mGameView, type);
            }
        }
    }

    private void sendLine(Line line) {
        if (line != null && MyApplication.getClient() != null) {
            Message msg = new Message();
            msg.what = WHAT_MOVE;
            msg.fromX = 8 - line.getFrom().getX();
            msg.fromY = 9 - line.getFrom().getY();
            msg.toX = 8 - line.getTo().getX();
            msg.toY = 9 - line.getTo().getY();
            MyApplication.getClient().sendMessage(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //向对手发送关闭消息
        if (MyApplication.getClient() != null) {
            Message msg = new Message();
            msg.what = WHAT_CLOSE;
            MyApplication.getClient().sendMessage(msg);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                MyApplication.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MyApplication.getForegroundActivity())
                                .setTitle("提示")
                                .setMessage("确定退出对战吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
