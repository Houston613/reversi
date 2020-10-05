package model;

import java.util.ArrayList;

public class Game {
    //поменять на один лист Unit
    public static ArrayList<Integer> listOfRow = new ArrayList<>();
    public static ArrayList<Integer> listOfColumn = new ArrayList<>();


    public Game(int score, int countForPlayer, int countForComp, int scoreForPlayer, int scoreForComp, Unit[][] table) {
        this.score = score;
        this.countForPlayer = countForPlayer;
        this.countForComp = countForComp;
        this.scoreForPlayer = scoreForPlayer;
        this.scoreForComp = scoreForComp;
        this.table = table;

    }
    private Unit[][] table;

    public Unit[][] getTable() { return table; }
    public void setTable(Unit[][] table) { this.table = table; }
    //таблица в которой изменения происходят в данный момент

    //количество оставшихся ходов
    private int score;
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    //кол-во фишек игрока
    private int countForPlayer;
    public int getCountForPlayer() { return countForPlayer; }
    public void setCountForPlayer(int countForPlayer) { this.countForPlayer = countForPlayer; }

    //кол-во фишек компа
    private int countForComp;
    public int getCountForComp() { return countForComp; }
    public void setCountForComp(int countForComp) { this.countForComp = countForComp; }

    //очки игрока
    private int scoreForPlayer;
    public int getScoreForPlayer() { return scoreForPlayer; }
    public void setScoreForPlayer(int scoreForPlayer) { this.scoreForPlayer = scoreForPlayer; }


    //очки компа
    private int scoreForComp;
    public int getScoreForComp() { return scoreForComp; }
    public void setScoreForComp(int scoreForComp) { this.scoreForComp = scoreForComp; }

    public enum Line{
        horizontalRight(0,1),horizontalLeft(0,-1),down(1,0),up(-1,0),
        rightUp(-1,1),leftLow(1,-1),leftUp(-1,-1),rightLow(1,1);
        private final int h;//аналог row
        private final int w;//аналог column
        Line(int h, int w){
            this.h = h;
            this.w = w;
        }
    }

    private void changer(int row, int column, int currentRow, int currentColumn, Unit.Color color, int t, Unit[][] table,
                         boolean change){
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
                countForComp++;
                countForPlayer--;

                scoreForPlayer-=table[currentRow][currentColumn].getScore();
                scoreForComp+=table[currentRow][currentColumn].getScore();

            }else {
                countForComp--;
                countForPlayer++;

                scoreForPlayer+=table[currentRow][currentColumn].getScore();
                scoreForComp-=table[currentRow][currentColumn].getScore();
            }
            listOfRow.add(currentRow);
            listOfColumn.add(currentColumn);
            //если используется не только для подсчета, то меняем цвет у пройденных фишек
            if (change) {
                table[currentRow][currentColumn].setColor(color);
            }
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
     * @param table - таблица содержащая unit
     * @param change - условие,позволяющие нам не менять цвета фигур на поле и не добавлять новые,
     *          если нужно только провести расчет
     */

    public boolean checker(int row, int column, Unit.Color color, int t, Unit[][] table, boolean change){

        int temp = t+1;

        //хранит значение направления движения
        Line k = l[t];

        //текущие положения точки
        int currentRow;
        int currentColumn;

        //для вражеской фишки
        boolean enemyUnit = false;

        if (table[row][column].getColor() == Unit.Color.Transparent) {

            //начинаем с шага в опр. сторону
            currentRow = row + k.h;
            currentColumn = column + k.w;

            //если не вышли за поле
            while (currentRow >= 0 && currentRow <= 7 && currentColumn >= 0 && currentColumn <= 7 &&
                    (table[currentRow][currentColumn].getColor() != Unit.Color.Transparent) ) {

                //если был найден вражеский unit и далее есть unit нашего цвета, то ход валидный,
                // рекурсия заканчивается
                if (enemyUnit && table[currentRow][currentColumn].getColor() == color) {
                    //меняем цвет всех фишек, что мы прошли
                    changer(row,column,currentRow,currentColumn,color,t,table, change);
                    score--;
                    if (color == Unit.Color.Black) {
                        //изменяем счет в пользу компьютера если наш основой цвет - черный
                        countForComp++;
                    }else {
                        countForPlayer++;
                    }
                    if (change) {
                        table[row][column].setColor(color);
                    }
                    return true;
                    //если наткнулись на свою же фишку, то в этом направлении не ищем
                }else if (!enemyUnit && table[currentRow][currentColumn].getColor() == color)
                    break;


                //если нашли вражеский юнит, то запускаем с поиском в том же направлении
                //поэтому вместо temp  используем t
                //вместо row column используем текущие
                if (table[currentRow][currentColumn].getColor() != color) {
                    enemyUnit = true;
                    currentRow+= k.h;
                    currentColumn+= k.w;
                }
            }
            //если закончились направления
            if (temp==8)
                return false;
            //меняем направление поиска
            return checker(row, column, color, temp,table, change);
        }
        //если клетка не пустая, ход не валидный
        return false;
    }
}