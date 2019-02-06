package chinesechess.game.disstudio.top.chinesechess.Game.Chess;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Bean.ChessList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Line;
import chinesechess.game.disstudio.top.chinesechess.Bean.LineList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Point;
import chinesechess.game.disstudio.top.chinesechess.R;

public class XiangChess extends Chess {

    public XiangChess(int type, int gameType, int x, int step, int borderWidth) {
        super(type == Chess.TYPE_RED ? R.drawable.red_xiang : R.drawable.black_xiang, type, gameType, type == gameType ? new Point(x, 9) : new Point(x, 0), step, borderWidth);
    }

    @Override
    public LineList getActionableLines(ChessList chessList) {
        LineList lines = new LineList();
        if ((getType() == getGameType() && getSite().getY() != 9) || (getType() != getGameType() && getSite().getY() != 4)) {
            if ((chessList.getByPoint(getSite().offset(2, 2)) == null || chessList.getByPoint(getSite().offset(2, 2)).getType() != getType())
                    && getSite().getX() != 8
                    && chessList.getByPoint(getSite().offset(1, 1)) == null) {
                lines.add(new Line(getSite(), getSite().offset(2, 2)));
            }
            if ((chessList.getByPoint(getSite().offset(-2, 2)) == null || chessList.getByPoint(getSite().offset(-2, 2)).getType() != getType())
                    && getSite().getX() != 8
                    && chessList.getByPoint(getSite().offset(-1, 1)) == null) {
                lines.add(new Line(getSite(), getSite().offset(-2, 2)));
            }
        }
        if ((getType() == getGameType() && getSite().getY() != 5) || (getType() != getGameType() && getSite().getY() != 0)) {
            if ((chessList.getByPoint(getSite().offset(2, -2)) == null || chessList.getByPoint(getSite().offset(2, -2)).getType() != getType())
                    && getSite().getX() != 8
                    && chessList.getByPoint(getSite().offset(1, -1)) == null) {
                lines.add(new Line(getSite(), getSite().offset(2, -2)));
            }
            if ((chessList.getByPoint(getSite().offset(-2, -2)) == null || chessList.getByPoint(getSite().offset(-2, -2)).getType() != getType())
                    && getSite().getX() != 0
                    && chessList.getByPoint(getSite().offset(-1, -1)) == null) {
                lines.add(new Line(getSite(), getSite().offset(-2, -2)));
            }
        }
        return lines;
    }

}
