package model;

import org.junit.jupiter.api.Test;

import static controller.Controller.*;
import static model.MatrixForGame.matrixTable;
import static org.junit.jupiter.api.Assertions.*;

class AITest {

    @Test
    void algorithm() {
        AI ai = new AI();
        MatrixForGame.fill();
        matrixTable[4][3].setColor(Unit.Color.Black);
        matrixTable[3][4].setColor(Unit.Color.Black);
        matrixTable[3][3].setColor(Unit.Color.White);
        matrixTable[4][4].setColor(Unit.Color.White);
        Game game = new Game(SCORE, COUNT_FOR_PLAYER, COUNT_FOR_COMP, SCORE_FOR_PLAYER, SCORE_FOR_COMP,matrixTable);
        ai.algorithm(game, Unit.Color.Black,0,-1,100);
    }
}