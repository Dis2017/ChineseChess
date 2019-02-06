package chinesechess.game.disstudio.top.chinesechess.Game.Chess;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Bean.ChessList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Line;
import chinesechess.game.disstudio.top.chinesechess.Bean.LineList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Point;
import chinesechess.game.disstudio.top.chinesechess.R;

public class ShiChess extends Chess {

    public ShiChess(int type, int gameType, int x, int step, int borderWidth) {
        super(type == TYPE_RED ? R.drawable.red_shi : R.drawable.black_shi, type, gameType, type == gameType ? new Point(x, 9) : new Point(x, 0), step, borderWidth);
    }

    @Override
    public LineList getActionableLines(ChessList chessList) {
        LineList lines = new LineList();
        Point to;
        Chess chess;

        for (int i = 0 ; i <= 1 ; i++) {
            for (int j = 0 ; j <= 1 ; j++) {
                to = getSite().offset(1 - 2 * i, 1 - 2 * j);
                chess = chessList.getByPoint(to);
                if ((chess == null || chess.getType() != getType())
                        && (to.getX() >= 3 && to.getX() <= 5)
                        && ((getType() == getGameType() && (to.getY() <= 9 && to.getY() >= 7)) || ((getType() != getGameType() && (to.getY() <= 2 && to.getY() >= 0))))) {
                    lines.add(new Line(getSite(), to));
                }
            }
        }

        return lines;
    }
}
