package model;

public class Game {
    private int turn = 61;
    //количество шагов до окончания игры

    public enum Line{
        horizontalRight(0,1),horizontalLeft(0,-1),down(1,0),up(-1,0),
        leftUp(-1,1),leftLow(1,-1),rightUp(-1,-1),rightLow(1,1);
        private final int h;//аналог row
        private final int w;//аналог column
        Line(int h, int w){
            this.h = h;
            this.w = w;
        }
        private int h() { return h; }
        private int w() { return w; }
    }

    //енам направлений


    //ходы игрока и компа. начинает человек - белые unit
    boolean pTurn = true;
    boolean cTurn = false;



    private static final Line[] l =  Line.values();

    //нужна для запуска рекурсии

    /**проверяем валидность хода
     * @param row - строка
     * @param column - столбец
     * @param color - цвет unit. Совпадает с цветом игрока или компьютера
     * @param t - переменная необходимая для движения по массиву l
     */

    public boolean checker(int row, int column, Unit.Color color, int t){

        int temp = t+1;

        //хранит значение направления движения
        Line k = l[t];

        //текущие положения точки
        int currentRow;
        int currentColumn;

        //для вражеской фишки

        boolean enemyUnit = false;

        if (MatrixForGame.matrixTable[row][column].getColor() == Unit.Color.Transparent) {
            //начинаем с шага в опр. сторону
            currentRow = row + k.h;
            currentColumn = column + k.w;

            //если не вышли за поле
            while (currentRow >= 0 && currentRow <= 7 && currentColumn >= 0 && currentColumn <= 7 &&
                    (MatrixForGame.matrixTable[currentRow][currentColumn].getColor() != Unit.Color.Transparent) ) {
                //если был найден вражеский unit и далее есть unit нашего цвета, то ход валидный,
                // рекурсия заканчивается
                if (enemyUnit && MatrixForGame.matrixTable[currentRow][currentColumn].getColor() == color)
                    return true;


                //если нашли вражеский юнит, то запускаем с поиском в том же направлении
                //поэтому вместо temp  используем t
                //вместо row column используем текущие
                if (MatrixForGame.matrixTable[currentRow][currentColumn].getColor() != color) {
                    enemyUnit = true;
                    currentRow+= k.h;
                    currentColumn+= k.w;
                }

            }
            if (temp==8)
                return false;
            return checker(row, column, color, temp);
        }
        //если клетка не пустая, ход не валидный
        return false;
    }
}
