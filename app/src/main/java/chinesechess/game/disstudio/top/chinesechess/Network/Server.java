package chinesechess.game.disstudio.top.chinesechess.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import chinesechess.game.disstudio.top.chinesechess.Other.MyApplication;

public class Server extends Thread {

    private ServerSocket mServerSocket;

    @Override
    public void run() {
        super.run();
        try {
            mServerSocket = new ServerSocket(0, 1);
            //提交绑定的端口
            MyApplication.setLocalPort(mServerSocket.getLocalPort());
            while (true) {
                //开始接受客户端链接
                Socket socket = mServerSocket.accept();
                new Client(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
