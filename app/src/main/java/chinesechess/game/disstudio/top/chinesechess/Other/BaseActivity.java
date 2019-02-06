package chinesechess.game.disstudio.top.chinesechess.Other;

import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.setForegroundActivity(this);
        if (MyApplication.isPlayBackgroundMusic()) {
            MyApplication.getBackgroundMusicBinder().start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.getBackgroundMusicBinder().pause();
    }

}
