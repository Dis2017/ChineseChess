package chinesechess.game.disstudio.top.chinesechess.Bean;

import java.util.ArrayList;

public class LineList extends ArrayList<Line> {

    public boolean isInTo(Point to) {
        for (Line line : this) {
            if (line.getTo().equals(to)) {
                return true;
            }
        }
        return false;
    }

}
