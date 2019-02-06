package chinesechess.game.disstudio.top.chinesechess.Bean;

public class Point {

    private int mX, mY;

    public static Point parse(int x, int y, int step) {
        int parsedX = x / step;
        int parsedY = y / step;
        return new Point(parsedX, parsedY);
    }

    public Point(int x, int y) {
        this.mX = x;
        this.mY = y;
    }

    public int getX() {
        return mX;
    }

    public int getX(int step) {
        return mX * step;
    }

    public void setX(int x) {
        this.mX = x;
    }

    public int getY() {
        return mY;
    }

    public int getY(int step) {
        return mY * step;
    }

    public void setY(int y) {
        this.mY = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            if (((Point) obj).getX() == mX && ((Point) obj).getY() == mY) {
                return true;
            }
        }
        return false;
    }

    public Point offset(int x, int y) {
        return new Point(mX + x, mY + y);
    }

}
