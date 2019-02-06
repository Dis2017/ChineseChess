package chinesechess.game.disstudio.top.chinesechess.Other;

import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import chinesechess.game.disstudio.top.chinesechess.BackgroundMusicService;
import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Bean.Message;
import chinesechess.game.disstudio.top.chinesechess.GameActivity;
import chinesechess.game.disstudio.top.chinesechess.Network.Client;
import chinesechess.game.disstudio.top.chinesechess.Network.Server;


public class MyApplication extends Application {

    public static final String KEY_IS_PALY_BACKGROUND_MUSIC = "isPlayBackgroundMusic";
    public static final String KEY_IS_PALY_SOUND_EFFECTS = "isPlaySoundEffects";
    public static final String KEY_PLAYER_NAME = "palyerName";

    //提供全局Context
    public static Context getContext() {
        return mInstance.getApplicationContext();
    }
    //提供Typeface
    public static Typeface getTypeface() {
        return mInstance.mTypeface;
    }
    //是否启用背景音乐
    public static boolean isPlayBackgroundMusic() {
        return mInstance.mSettingSharedPrfs.getBoolean(KEY_IS_PALY_BACKGROUND_MUSIC, true);
    }
    //设置是否启用背景音乐
    public static void setPlayBackgroundMusic(boolean enable) {
        mInstance.mSettingSharedPrfs.edit().putBoolean(KEY_IS_PALY_BACKGROUND_MUSIC, enable).commit();
        if (enable) {
            mInstance.mBackgroundMusicBinder.start();
        } else {
            mInstance.mBackgroundMusicBinder.pause();
        }
    }
    //是否启用音效
    public static boolean isPlaySoundEffects() {
        return mInstance.mSettingSharedPrfs.getBoolean(KEY_IS_PALY_SOUND_EFFECTS, true);
    }
    //设置是否启用音效
    public static void setPlaySoundEffects(boolean enable) {
        mInstance.mSettingSharedPrfs.edit().putBoolean(KEY_IS_PALY_SOUND_EFFECTS, enable).commit();
    }
    //提供背景音乐的Binder
    public static BackgroundMusicService.MusicBinder getBackgroundMusicBinder() {
        return mInstance.mBackgroundMusicBinder;
    }
    //提供玩家名称
    public static String getPlayerName() {
        return mInstance.mSettingSharedPrfs.getString(KEY_PLAYER_NAME, null);
    }
    //设置玩家名称
    public static void setPlayerName(String name) {
        mInstance.mSettingSharedPrfs.edit().putString(KEY_PLAYER_NAME, name).commit();
    }
    //设置本地端口
    public static void setLocalPort(int port) {
        mInstance.mLoaclPort = port;
    }
    //提供本地端口
    public static int getLocalPort() {
        return mInstance.mLoaclPort;
    }
    //设置前台活动
    public static void setForegroundActivity(BaseActivity activity) {
        mInstance.mForegroundActivity = activity;
    }
    //提供前台活动
    public static BaseActivity getForegroundActivity() {
        return mInstance.mForegroundActivity;
    }
    //提供客户端
    public static Client getClient() {
        return mInstance.mClient;
    }
    //设置客户端
    public static void setClient(Client client) {
        mInstance.mClient = client;
    }
    //显示等待对话框
    public static void showWaitDialog() {
        if (mInstance.mWaitDialog == null) {
            mInstance.mWaitDialog = new AlertDialog.Builder(MyApplication.getForegroundActivity())
                    .setTitle("请稍等")
                    .setMessage("正在请求...")
                    .setCancelable(false)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
        }
        MyApplication.getForegroundActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInstance.mWaitDialog.show();
            }
        });
    }
    //取消等待对话框
    public static void dismissWaitDialog() {
        getForegroundActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mInstance.mWaitDialog != null){
                    mInstance.mWaitDialog.dismiss();
                }
            }
        });
    }
    //显示请求对战对话框
    public static void noticeClientConnect(final Client client) {
        getForegroundActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(getForegroundActivity())
                        .setTitle("提示")
                        .setMessage(client.getUserName() + "  向您提出了一个对局请求，是否接受？")
                        .setCancelable(false)
                        .setPositiveButton("接受", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setClient(client);
                                Message msg = new Message();
                                msg.what = GameActivity.WHAT_ACCEPT;
                                client.sendMessage(msg);
                                dialog.dismiss();
                                Intent intent = new Intent(getContext(), GameActivity.class);
                                intent.putExtra(GameActivity.NAME_GAME_TYPE, Chess.TYPE_BLACK);
                                getForegroundActivity().startActivity(intent);
                            }
                        })
                        .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Message msg = new Message();
                                msg.what = GameActivity.WHAT_REFUSE;
                                client.sendMessage(msg);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }
    //显示一个Toast
    public static void showToast(final String text) {
        getForegroundActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //播放音频
    public static void playSound(final int resId) {
        if (isPlaySoundEffects()) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (mInstance.mMediaPlayer != null) {
                        mInstance.mMediaPlayer.release();
                        mInstance.mMediaPlayer = null;
                    }
                    mInstance.mMediaPlayer = MediaPlayer.create(getContext(), resId);
                    mInstance.mMediaPlayer.start();
                }
            }.start();
        }
    }
    //在UI线程运行代码
    public static void runOnUiThread(Runnable runnable) {
        getForegroundActivity().runOnUiThread(runnable);
    }

    private static MyApplication mInstance;

    private Typeface mTypeface;
    private SharedPreferences mSettingSharedPrfs;
    private BackgroundMusicService.MusicBinder mBackgroundMusicBinder;
    private int mLoaclPort;
    private BaseActivity mForegroundActivity;
    private AlertDialog mWaitDialog;
    private Server mServer;
    private Client mClient;
    private MediaPlayer mMediaPlayer;

    private void Init() {
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/TencentFonts.ttf");
        mSettingSharedPrfs = getSharedPreferences("Setting", MODE_PRIVATE);
        bindService(new Intent(this, BackgroundMusicService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBackgroundMusicBinder = (BackgroundMusicService.MusicBinder)service;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);
        mServer = new Server();
        mServer.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Init();
    }

}
