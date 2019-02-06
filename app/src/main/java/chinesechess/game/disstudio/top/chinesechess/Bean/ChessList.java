package chinesechess.game.disstudio.top.chinesechess.Bean;

import java.util.ArrayList;

public class ChessList extends ArrayList<Chess> {

    public Chess getByPoint(Point site) {
        for (Chess chess : this) {
            if (!chess.isIsDead() && chess.getSite().equals(site)) {
                return chess;
            }
        }
        return null;
    }

}
