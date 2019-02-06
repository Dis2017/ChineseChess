package chinesechess.game.disstudio.top.chinesechess.Network;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Bean.Line;
import chinesechess.game.disstudio.top.chinesechess.Bean.Message;
import chinesechess.game.disstudio.top.chinesechess.Bean.Point;
import chinesechess.game.disstudio.top.chinesechess.Game.Game;
import chinesechess.game.disstudio.top.chinesechess.GameActivity;
import chinesechess.game.disstudio.top.chinesechess.Other.MyApplication;
import chinesechess.game.disstudio.top.chinesechess.View.GameView;

public class Client extends Thread {

    private String mIP;
    private int mPort;
    private String mName;
    private Socket mSocket;
    private Gson mGson;
    private BufferedReader mBufferedReader;
    private BufferedWriter mBufferedWriter;
    private GameView mGameView;
    private boolean mOtherBinded;
    private boolean mIsActive;

    //被链接时将Socket封装成Client
    public Client(Socket socket) {
        mSocket = socket;
        start();
    }
    //主动链接时使用IP，Port封装Client
    public Client(String ip, int port) {
        mIP = ip;
        mPort = port;
        start();
    }

    //创建Socket，BufferedWrite，BufferedRead，开启线程接收消息，发送个人信息
    @Override
    public void run() {
        super.run();
        mOtherBinded = false;
        mGson = new Gson();
        try {
            //创建Socket
            if (mSocket == null) {
                mSocket = new Socket(mIP, mPort);
                //标记为主动Client（主动发起的链接）
                mIsActive = true;
            }
            //创建Write、Reader
            mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mBufferedWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
            //开启监听线程
            new ReceiveThread().start();
            //发送个人信息
            Message msg = new Message();
            msg.what = GameActivity.WHAT_NAME;
            msg.text = MyApplication.getPlayerName();
            sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
            onFail();
        }
    }

    //关闭Client
    public void close() {
        try {
            //关闭Socket
            mSocket.close();
            //置空
            MyApplication.setClient(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //发送消息
    public void sendMessage(final Message msg) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String json = mGson.toJson(msg);
                    mBufferedWriter.write(json + "\n");
                    mBufferedWriter.flush();
                    if (msg.what == GameActivity.WHAT_REFUSE || msg.what == GameActivity.WHAT_CLOSE) {
                            close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //处理消息
    private void handleMessage(final Message msg) {
        switch (msg.what) {
            //对手同意游戏
            case GameActivity.WHAT_ACCEPT: {
                onAccept();
                break;
            }
            //对手拒绝游戏
            case GameActivity.WHAT_REFUSE: {
                onRefuse();
                close();
                break;
            }
            //对手绑定游戏
            case GameActivity.WHAT_BINDED: {
                mOtherBinded = true;
                if (mGameView != null) {
                    onReady();
                }
                break;
            }
            //移动棋子
            case GameActivity.WHAT_MOVE: {
                //封装成Line
                final Line line = new Line(new Point(msg.fromX, msg.fromY), new Point(msg.toX, msg.toY));
                MyApplication.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //移动
                        mGameView.getGame().move(line);
                        //刷新
                        mGameView.postInvalidate();
                    }
                });
                break;
            }
            //悔棋
            case GameActivity.WHAT_REGRET: {
                mGameView.getGame().unMove();
                //刷新
                mGameView.postInvalidate();
                break;
            }
            //关闭游戏
            case GameActivity.WHAT_CLOSE: {
                MyApplication.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MyApplication.getForegroundActivity())
                                .setCancelable(false)
                                .setTitle("提示")
                                .setMessage("对手逃跑，你赢了。")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //结束游戏
                                        mGameView.getGame().finish(false);
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });
                //关闭客户端
                close();
                break;
            }
            //接收对方游戏名
            case GameActivity.WHAT_NAME: {
                mName = msg.text;
                if (!mIsActive) {
                    //被动链接时通知用户有客户端链接
                    MyApplication.noticeClientConnect(this);
                }
                break;
            }
        }
    }

    //链接失败时
    private void onFail(){
        MyApplication.showToast("链接失败");
        //取消等待对话框（请求时显示）
        MyApplication.dismissWaitDialog();
    }
    //对手同意对战时
    private void onAccept(){
        //注册客户端
        MyApplication.setClient(this);
        //跳转到游戏界面
        Intent intent = new Intent(MyApplication.getContext(), GameActivity.class);
        intent.putExtra(GameActivity.NAME_GAME_TYPE, Chess.TYPE_RED);
        MyApplication.getForegroundActivity().startActivity(intent);
    }
    //对手拒绝游戏时
    private void onRefuse() {
        MyApplication.dismissWaitDialog();
        MyApplication.showToast("对手拒绝游戏");
    }
    //准备开始游戏时
    private void onReady() {
        //取消等待对话框（请求时显示）
        MyApplication.dismissWaitDialog();
        //通知开始游戏
        mGameView.getGame().start();
    }

    //绑定GameView（双方都完成绑定之后才能进入准备开始游戏）
    public void bindGameView(GameView gameView) {
        mGameView = gameView;
        //通知对手我方已绑定
        Message msg = new Message();
        msg.what = GameActivity.WHAT_BINDED;
        sendMessage(msg);
        //若对手已绑定
        if (mOtherBinded) {
            //准备开始游戏
            onReady();
        }
    }
    //获取用户名（对手）
    public String getUserName() {
        return mName;
    }

    //接受线程
    public class ReceiveThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    //读取对手的消息
                    String json = mBufferedReader.readLine();
                    if (json != null){
                        //封装成Message
                        Message msg = mGson.fromJson(json, Message.class);
                        //通知处理
                        handleMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

}
