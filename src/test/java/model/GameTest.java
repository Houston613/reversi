package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void check() {
        MatrixForGame.fill();
        Game game = new Game();
        assertFalse((game.checker(7,7, Unit.Color.Black,0)));
        assertTrue((game.checker(5,4, Unit.Color.Black,0)));
        assertTrue((game.checker(5,3, Unit.Color.White,0)));

    }
}