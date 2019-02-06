package chinesechess.game.disstudio.top.chinesechess.AI;


import java.util.Random;

import chinesechess.game.disstudio.top.chinesechess.Bean.Chess;
import chinesechess.game.disstudio.top.chinesechess.Bean.Line;
import chinesechess.game.disstudio.top.chinesechess.Bean.LineList;
import chinesechess.game.disstudio.top.chinesechess.Game.Game;
import chinesechess.game.disstudio.top.chinesechess.View.GameView;

public class AI {

    public static LineList getActionableLines(Game game) {
        LineList lines = new LineList();
        for (Chess chess : game.getChessList()) {
            if (chess.getType() != game.getCurrentType() || chess.isIsDead()) {
                continue;
            }
            LineList lineList = chess.getActionableLines(game.getChessList());
            for (Line line : lineList) {
                if (game.canMove(line, false)) {
                    lines.add(line);
                }
            }
        }
        return  lines;
    }

    public static void action(final GameView gameView, final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //延迟执行，脱离onTouch的invalidate执行，保证界面真实性
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //逻辑
                Random random = new Random(System.currentTimeMillis());
                LineList lines = getActionableLines(gameView.getGame());
                if (lines.size() == 0) {
                    return;
                }
                gameView.getGame().move(lines.get(random.nextInt(lines.size())));
                gameView.postInvalidate();
            }
        }.start();
    }

}
