package chinesechess.game.disstudio.top.chinesechess.Bean;

public class Line {

    private Point mFrom, mTo;

    public Line(Point mFrom, Point mTo) {
        this.mFrom = mFrom;
        this.mTo = mTo;
    }

    public Point getFrom() {
        return mFrom;
    }

    public void setFrom(Point mFrom) {
        this.mFrom = mFrom;
    }

    public Point getTo() {
        return mTo;
    }

    public void setTo(Point mTo) {
        this.mTo = mTo;
    }

}
