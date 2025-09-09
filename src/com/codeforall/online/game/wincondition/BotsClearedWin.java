package com.codeforall.online.game.wincondition;

import com.codeforall.online.game.entities.BasePlayer;
import com.codeforall.online.game.entities.BotPlayer;
import java.util.List;

/** Vence quando não existe nenhum Bot vivo. */
public class BotsClearedWin implements WinCondition {
    @Override
    public Result check(List<BasePlayer> players) {
        for (BasePlayer p : players) {
            if (p instanceof BotPlayer && p.isAlive()) {
                return Result.ONGOING;
            }
        }
        return Result.WIN;
    }
}
