package model;
import java.util.ArrayList;
import static model.MatrixForGame.matrixTable;

public class Game {
    public static ArrayList<Integer> listOfRow = new ArrayList<>();
    public static ArrayList<Integer> listOfColumn = new ArrayList<>();

    //количество оставшихся ходов
    private int score = 60;
    public int getScore() { return score; }

    //очки игрока
    private int scoreForPlayer = 2;
    public int getScoreForPlayer() { return scoreForPlayer; }

    //очки компа
    private int scoreForComp = 2;
    public int getScoreForComp() { return scoreForComp; }

    public enum Line{
        horizontalRight(0,1),horizontalLeft(0,-1),down(1,0),up(-1,0),
        rightUp(-1,1),leftLow(1,-1),leftUp(-1,-1),rightLow(1,1);
        private final int h;//аналог row
        private final int w;//аналог column
        Line(int h, int w){
            this.h = h;
            this.w = w;
        }
        private int h() { return h; }
        private int w() { return w; }
    }

    private void changer(int row, int column, int currentRow, int currentColumn, Unit.Color color, int t){
        //Содержат координаты точек, цвет unit'a которых нужно изменить
        listOfRow.clear();
        listOfColumn.clear();


        //в enum Line противоположные направления разбиты по парам
        //поэтому если индекс направления четный, чтобы получить противоположное направление, необходимо добавить 1
        //если индекс нечетный, то вычесть 1
        Line m;
        if (t % 2 != 0) {
            m = l[t - 1];
        }
        else {
            m = l[t + 1];
        }
        currentRow+= m.h;
        currentColumn+=m.w;

        while (currentRow!=row || currentColumn!=column) {
            if (color == Unit.Color.Black) {
                //изменяем счет в пользу компьютера если наш основой цвет - черный
                scoreForComp++;
                scoreForPlayer--;

            }else {
                scoreForComp--;
                scoreForPlayer++;
            }
            listOfRow.add(currentRow);
            listOfColumn.add(currentColumn);
            matrixTable[currentRow][currentColumn].setColor(color);

            currentRow+= m.h;
            currentColumn+=m.w;

        }
    }

    //нужна для запуска рекурсии
    private static final Line[] l =  Line.values();


    /**проверяем валидность хода
     * @param row строка
     * @param column столбец
     * @param color цвет unit. Совпадает с цветом игрока или компьютера
     * @param t  переменная необходимая для движения по массиву l
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

        if (matrixTable[row][column].getColor() == Unit.Color.Transparent) {

            //начинаем с шага в опр. сторону
            currentRow = row + k.h;
            currentColumn = column + k.w;

            //если не вышли за поле
            while (currentRow >= 0 && currentRow <= 7 && currentColumn >= 0 && currentColumn <= 7 &&
                    (matrixTable[currentRow][currentColumn].getColor() != Unit.Color.Transparent) ) {

                //если был найден вражеский unit и далее есть unit нашего цвета, то ход валидный,
                // рекурсия заканчивается
                if (enemyUnit && matrixTable[currentRow][currentColumn].getColor() == color) {
                    //меняем цвет всех фишек, что мы прошли
                    changer(row,column,currentRow,currentColumn,color,t);
                    score--;
                    if (color == Unit.Color.Black) {
                        //изменяем счет в пользу компьютера если наш основой цвет - черный
                        scoreForComp++;
                    }else {
                        scoreForPlayer++;
                    }
                    return true;
                }


                //если нашли вражеский юнит, то запускаем с поиском в том же направлении
                //поэтому вместо temp  используем t
                //вместо row column используем текущие
                if (matrixTable[currentRow][currentColumn].getColor() != color) {
                    enemyUnit = true;
                    currentRow+= k.h;
                    currentColumn+= k.w;
                }

            }
            //если закончились направления
            if (temp==8)
                return false;
            //меняем направление поиска
            return checker(row, column, color, temp);
        }
        //если клетка не пустая, ход не валидный
        return false;
    }
}
