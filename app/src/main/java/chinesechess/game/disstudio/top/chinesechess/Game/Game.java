package chinesechess.game.disstudio.top.chinesechess.Game;

import java.util.ArrayList;
import java.util.List;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Bean.ChessList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Line;
import chinesechess.game.disstudio.top.chinesechess.Bean.LineList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Record;
import chinesechess.game.disstudio.top.chinesechess.Bean.RecordList;
import chinesechess.game.disstudio.top.chinesechess.Game.Chess.BinChess;
import chinesechess.game.disstudio.top.chinesechess.Game.Chess.JiangChess;
import chinesechess.game.disstudio.top.chinesechess.Game.Chess.JuChess;
import chinesechess.game.disstudio.top.chinesechess.Game.Chess.MaChess;
import chinesechess.game.disstudio.top.chinesechess.Game.Chess.PaoChess;
import chinesechess.game.disstudio.top.chinesechess.Game.Chess.ShiChess;
import chinesechess.game.disstudio.top.chinesechess.Game.Chess.ShuaiChess;
import chinesechess.game.disstudio.top.chinesechess.Game.Chess.XiangChess;

public abstract class Game {

    public final static int STATIC_JIANGJUN = 1;
    public final static int STATIC_CHI = 2;
    public final static int STATIC_SONGJIANG = 3;
    public final static int STATIC_JUESHA= 4;
    public final static int STATIC_MOVE= 5;

    private int mGameType;
    private ChessList mChessList;
    private RecordList mRecordList;
    private List<OnActionableTypeChangeListener> mOnActionableTypeChangeListenerList;
    private List<OnStatusChangeListener> mOnStatusChangeListenerList;
    private int mCurrentType;
    private boolean mStart;

    //初始化
    public Game(int gameType, int step, int borderWidth) {
        mGameType = gameType;
        mChessList = new ChessList();
        mRecordList = new RecordList();
        mOnActionableTypeChangeListenerList = new ArrayList<>();
        mOnStatusChangeListenerList = new ArrayList<>();
        mStart = false;
        mChessList.add(new JiangChess(mGameType, step, borderWidth));
        mChessList.add(new ShuaiChess(mGameType, step, borderWidth));
        mChessList.add(new ShiChess(Chess.TYPE_RED, gameType, 3, step, borderWidth));
        mChessList.add(new ShiChess(Chess.TYPE_RED, gameType, 5, step, borderWidth));
        mChessList.add(new ShiChess(Chess.TYPE_BLACK, gameType, 3, step, borderWidth));
        mChessList.add(new ShiChess(Chess.TYPE_BLACK, gameType, 5, step, borderWidth));
        mChessList.add(new XiangChess(Chess.TYPE_RED, gameType, 2, step, borderWidth));
        mChessList.add(new XiangChess(Chess.TYPE_RED, gameType, 6, step, borderWidth));
        mChessList.add(new XiangChess(Chess.TYPE_BLACK, gameType, 2, step, borderWidth));
        mChessList.add(new XiangChess(Chess.TYPE_BLACK, gameType, 6, step, borderWidth));
        mChessList.add(new MaChess(Chess.TYPE_RED, gameType, 1, step, borderWidth));
        mChessList.add(new MaChess(Chess.TYPE_RED, gameType, 7, step, borderWidth));
        mChessList.add(new MaChess(Chess.TYPE_BLACK, gameType, 1, step, borderWidth));
        mChessList.add(new MaChess(Chess.TYPE_BLACK, gameType, 7, step, borderWidth));
        mChessList.add(new JuChess(Chess.TYPE_RED, gameType, 0, step, borderWidth));
        mChessList.add(new JuChess(Chess.TYPE_RED, gameType, 8, step, borderWidth));
        mChessList.add(new JuChess(Chess.TYPE_BLACK, gameType, 0, step, borderWidth));
        mChessList.add(new JuChess(Chess.TYPE_BLACK, gameType, 8, step, borderWidth));
        mChessList.add(new PaoChess(Chess.TYPE_RED, gameType, 1, step, borderWidth));
        mChessList.add(new PaoChess(Chess.TYPE_RED, gameType, 7, step, borderWidth));
        mChessList.add(new PaoChess(Chess.TYPE_BLACK, gameType, 1, step, borderWidth));
        mChessList.add(new PaoChess(Chess.TYPE_BLACK, gameType, 7, step, borderWidth));
        mChessList.add(new BinChess(Chess.TYPE_RED, gameType, 0, step, borderWidth));
        mChessList.add(new BinChess(Chess.TYPE_RED, gameType, 2, step, borderWidth));
        mChessList.add(new BinChess(Chess.TYPE_RED, gameType, 4, step, borderWidth));
        mChessList.add(new BinChess(Chess.TYPE_RED, gameType, 6, step, borderWidth));
        mChessList.add(new BinChess(Chess.TYPE_RED, gameType, 8, step, borderWidth));
        mChessList.add(new BinChess(Chess.TYPE_BLACK, gameType, 0, step, borderWidth));
        mChessList.add(new BinChess(Chess.TYPE_BLACK, gameType, 2, step, borderWidth));
        mChessList.add(new BinChess(Chess.TYPE_BLACK, gameType, 4, step, borderWidth));
        mChessList.add(new BinChess(Chess.TYPE_BLACK, gameType, 6, step, borderWidth));
        mChessList.add(new BinChess(Chess.TYPE_BLACK, gameType, 8, step, borderWidth));
        mCurrentType = Chess.TYPE_RED;
    }

