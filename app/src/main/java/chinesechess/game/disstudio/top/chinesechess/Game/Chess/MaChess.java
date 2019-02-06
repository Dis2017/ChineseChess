package chinesechess.game.disstudio.top.chinesechess.Game.Chess;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Bean.ChessList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Line;
import chinesechess.game.disstudio.top.chinesechess.Bean.LineList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Point;
import chinesechess.game.disstudio.top.chinesechess.R;

public class MaChess extends Chess {

    public MaChess(int type, int gameType, int x, int step, int borderWidth) {
        super(type == TYPE_RED ? R.drawable.red_ma : R.drawable.black_ma, type, gameType, type == gameType ? new Point(x, 9) : new Point(x, 0), step, borderWidth);
    }

    @Override
    public LineList getActionableLines(ChessList chessList) {
        LineList lines = new LineList();
        Point to;
        Chess chess;

        to = getSite().offset(0, 1);
        if (chessList.getByPoint(to) == null && to.getY() + 1 <= 9) {
            to = getSite().offset(-1, 2);
            chess = chessList.getByPoint(to);
            if (chess == null || chess.getType() != getType()) {
                lines.add(new Line(getSite(), to));
            }
            to = getSite().offset(1, 2);
            chess = chessList.getByPoint(to);
            if (chess == null || chess.getType() != getType()) {
                lines.add(new Line(getSite(), to));
            }
        }
        to = getSite().offset(0, -1);
        if (chessList.getByPoint(to) == null && to.getY() - 1 >= 0) {
            to = getSite().offset(-1, -2);
            chess = chessList.getByPoint(to);
            if (chess == null || chess.getType() != getType()) {
                lines.add(new Line(getSite(), to));
            }
            to = getSite().offset(1, -2);
            chess = chessList.getByPoint(to);
            if (chess == null || chess.getType() != getType()) {
                lines.add(new Line(getSite(), to));
            }
        }
        to = getSite().offset(1, 0);
        if (chessList.getByPoint(to) == null && to.getX() + 1 <= 8) {
            to = getSite().offset(2, -1);
            chess = chessList.getByPoint(to);
            if (chess == null || chess.getType() != getType()) {
                lines.add(new Line(getSite(), to));
            }
            to = getSite().offset(2, 1);
            chess = chessList.getByPoint(to);
            if (chess == null || chess.getType() != getType()) {
                lines.add(new Line(getSite(), to));
            }
        }
        to = getSite().offset(-1, 0);
        if (chessList.getByPoint(to) == null && to.getX() - 1 >= 0) {
            to = getSite().offset(-2, -1);
            chess = chessList.getByPoint(to);
            if (chess == null || chess.getType() != getType()) {
                lines.add(new Line(getSite(), to));
            }
            to = getSite().offset(-2, 1);
            chess = chessList.getByPoint(to);
            if (chess == null || chess.getType() != getType()) {
                lines.add(new Line(getSite(), to));
            }
        }

        return lines;
    }

}
