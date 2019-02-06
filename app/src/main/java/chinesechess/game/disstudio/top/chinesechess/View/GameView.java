package chinesechess.game.disstudio.top.chinesechess.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Bean.Line;
import chinesechess.game.disstudio.top.chinesechess.Bean.LineList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Point;
import chinesechess.game.disstudio.top.chinesechess.Game.Game;
import chinesechess.game.disstudio.top.chinesechess.Game.OnStatusChangeListener;
import chinesechess.game.disstudio.top.chinesechess.Other.BaseActivity;
import chinesechess.game.disstudio.top.chinesechess.Other.MyApplication;
import chinesechess.game.disstudio.top.chinesechess.R;

public class GameView extends View implements View.OnTouchListener {

    private Game mGame;
    private Chess mSelectedChess;
    private int mBorderWidth, mStepWidth;
    private Rect mChessboardRect;
    private boolean mCanSelectOtherChess;
    private BaseActivity mActivity;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        Bitmap chessboardBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.chessborad_bg)).getBitmap();
        Rect chessboardSrcRect = new Rect(0, 0, chessboardBitmap.getWidth(), chessboardBitmap.getHeight());
        paint.setAntiAlias(true);

        //绘制背景
        canvas.drawBitmap(chessboardBitmap, chessboardSrcRect, new Rect(0, 0, getWidth(), getHeight()), paint);
        //绘制棋盘
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        //边框
        canvas.drawRect(mChessboardRect, paint);
        //水平线
        for (int i = 1 ; i < 9 ; i++) {
            canvas.drawLine(mChessboardRect.left, mChessboardRect.top + i * mStepWidth, mChessboardRect.right, mChessboardRect.top + i * mStepWidth, paint);
        }
        //垂直线
        for (int i = 1 ; i < 8 ; i++) {
            canvas.drawLine(mChessboardRect.left + i * mStepWidth, mChessboardRect.top, mChessboardRect.left + i * mStepWidth, mChessboardRect.top + mStepWidth * 4, paint);
            canvas.drawLine(mChessboardRect.left + i * mStepWidth,  mChessboardRect.top + mStepWidth * 5, mChessboardRect.left + i * mStepWidth, mChessboardRect.bottom, paint);
        }
        //斜线
        canvas.drawLine(mChessboardRect.left + 3 * mStepWidth, mChessboardRect.top, mChessboardRect.left + 5 * mStepWidth, mChessboardRect.top + 2 * mStepWidth, paint);
        canvas.drawLine(mChessboardRect.left + 3 * mStepWidth, mChessboardRect.top + 2 * mStepWidth, mChessboardRect.left + 5 * mStepWidth, mChessboardRect.top, paint);
        canvas.drawLine(mChessboardRect.left + 3 * mStepWidth, mChessboardRect.top + 9 * mStepWidth, mChessboardRect.left + 5 * mStepWidth, mChessboardRect.top + 7 * mStepWidth, paint);
        canvas.drawLine(mChessboardRect.left + 3 * mStepWidth, mChessboardRect.top + 7 * mStepWidth, mChessboardRect.left + 5 * mStepWidth, mChessboardRect.top + 9 * mStepWidth, paint);
        //短线
        drawShortLine(canvas, paint, 0, 3);
        drawShortLine(canvas, paint, 1, 2);
        drawShortLine(canvas, paint, 2, 3);
        drawShortLine(canvas, paint, 4, 3);
        drawShortLine(canvas, paint, 6, 3);
        drawShortLine(canvas, paint, 7, 2);
        drawShortLine(canvas, paint, 8, 3);
        drawShortLine(canvas, paint, 0, 6);
        drawShortLine(canvas, paint, 1, 7);
        drawShortLine(canvas, paint, 2, 6);
        drawShortLine(canvas, paint, 4, 6);
        drawShortLine(canvas, paint, 6, 6);
        drawShortLine(canvas, paint, 7, 7);
        drawShortLine(canvas, paint, 8, 6);
        if (mGame == null) {
            return;
        }
        //绘制棋子
        for (Chess chess : mGame.getChessList()) {
            if (!chess.isIsDead()){
                if (!mCanSelectOtherChess || chess.getType() == getGame().getGameType()) {
                    canvas.drawBitmap(chess.getSrc(), chess.getSrcRect(), chess.getRect(), paint);
                } else {
                    Rect rect = new Rect(chess.getRect().right, chess.getRect().bottom, chess.getRect().left, chess.getRect().top);
                    canvas.drawBitmap(chess.getSrc(), chess.getSrcRect(), rect, paint);
                }
            }
        }
        //绘制路径点
        if (mSelectedChess != null){
            canvas.drawRect(mSelectedChess.getRect(), paint);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.GREEN);
            LineList lines = mSelectedChess.getActionableLines(mGame.getChessList());
            for (Line line : lines) {
                canvas.drawCircle(line.getTo().getX(mStepWidth) + mBorderWidth, line.getTo().getY(mStepWidth) + mBorderWidth * 1.1f, mStepWidth * 0.075f, paint);
            }
        }
    }
    private void drawShortLine(Canvas canvas, Paint paint, int x, int y) {
        int padding = (int)(mStepWidth * 0.075f);
        int lineWidth = (int)(mStepWidth * 0.2f);
        if (x != 0) {
            canvas.drawLine(mChessboardRect.left + x * mStepWidth - padding, mChessboardRect.top + y * mStepWidth - padding, mChessboardRect.left + x * mStepWidth - padding, mChessboardRect.top + y * mStepWidth - padding - lineWidth, paint);
            canvas.drawLine(mChessboardRect.left + x * mStepWidth - padding, mChessboardRect.top + y * mStepWidth - padding, mChessboardRect.left + x * mStepWidth - padding - lineWidth, mChessboardRect.top + y * mStepWidth - padding, paint);
            canvas.drawLine(mChessboardRect.left + x * mStepWidth - padding, mChessboardRect.top + y * mStepWidth + padding, mChessboardRect.left + x * mStepWidth - padding, mChessboardRect.top + y * mStepWidth + padding + lineWidth, paint);
            canvas.drawLine(mChessboardRect.left + x * mStepWidth - padding, mChessboardRect.top + y * mStepWidth + padding, mChessboardRect.left + x * mStepWidth - padding - lineWidth, mChessboardRect.top + y * mStepWidth + padding, paint);
        }
        if (x != 8) {
            canvas.drawLine(mChessboardRect.left + x * mStepWidth + padding, mChessboardRect.top + y * mStepWidth - padding, mChessboardRect.left + x * mStepWidth + padding, mChessboardRect.top + y * mStepWidth - padding - lineWidth, paint);
            canvas.drawLine(mChessboardRect.left + x * mStepWidth + padding, mChessboardRect.top + y * mStepWidth - padding, mChessboardRect.left + x * mStepWidth + padding + lineWidth, mChessboardRect.top + y * mStepWidth - padding, paint);
            canvas.drawLine(mChessboardRect.left + x * mStepWidth + padding, mChessboardRect.top + y * mStepWidth + padding, mChessboardRect.left + x * mStepWidth + padding, mChessboardRect.top + y * mStepWidth + padding + lineWidth, paint);
            canvas.drawLine(mChessboardRect.left + x * mStepWidth + padding, mChessboardRect.top + y * mStepWidth + padding, mChessboardRect.left + x * mStepWidth + padding + lineWidth, mChessboardRect.top + y * mStepWidth + padding, paint);
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode != MeasureSpec.AT_MOST) {
            width = (int)(height * 0.8f);
        } else if (widthMode != MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            height = (int)(width * 1.125);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            height = Math.max(width, height);
            width = (int)(height * 0.88f);
        }

        mBorderWidth = (int)(width * 0.055);
        mChessboardRect = new Rect(mBorderWidth, mBorderWidth, width - mBorderWidth, height - mBorderWidth);
        mStepWidth = mChessboardRect.width() / 8;

        setMeasuredDimension(width, height);
    }

    public Game getGame() {
        return mGame;
    }

    public void InitGame(final int type) {
        post(new Runnable() {
            @Override
            public void run() {
                mGame = new Game(type, mStepWidth, mBorderWidth) {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onPause() {
                    }

                    //游戏结束时
                    @Override
                    public void onFinish(boolean showDialog) {
                        if (showDialog){
                            //显示对话框
                            MyApplication.getForegroundActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(MyApplication.getForegroundActivity())
                                            .setCancelable(false)
                                            .setTitle("游戏结束")
                                            .setMessage(mGame.getGameType() != mGame.getCurrentType() ? "你赢了" : "你输了")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (mActivity != null) {
                                                        mActivity.finish();
                                                    }
                                                }
                                            })
                                            .show();
                                }
                            });
                        } else {
                            if (mActivity != null) {
                                mActivity.finish();
                            }
                        }
                    }
                };
                invalidate();
            }
        });
    }

    //是否可选择对手棋子
    public void setCanSelectOtherChess(boolean canSelectOtherChess) {
        mCanSelectOtherChess = canSelectOtherChess;
        postInvalidate();
    }

    //绑定活动（游戏结束时活动一起结束）
    public void bindActivity(BaseActivity activity) {
        mActivity = activity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //解析触摸点
        Point point = Point.parse((int)event.getX(), (int)event.getY(), mStepWidth);
        //获取触摸棋子
        Chess chess = mGame.getChessList().getByPoint(point);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {
                if (mSelectedChess == null) {
                    //选择棋子
                    if (chess != null && chess.getType() == mGame.getCurrentType() && (mCanSelectOtherChess || chess.getType() == getGame().getGameType())) {
                        mSelectedChess = chess;
                    }
                } else {
                    //更换选择棋子
                    if (chess != null && chess.getType() == mGame.getCurrentType() && (mCanSelectOtherChess || chess.getType() == getGame().getGameType())) {
                        mSelectedChess = chess;
                    } else if (mSelectedChess.getActionableLines(mGame.getChessList()).isInTo(point)) {
                        //移动
                        if (mGame.move(new Line(mSelectedChess.getSite(), point))) {
                            //取消选择
                            mSelectedChess = null;
                        }
                    }
                }
            }
        }
        //刷新
        invalidate();
        return true;
    }

}
