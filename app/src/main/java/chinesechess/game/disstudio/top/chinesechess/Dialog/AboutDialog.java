package chinesechess.game.disstudio.top.chinesechess.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.TextView;

import chinesechess.game.disstudio.top.chinesechess.Other.MyApplication;
import chinesechess.game.disstudio.top.chinesechess.Other.Utils;
import chinesechess.game.disstudio.top.chinesechess.R;

public class AboutDialog extends AppCompatDialog implements View.OnClickListener {

    private TextView mAppNameTv, mAppVersionTv, mIntroduceTv, mCopyrightTv;
    private View mBackground;

    public AboutDialog(Context context) {
        super(context, R.style.DialogTheme);
        setContentView(R.layout.dialog_about);
    }

    @Override
    public void setContentView(int resId) {

        super.setContentView(resId);

        mAppNameTv = findViewById(R.id.about_app_name_tv);
        mAppVersionTv = findViewById(R.id.about_app_version_tv);
        mIntroduceTv = findViewById(R.id.about_introduce_tv);
        mCopyrightTv = findViewById(R.id.about_copyright_tv);
        mBackground = findViewById(R.id.about_background_rl);

        //设置Typeface
        Utils.setTypeface(mAppNameTv);
        Utils.setTypeface(mAppVersionTv);
        Utils.setTypeface(mIntroduceTv);
        Utils.setTypeface(mCopyrightTv);

        //设置OnClickListener
        mBackground.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

}
