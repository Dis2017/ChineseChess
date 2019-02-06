package chinesechess.game.disstudio.top.chinesechess.Bean;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.List;

import chinesechess.game.disstudio.top.chinesechess.Other.MyApplication;

public abstract class Chess {

    public static final int TYPE_BLACK = Color.BLACK;
    public static final int TYPE_RED = Color.RED;

    private Bitmap mSrc;
    private int mWeight;
    private int mType;
    private int mGameType;
    private int mStep;
    private int mBorderWidth;
    private Rect mSrcRect;
    private Rect mRect;
    private Point mSite;
    private boolean mIsDead;

    public Chess(int srcResId, int type, int gameType, Point site, int step, int borderWidth) {
        mSrc = ((BitmapDrawable)MyApplication.getContext().getResources().getDrawable(srcResId)).getBitmap();
        mType = type;
        mGameType = gameType;
        mStep = step;
        mBorderWidth = borderWidth;
        mSite = site;
        mSrcRect = new Rect(0, 0, mSrc.getWidth(), mSrc.getHeight());
        changeRect();
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int mWeight) {
        this.mWeight = mWeight;
    }

    public Point getSite() {
        return mSite;
    }

    public void setSite(Point mSite) {
        this.mSite = mSite;
        changeRect();
    }

    public abstract LineList getActionableLines(ChessList chessList);

    public Bitmap getSrc() {
        return mSrc;
    }

    public int getType() {
        return mType;
    }

    public int getGameType() {
        return mGameType;
    }

    public Rect getRect() {
        return mRect;
    }

    public Rect getSrcRect() {
        return mSrcRect;
    }

    public boolean isIsDead() {
        return mIsDead;
    }

    public void setIsDead(boolean mIsDead) {
        this.mIsDead = mIsDead;
    }

    private void changeRect() {
        mRect = new Rect(mSite.getX(mStep) + mBorderWidth - mStep / 2, mSite.getY(mStep) + mBorderWidth - mStep / 2, mSite.getX(mStep) + mBorderWidth + mStep / 2, mSite.getY(mStep) + mBorderWidth + mStep / 2);
    }
}