    //是否可移动(移动，还原，通知是否送将）
    public boolean canMove(Line line, boolean isNotice) {
        Chess chessFrom, chessTo, commander;
        boolean flag = true;

        //非法检测
        if (line == null || !isStart()) {
            return false;
        }
        chessFrom = getChessList().getByPoint(line.getFrom());
        if (chessFrom == null || chessFrom.getType() != getCurrentType() || !chessFrom.getActionableLines(getChessList()).isInTo(line.getTo())) {
            return false;
        }

        //移动
        chessTo = getChessList().getByPoint(line.getTo());
        if (chessTo != null) {
            chessTo.setIsDead(true);
        }
        chessFrom.setSite(line.getTo());

        //判断是否被将军
        commander = findCommander(getCurrentType());
        if (commander == null || isThreaten(commander)) {
            flag = false;
        }

        //还原
        if (chessTo != null) {
            chessTo.setIsDead(false);
        }
        chessFrom.setSite(line.getFrom());

        //通知
        if (isNotice && !flag) {
            noticeOnStatusChange(STATIC_SONGJIANG);
        }

        return flag;
    }
    //移动棋子（非法检测，移动，提交记录，更改行动方，将军、绝杀判断和通知）
    public boolean move(Line line) {
        return testMove(line, true);
    }
    private boolean testMove(Line line, boolean isDisplay) {

        //非法检测
        if (!canMove(line, isDisplay)) {
            return false;
        }

        //获取相关内容
        Chess chessFrom = getChessList().getByPoint(line.getFrom());
        Chess chessTo = getChessList().getByPoint(line.getTo());
        Chess commander;
        //建立记录
        Record record = new Record(line, chessFrom, null);

        //移动
        chessFrom.setSite(line.getTo());
        //吃棋
        if (chessTo != null) {
            chessTo.setIsDead(true);
            record.setDead(chessTo);
        }

        //提交记录
        mRecordList.Add(record);
        //使用用户设置更改行动方
        changeActionableType(isDisplay);

        //获取对手主帅
        commander = findCommander(getCurrentType());
        //判断是否将军
        if (commander != null && isThreaten(commander)) {
            //初始标记为绝杀
            boolean flag = false;
            LineList lines = new LineList();
            ChessList chessList = getChessListByType(commander.getType());
            for (Chess chess : chessList) {
                lines.addAll(chess.getActionableLines(getChessList()));
            }
            //绝杀判断
            for (Line l : lines) {
                if (canMove(l, false)) {
                    //标记将军
                    flag = true;
                    break;
                }
            }
            if (isDisplay) {
                //通知将军或绝杀
                noticeOnStatusChange(flag ? STATIC_JIANGJUN : STATIC_JUESHA);
            }
        }
        //通知吃或移动
        else {
            if (record.getDead() != null) {
                if (isDisplay) {
                    noticeOnStatusChange(STATIC_CHI);
                }
            }
            if (isDisplay) {
                noticeOnStatusChange(STATIC_MOVE);
            }
        }
        return true;
    }
    //撤销移动
    public void unMove() {
        Record record;
        Chess target, dead;
        Line line;
        for (int i = 0 ; i < 2 ; i++) {
            record = mRecordList.Get();

            //非法检测
            if (record == null || record.getTarget() == null || record.getLine() == null || !isStart()) {
                return;
            }

            //还原
            target = record.getTarget();
            dead = record.getDead();
            line = record.getLine();

            if (dead != null) {
                dead.setIsDead(false);
            }
            target.setSite(line.getFrom());

            mRecordList.Remove();
        }
    }
    //更改行动方
    private void changeActionableType(boolean isNotice) {
        mCurrentType = mCurrentType == Chess.TYPE_RED ? Chess.TYPE_BLACK : Chess.TYPE_RED;
        if (isNotice) {
            for (OnActionableTypeChangeListener onActionableTypeChangeListener : mOnActionableTypeChangeListenerList) {
                onActionableTypeChangeListener.onActionableTypeChange(mCurrentType);
            }
        }
    }
    //是否被威胁
    private boolean isThreaten(Chess chess) {
        ChessList chessList = getChessListByType(chess.getType() == Chess.TYPE_RED ? Chess.TYPE_BLACK : Chess.TYPE_RED);
        for (Chess c : chessList) {
            LineList lines = c.getActionableLines(getChessList());
            if (lines.isInTo(chess.getSite())) {
                return true;
            }
        }
        return false;
    }
    //寻找主帅
    private Chess findCommander(int type) {
        for (Chess chess : getChessList()) {
            if ((type == Chess.TYPE_RED && chess instanceof ShuaiChess) || (type == Chess.TYPE_BLACK && chess instanceof JiangChess)) {
                return chess;
            }
        }
        return null;
    }
    //发生状态更改通知
    private void noticeOnStatusChange(int status) {
        for (OnStatusChangeListener onStatusChangeListener : mOnStatusChangeListenerList) {
            onStatusChangeListener.onStatusChange(status);
        }
        if (status == STATIC_JUESHA) {
            finish(true);
        }
    }

