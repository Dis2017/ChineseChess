package chinesechess.game.disstudio.top.chinesechess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import chinesechess.game.disstudio.top.chinesechess.Other.BaseActivity;

public class StartActivity extends AppCompatActivity implements Animation.AnimationListener {

    private TextView mLogoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        AnimationSet animationSet = new AnimationSet(true);

        mLogoTv = findViewById(R.id.start_logo_tv);
        animationSet.setAnimationListener(this);
        animationSet.addAnimation(new AlphaAnimation(0f, 1f));
        animationSet.addAnimation(new AlphaAnimation(1f, 0f));
        animationSet.setFillAfter(true);
        animationSet.setDuration(3000);
        mLogoTv.startAnimation(animationSet);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
