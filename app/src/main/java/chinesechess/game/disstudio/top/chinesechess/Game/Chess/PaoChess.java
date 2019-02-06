package chinesechess.game.disstudio.top.chinesechess.Game.Chess;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Bean.ChessList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Line;
import chinesechess.game.disstudio.top.chinesechess.Bean.LineList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Point;
import chinesechess.game.disstudio.top.chinesechess.R;

public class PaoChess extends Chess {

    public PaoChess(int type, int gameType, int x, int step, int borderWidth) {
        super(type == TYPE_RED ? R.drawable.red_pao : R.drawable.black_pao, type, gameType, type == gameType ? new Point(x, 7) : new Point(x, 2), step, borderWidth);
    }

    @Override
    public LineList getActionableLines(ChessList chessList) {
        LineList lines = new LineList();
        boolean flag = false;
        int x = getSite().getX(), y = getSite().getY();
        while (true) {
            y--;
            Point to = new Point(x, y);
            Chess chess = chessList.getByPoint(to);
            if (chess != null) {
                if (!flag) {
                    flag = true;
                } else {
                    if (chess.getType() != getType()) {
                        lines.add(new Line(getSite(), to));
                    }
                    break;
                }
            }
            if (to.getY() < 0) {
                break;
            }
            if (!flag) {
                lines.add(new Line(getSite(), to));
            }
        }
        flag = false;
        y = getSite().getY();
        while (true) {
            y++;
            Point to = new Point(x, y);
            Chess chess = chessList.getByPoint(to);
            if (chess != null) {
                if (!flag) {
                    flag = true;
                } else {
                    if (chess.getType() != getType()) {
                        lines.add(new Line(getSite(), to));
                    }
                    break;
                }
            }
            if (to.getY() > 9) {
                break;
            }
            if (!flag) {
                lines.add(new Line(getSite(), to));
            }
        }
        flag = false;
        y = getSite().getY();
        while (true) {
            x--;
            Point to = new Point(x, y);
            Chess chess = chessList.getByPoint(to);
            if (chess != null) {
                if (!flag) {
                    flag = true;
                } else {
                    if (chess.getType() != getType()) {
                        lines.add(new Line(getSite(), to));
                    }
                    break;
                }
            }
            if (to.getX() < 0) {
                break;
            }
            if (!flag) {
                lines.add(new Line(getSite(), to));
            }
        }
        flag = false;
        x = getSite().getX();
        while (true) {
            x++;
            Point to = new Point(x, y);
            Chess chess = chessList.getByPoint(to);
            if (chess != null) {
                if (!flag) {
                    flag = true;
                } else {
                    if (chess.getType() != getType()) {
                        lines.add(new Line(getSite(), to));
                    }
                    break;
                }
            }
            if (to.getX() > 8) {
                break;
            }
            if (!flag) {
                lines.add(new Line(getSite(), to));
            }
        }
        return lines;
    }
}
