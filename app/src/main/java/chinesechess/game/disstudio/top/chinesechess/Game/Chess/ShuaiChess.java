package chinesechess.game.disstudio.top.chinesechess.Game.Chess;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Bean.ChessList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Line;
import chinesechess.game.disstudio.top.chinesechess.Bean.LineList;
import chinesechess.game.disstudio.top.chinesechess.Bean.Point;
import chinesechess.game.disstudio.top.chinesechess.R;

public class ShuaiChess extends Chess {

    public ShuaiChess(int gameType, int step, int borderWidth) {
        super(R.drawable.red_shuai, TYPE_RED, gameType, gameType == TYPE_RED ? new Point(4, 9) : new Point(4, 0), step, borderWidth);
    }

    @Override
    public LineList getActionableLines(ChessList chessList) {
        LineList lines = new LineList();
        int y = getSite().getY();
        int weight = getType() == getGameType() ? -1 : 1;
        Point to;
        Chess chess;

        //添线路
        if ((chessList.getByPoint(getSite().offset(0, 1)) == null || chessList.getByPoint(getSite().offset(0, 1)).getType() != getType())
                && ((getType() == getGameType() && getSite().getY() != 9) || (getType() != getGameType() && getSite().getY() != 2))) {
            lines.add(new Line(getSite(), getSite().offset(0, 1)));
        }

        if ((chessList.getByPoint(getSite().offset(0, -1)) == null || chessList.getByPoint(getSite().offset(0, -1)).getType() != getType())
                && ((getType() == getGameType() && getSite().getY() != 7) || (getType() != getGameType() && getSite().getY() != 0))) {
            lines.add(new Line(getSite(), getSite().offset(0, -1)));
        }

        if ((chessList.getByPoint(getSite().offset(1, 0)) == null || chessList.getByPoint(getSite().offset(1, 0)).getType() != getType())
                && getSite().getX() != 5) {
            lines.add(new Line(getSite(), getSite().offset(1, 0)));
        }

        if ((chessList.getByPoint(getSite().offset(-1, 0)) == null || chessList.getByPoint(getSite().offset(-1, 0)).getType() != getType())
                && getSite().getX() != 3) {
            lines.add(new Line(getSite(), getSite().offset(-1, 0)));
        }

        while (true) {
            y += weight;
            to = new Point(getSite().getX(), y);
            chess = chessList.getByPoint(to);
            if (chess != null || (y < 0 || y > 9)) {
                if (chess instanceof JiangChess) {
                    lines.add(new Line(getSite(), to));
                }
                break;
            }
        }

        return lines;
    }
}
