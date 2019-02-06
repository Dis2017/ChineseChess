package chinesechess.game.disstudio.top.chinesechess;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import chinesechess.game.disstudio.top.chinesechess.Other.MyApplication;

public class BackgroundMusicService extends Service {

    private MediaPlayer mBackgroundMusicPlayer;

    //Music Binder
    public class MusicBinder extends Binder {

        public void start()
        {
            mBackgroundMusicPlayer.start();
        }

        public void pause() {
            mBackgroundMusicPlayer.pause();
        }

    }

    public BackgroundMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  new MusicBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBackgroundMusicPlayer = MediaPlayer.create(this, R.raw.background);
        mBackgroundMusicPlayer.setLooping(true);
    }
}
