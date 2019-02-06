package chinesechess.game.disstudio.top.chinesechess.Dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import chinesechess.game.disstudio.top.chinesechess.Other.Utils;
import chinesechess.game.disstudio.top.chinesechess.R;

public  abstract class InputDialog extends AppCompatDialog implements View.OnClickListener, TextWatcher {

    private EditText mTextEt;
    private TextView mOKTv, mCancelTv, mErrorMsgTv;

    public InputDialog(Context context, String hint, int type) {
        super(context, R.style.InputDialogTheme);
        setContentView(R.layout.dialog_input, hint);
        setInputType(type);
    }
    public InputDialog(Context context, String hint) {
        this(context, hint, InputType.TYPE_CLASS_TEXT);
    }

    public void setContentView(int layoutResID, String hint) {
        super.setContentView(layoutResID);

        mTextEt = findViewById(R.id.input_text_et);
        mOKTv = findViewById(R.id.input_ok_tv);
        mCancelTv = findViewById(R.id.input_cancel_tv);
        mErrorMsgTv = findViewById(R.id.input_error_msg_tv);

        //设置Typeface
        Utils.setTypeface(mTextEt);
        Utils.setTypeface(mOKTv);
        Utils.setTypeface(mCancelTv);
        Utils.setTypeface(mErrorMsgTv);

        //设置OnClickListener
        mOKTv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);

        //初始化
        mTextEt.setHint(hint);
        mTextEt.setFocusable(true);
    }

    @Override
    public void onClick(View v) {

        boolean result = false;

        switch (v.getId()) {

            case R.id.input_ok_tv: {
                result = onResult(true, mTextEt.getText().toString());
                break;
            }

            case R.id.input_cancel_tv: {
                result = onResult(false, mTextEt.getText().toString());
                break;
            }

        }

        if (result) {
            dismiss();
        }

    }

    public void setErrorMessage(String text) {
        mErrorMsgTv.setText(text);
    }
    public void setInputType(int type) {
        mTextEt.setInputType(type);
    }

    public abstract boolean onResult(boolean isOK, String text);

}
