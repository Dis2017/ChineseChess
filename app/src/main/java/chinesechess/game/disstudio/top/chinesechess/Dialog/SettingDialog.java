package chinesechess.game.disstudio.top.chinesechess.Dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import chinesechess.game.disstudio.top.chinesechess.Other.MyApplication;
import chinesechess.game.disstudio.top.chinesechess.Other.Utils;
import chinesechess.game.disstudio.top.chinesechess.R;

public class SettingDialog extends AppCompatDialog implements View.OnClickListener {

    private TextView mBackgroundMusicTextTv, mBackgroundMusicTv, mSoundEffectsTextTv, mSoundEffectsTv, mPlayerNameTextTv, mPlayerNameTv;

    public SettingDialog(Context context) {
        super(context, R.style.DialogTheme);
        setContentView(R.layout.dialog_setting);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        mBackgroundMusicTextTv = findViewById(R.id.setting_bgm_text_tv);
        mBackgroundMusicTv = findViewById(R.id.setting_bgm_switch_tv);
        mSoundEffectsTextTv = findViewById(R.id.setting_sound_effects_text_tv);
        mSoundEffectsTv = findViewById(R.id.setting_sound_effects_switch_tv);
        mPlayerNameTextTv = findViewById(R.id.setting_player_name_text_tv);
        mPlayerNameTv = findViewById(R.id.setting_player_name_tv);

        //设置Typeface
        Utils.setTypeface(mBackgroundMusicTv);
        Utils.setTypeface(mBackgroundMusicTextTv);
        Utils.setTypeface(mSoundEffectsTextTv);
        Utils.setTypeface(mSoundEffectsTv);
        Utils.setTypeface(mPlayerNameTv);
        Utils.setTypeface(mPlayerNameTextTv);

        //设置OnClickListener
        mBackgroundMusicTv.setOnClickListener(this);
        mPlayerNameTv.setOnClickListener(this);
        mSoundEffectsTv.setOnClickListener(this);

        //初始化数据
        updateBackgroundMusicText();
        updateSoundEffectsText();
        updatePlayerName();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.setting_bgm_switch_tv: {
                changeBackgroundMusic();
                break;
            }

            case R.id.setting_sound_effects_switch_tv: {
                changeSoundEffects();
                break;
            }

            case R.id.setting_player_name_tv: {
                new InputDialog(getContext(), "名称") {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public boolean onResult(boolean isOK, String text) {
                        if (isOK) {
                            if (text.length() <= 5 && text.length() > 0) {
                                MyApplication.setPlayerName(text);
                                updatePlayerName();
                            }
                            if (text.length() > 5) {
                                setErrorMessage("长度过长");
                                return false;
                            } else if (text.length() == 0) {
                                setErrorMessage("长度不能为零");
                                return false;
                            }
                        }
                        return true;
                    }

                }.show();
                break;
            }

        }
    }

    private void updatePlayerName() {
        String name = MyApplication.getPlayerName();
        mPlayerNameTv.setText(name != null ? name : "点击输入");
    }
    private void updateBackgroundMusicText() {
        if (MyApplication.isPlayBackgroundMusic()) {
            mBackgroundMusicTv.setText("打开");
            mBackgroundMusicTv.setTextColor(Color.GREEN);
        } else {
            mBackgroundMusicTv.setText("关闭");
            mBackgroundMusicTv.setTextColor(Color.RED);
        }
    }
    private void updateSoundEffectsText() {
        if (MyApplication.isPlaySoundEffects()) {
            mSoundEffectsTv.setText("打开");
            mSoundEffectsTv.setTextColor(Color.GREEN);
        } else {
            mSoundEffectsTv.setText("关闭");
            mSoundEffectsTv.setTextColor(Color.RED);
        }
    }
    private void changeBackgroundMusic() {
        MyApplication.setPlayBackgroundMusic(!MyApplication.isPlayBackgroundMusic());
        updateBackgroundMusicText();
    }
    private void changeSoundEffects() {
        MyApplication.setPlaySoundEffects(!MyApplication.isPlaySoundEffects());
        updateSoundEffectsText();
    }
}
