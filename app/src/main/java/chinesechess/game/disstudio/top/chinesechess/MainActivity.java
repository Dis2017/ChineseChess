package chinesechess.game.disstudio.top.chinesechess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Dialog.AboutDialog;
import chinesechess.game.disstudio.top.chinesechess.Dialog.ChooseOpponentDialog;
import chinesechess.game.disstudio.top.chinesechess.Dialog.SettingDialog;
import chinesechess.game.disstudio.top.chinesechess.Other.BaseActivity;
import chinesechess.game.disstudio.top.chinesechess.Other.MyApplication;
import chinesechess.game.disstudio.top.chinesechess.Other.Utils;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button mStartGameBtn, mOnlineGameBtn, mSetttingBtn, mAboutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartGameBtn = findViewById(R.id.main_start_game_btn);
        mOnlineGameBtn = findViewById(R.id.main_online_game_btn);
        mSetttingBtn = findViewById(R.id.main_setting_btn);
        mAboutBtn = findViewById(R.id.main_about_btn);

        //设置Typeface
        Utils.setTypeface(mStartGameBtn, 25);
        Utils.setTypeface(mOnlineGameBtn, 25);
        Utils.setTypeface(mSetttingBtn, 25);
        Utils.setTypeface(mAboutBtn, 25);

        //设置OnClickListener
        mStartGameBtn.setOnClickListener(this);
        mOnlineGameBtn.setOnClickListener(this);
        mSetttingBtn.setOnClickListener(this);
        mAboutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.main_start_game_btn: {
                new AlertDialog.Builder(this)
                        .setTitle("选择模式")
                        .setItems(new String[]{"线下双人", "人机对战(暂未开放)"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                    intent.putExtra(GameActivity.NAME_GAME_TYPE, Chess.TYPE_RED);
                                    intent.putExtra(GameActivity.NAME_OFFLINE_GAME, true);
                                    startActivity(intent);
                                } else {
                                    //MyApplication.showToast("暂未开放");
                                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                    intent.putExtra(GameActivity.NAME_GAME_TYPE, Chess.TYPE_RED);
                                    intent.putExtra(GameActivity.NAME_AI_GAME, true);
                                    startActivity(intent);
                                }
                            }
                        })
                        .show();
                break;
            }

            case  R.id.main_online_game_btn: {
                new ChooseOpponentDialog(this).show();
                break;
            }

            case R.id.main_setting_btn: {
                new SettingDialog(this).show();;
                break;
            }

            case R.id.main_about_btn: {
                new AboutDialog(this).show();
                break;
            }

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                MyApplication.getForegroundActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MyApplication.getForegroundActivity())
                                .setTitle("提示")
                                .setMessage("确定退出程序吗？")
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
