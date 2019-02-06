package chinesechess.game.disstudio.top.chinesechess.Game.Chess;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Bean.ChessList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Line;
import chinesechess.game.disstudio.top.chinesechess.Bean.LineList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Point;
import chinesechess.game.disstudio.top.chinesechess.R;

public class BinChess extends Chess {

    public BinChess(int type, int gameType, int x, int step, int borderWidth) {
        super(type == TYPE_RED ? R.drawable.red_bing : R.drawable.black_bing, type, gameType, type == gameType ? new Point(x, 6) : new Point(x, 3), step, borderWidth);
    }

    @Override
    public LineList getActionableLines(ChessList chessList) {
        LineList lines = new LineList();
        Point to;
        Chess chess;

        to = getSite().offset(0, 1);
        if (getType() == getGameType()) {
            to = getSite().offset(0, -1);
        }
        chess = chessList.getByPoint(to);
        if ((chess == null || chess.getType() != getType()) && (to.getY() >= 0 || to.getY() <= 9)) {
            lines.add(new Line(getSite(), to));
        }

        if ((getType() == getGameType() && getSite().getY() <= 4) || (getType() != getGameType() && getSite().getY() >= 5)) {
            to = getSite().offset(-1, 0);
            chess = chessList.getByPoint(to);
            if ((chess == null || chess.getType() != getType()) && to.getX() >= 0) {
                lines.add(new Line(getSite(), to));
            }
            to = getSite().offset(1, 0);
            chess = chessList.getByPoint(to);
            if ((chess == null || chess.getType() != getType()) && to.getX() >= 0) {
                lines.add(new Line(getSite(), to));
            }
        }

        return lines;
    }
}
