package model;

import org.junit.jupiter.api.Test;

import static controller.Controller.*;
import static model.MatrixForGame.matrixTable;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void check() {
        MatrixForGame.fill();
        matrixTable[4][3].setColor(Unit.Color.Black);
        matrixTable[3][4].setColor(Unit.Color.Black);
        matrixTable[3][3].setColor(Unit.Color.White);
        matrixTable[4][4].setColor(Unit.Color.White);
        Game game = new Game(SCORE, COUNT_FOR_PLAYER, COUNT_FOR_COMP, SCORE_FOR_PLAYER, SCORE_FOR_COMP,matrixTable);
        assertFalse((game.checker(7,7, Unit.Color.Black,0,matrixTable, true)));
        assertTrue((game.checker(5,4, Unit.Color.Black,0,matrixTable, true)));
        assertSame(matrixTable[4][4].getColor(), Unit.Color.Black);
        assertTrue((game.checker(5,3, Unit.Color.White,0,matrixTable,true)));
        assertSame(matrixTable[4][3].getColor(), Unit.Color.Black);

    }
}