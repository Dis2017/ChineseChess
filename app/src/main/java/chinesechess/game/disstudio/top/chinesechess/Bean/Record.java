package chinesechess.game.disstudio.top.chinesechess.Bean;

public class Record {

    private Line mLine;
    private Chess mTarget, mDead;

    public Record(Line line, Chess target, Chess dead) {
        this.mLine = line;
        this.mTarget = target;
        this.mDead = dead;
    }
    
    public Line getLine() {
        return mLine;
    }

    public void setLine(Line line) {
        this.mLine = line;
    }

    public Chess getTarget() {
        return mTarget;
    }

    public void setTarget(Chess target) {
        this.mTarget = target;
    }

    public Chess getDead() {
        return mDead;
    }

    public void setDead(Chess dead) {
        this.mDead = dead;
    }
}