    //Getter Setter
    public int getGameType() {
        return this.mGameType;
    }
    public ChessList getChessList() {
        return this.mChessList;
    }
    public ChessList getChessListByType(int type) {
        ChessList list = new ChessList();
        for (Chess chess : getChessList()) {
            if (chess.getType() == type && !chess.isIsDead()) {
                list.add(chess);
            }
        }
        return list;
    }
    public int getCurrentType() {
        return mCurrentType;
    }
    public void addOnActionableTypeChangeListener(OnActionableTypeChangeListener onActionableTypeChangeListener) {
        onActionableTypeChangeListener.onActionableTypeChange(getCurrentType());
        this.mOnActionableTypeChangeListenerList.add(onActionableTypeChangeListener);
    }
    public void addStatusChangeListener(OnStatusChangeListener onStatusChangeListener) {
        this.mOnStatusChangeListenerList.add(onStatusChangeListener);
    }
    public boolean isStart() {
        return mStart;
    }
    public void start() {
        mStart = true;
        onStart();
    }
    public void pause() {
        mStart = false;
        onPause();
    }
    public void finish(boolean showDialog) {
        onFinish(showDialog);
    }
    public Line getLastLine() {
        return mRecordList.Get() == null ? null : mRecordList.Get().getLine();
    }

    public abstract void onStart();
    public abstract void onPause();
    public abstract void onFinish(boolean showDialog);
}
