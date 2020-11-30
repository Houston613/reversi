
package model;

public class MatrixForGame {

    public static Unit[][] matrixTable = new Unit[8][8];

    public static void fill() {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++)
                matrixTable[i][j] = new Unit(Unit.Color.Transparent, 1, i, j);
        }

        for (int i = 1; i <= 6; i = i+5)
            for (int j = 0; j <= 7; j = j+7)
                matrixTable[i][j].setScore(-1);
        for (int i = 0; i <= 7; i = i+7)
            for (int j = 1; j <= 6; j = j+5)
                matrixTable[i][j].setScore(-1);

        for (int i = 0; i <= 7; i = i+7)
            for (int j = 0; j <= 7; j = j+7)
                matrixTable[i][j].setScore(10);

        for (int i = 1; i <= 6; i = i+5)
            for (int j = 1; j <= 6; j = j+5)
                matrixTable[i][j].setScore(-2);

        for (int i = 2; i <= 5; i = i+3)
            for (int j = 0; j <= 7; j = j+7)
                matrixTable[i][j].setScore(5);
    }

}

