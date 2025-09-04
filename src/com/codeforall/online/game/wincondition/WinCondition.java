package com.codeforall.online.game.wincondition;

import com.codeforall.online.game.entities.BasePlayer;
import java.util.List;

public interface WinCondition {
    enum Result { ONGOING, WIN, LOSE }
    Result check(List<BasePlayer> players);
}
