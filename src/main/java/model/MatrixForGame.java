
package model;

public class MatrixForGame {

    public static Unit[][] matrixTable = new Unit[8][8];

    public static void fill(){
        for (int i = 1; i < 7; i++)
            for (int j = 1; j < 7; j++)
                matrixTable[i][j] = new Unit(Unit.Color.Transparent,1,i,j);
        //заполнили все кроме углов которые ии будет пытаться забрать
        for (int i = 0; i <= 7; i = i+7)
            for (int j = 0; j <= 7; j++)
                matrixTable[i][j] = new Unit(Unit.Color.Transparent, 5,i,j);
        for (int i = 0; i <= 7; i++)
            for (int j = 0; j <= 7; j = j+7)
                matrixTable[i][j] = new Unit(Unit.Color.Transparent, 5,i,j);
    }
        //забили углы
    //заполнили таблицу пустыми клетками и задали начальные позиции

}

