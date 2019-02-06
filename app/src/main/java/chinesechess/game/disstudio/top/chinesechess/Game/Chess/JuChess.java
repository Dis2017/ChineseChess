package chinesechess.game.disstudio.top.chinesechess.Game.Chess;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Bean.ChessList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Line;
import chinesechess.game.disstudio.top.chinesechess.Bean.LineList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Point;
import chinesechess.game.disstudio.top.chinesechess.R;

public class JuChess extends Chess {

    public JuChess(int type, int gameType, int x, int step, int borderWidth) {
        super(type == TYPE_RED ? R.drawable.red_ju : R.drawable.black_ju, type, gameType, type == gameType ? new Point(x, 9) : new Point(x, 0), step, borderWidth);
    }

    @Override
    public LineList getActionableLines(ChessList chessList) {
        LineList lines = new LineList();
        int x = getSite().getX(), y = getSite().getY();
        while (true) {
            y--;
            Point to = new Point(x, y);
            Chess chess = chessList.getByPoint(to);
            if (chess != null || y < 0) {
                if (chess != null && chess.getType() != getType()) {
                    lines.add(new Line(getSite(), to));
                }
                break;
            }
            lines.add(new Line(getSite(), to));
        }
        y = getSite().getY();
        while (true) {
            y++;
            Point to = new Point(x, y);
            Chess chess = chessList.getByPoint(to);
            if (chess != null || y > 9) {
                if (chess != null && chess.getType() != getType()) {
                    lines.add(new Line(getSite(), to));
                }
                break;
            }
            lines.add(new Line(getSite(), to));
        }
        y = getSite().getY();
        while (true) {
            x--;
            Point to = new Point(x, y);
            Chess chess = chessList.getByPoint(to);
            if (chess != null || x < 0) {
                if (chess != null && chess.getType() != getType()) {
                    lines.add(new Line(getSite(), to));
                }
                break;
            }
            lines.add(new Line(getSite(), to));
        }
        x = getSite().getX();
        while (true) {
            x++;
            Point to = new Point(x, y);
            Chess chess = chessList.getByPoint(to);
            if (chess != null || x > 8) {
                if (chess != null && chess.getType() != getType()) {
                    lines.add(new Line(getSite(), to));
                }
                break;
            }
            lines.add(new Line(getSite(), to));
        }
        return lines;
    }
}
