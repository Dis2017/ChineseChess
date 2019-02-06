package chinesechess.game.disstudio.top.chinesechess.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.GameActivity;
import chinesechess.game.disstudio.top.chinesechess.Network.Client;
import chinesechess.game.disstudio.top.chinesechess.Other.MyApplication;
import chinesechess.game.disstudio.top.chinesechess.Other.Utils;
import chinesechess.game.disstudio.top.chinesechess.R;

public class ChooseOpponentDialog extends Dialog implements View.OnClickListener {

    private Button mOkBtn, mCancelBtn;
    private TextView mSelfTextTv, mOpponentTextTv, mVsTv;
    private TextView mOpponentIPTextTv, mSelfIPTextTv, mOpponentIPTv, mSelfIPTv;
    private TextView mOpponentPortTextTv, mSelfPortTextTv, mOpponentPortTv, mSelfPortTv;
    private AlertDialog mProgressDialog;

    public ChooseOpponentDialog(Context context) {
        super(context, R.style.DialogTheme);
        setContentView(R.layout.dialog_choose_opponent);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        mOkBtn = findViewById(R.id.choose_opponent_ok_btn);
        mCancelBtn = findViewById(R.id.choose_opponent_cancel_btn);
        mSelfTextTv = findViewById(R.id.choose_opponent_self_text_tv);
        mOpponentTextTv = findViewById(R.id.choose_opponent_opponent_text_tv);
        mVsTv = findViewById(R.id.choose_opponent_vs_tv);
        mOpponentIPTextTv = findViewById(R.id.choose_opponent_opponent_ip_text_tv);
        mSelfIPTextTv = findViewById(R.id.choose_opponent_self_ip_text_tv);
        mOpponentIPTv = findViewById(R.id.choose_opponent_opponent_ip_tv);
        mSelfIPTv = findViewById(R.id.choose_opponent_self_ip_tv);
        mOpponentPortTextTv = findViewById(R.id.choose_opponent_opponent_port_text_tv);
        mSelfPortTextTv = findViewById(R.id.choose_opponent_self_port_text_tv);
        mOpponentPortTv = findViewById(R.id.choose_opponent_opponent_port_tv);
        mSelfPortTv = findViewById(R.id.choose_opponent_self_port_tv);

        //设置Typeface
        Utils.setTypeface(mOkBtn);
        Utils.setTypeface(mCancelBtn);
        Utils.setTypeface(mSelfTextTv);
        Utils.setTypeface(mOpponentTextTv);
        Utils.setTypeface(mVsTv);
        Utils.setTypeface(mOpponentIPTextTv);
        Utils.setTypeface(mSelfIPTextTv);
        Utils.setTypeface(mOpponentIPTv);
        Utils.setTypeface(mSelfIPTv);
        Utils.setTypeface(mOpponentPortTextTv);
        Utils.setTypeface(mSelfPortTextTv);
        Utils.setTypeface(mOpponentPortTv);
        Utils.setTypeface(mSelfPortTv);

        //初始化
        mSelfIPTv.setText(Utils.getIP(getContext()));
        mSelfPortTv.setText("" + MyApplication.getLocalPort());
        mOpponentIPTv.setText("点击输入");
        mOpponentPortTv.setText("点击输入");

        //设置OnCLickListener
        mOkBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mOpponentIPTv.setOnClickListener(this);
        mOpponentPortTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.choose_opponent_ok_btn: {
                try {
                    String ip = mOpponentIPTv.getText().toString();
                    int port = Integer.parseInt(mOpponentPortTv.getText().toString());
                    MyApplication.showWaitDialog();
                    new Client(ip, port);
                    dismiss();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            }

            case R.id.choose_opponent_cancel_btn: {
                dismiss();
                break;
            }

            case R.id.choose_opponent_opponent_ip_tv: {
                new InputDialog(getContext(), "输入对手IP") {
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
                            if (text.length() < 7 || text.length() > 15) {
                                setErrorMessage("长度不合法");
                                return false;
                            } else if (text.equals(Utils.getIP(MyApplication.getContext())) || text.equals("127.0.0.1")) {
                                setErrorMessage("不能将自己作为对手");
                                return false;
                            }
                            mOpponentIPTv.setText(text);
                        }
                        return true;
                    }
                }.show();
                break;
            }

            case R.id.choose_opponent_opponent_port_tv: {
                new InputDialog(getContext(), "输入对手Port", InputType.TYPE_CLASS_NUMBER) {
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
                            if (text.length() == 0) {
                                setErrorMessage("长度不能为零");
                                return false;
                            }
                            try {
                                if (Integer.parseInt(text) > 65535) {
                                    setErrorMessage("端口不能大于65535");
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            mOpponentPortTv.setText(text);
                        }
                        return true;
                    }
                }.show();
                break;
            }

        }

    }

}
