package model;

import java.util.ArrayList;

public class Game {
    //поменять на один лист Unit
    public static ArrayList<Integer> listOfRow = new ArrayList<>();
    public static ArrayList<Integer> listOfColumn = new ArrayList<>();
    public Renderer renderer = new Renderer();

    public Game(int score, int countForPlayer, int countForComp, int scoreForPlayer, int scoreForComp, Unit[][] table) {
        this.score = score;
        this.countForPlayer = countForPlayer;
        this.countForComp = countForComp;
        this.scoreForPlayer = scoreForPlayer;
        this.scoreForComp = scoreForComp;
        this.table = table;

    }

    private Unit[][] table;

    public Unit[][] getTable(boolean needNew) {
        if (needNew) {
            Unit[][] tempTable = new Unit[8][8];
            for (int i = 0; i <= 7; i++)
                for (int j = 0; j <= 7; j++)
                    tempTable[i][j] = new Unit(table[i][j].getColor(), table[i][j].getScore(),
                            table[i][j].getColumn(), table[i][j].getRow());
            return tempTable;
        }
        else return table;
    }


    public void setTable(Unit[][] tableSet, boolean needNew) {
        if (needNew) {
            Unit[][] tempTable = new Unit[8][8];
            for (int i = 0; i <= 7; i++)
                for (int j = 0; j <= 7; j++)
                    tempTable[i][j] = new Unit(tableSet[i][j].getColor(), tableSet[i][j].getScore(),
                            tableSet[i][j].getColumn(), tableSet[i][j].getRow());
            //заполнили все кроме углов которые ии будет пытаться забрать
            this.table = tempTable;
        }
        else this.table = tableSet;
    }
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

    private void changer(int column, int row, int currentColumn, int currentRow, Unit.Color color, int t, Unit[][] table,
                         boolean change){
        //Содержат координаты точек, цвет unit'a которых нужно изменить
        listOfColumn.clear();
        listOfRow.clear();


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

        while (currentColumn!=column || currentRow!=row) {

            if (color == Unit.Color.Black) {

                //изменяем счет в пользу компьютера если наш основой цвет - черный
                countForComp++;
                countForPlayer--;

                scoreForPlayer-=table[currentColumn][currentRow].getScore();
                scoreForComp+=table[currentColumn][currentRow].getScore();

            }else {
                countForComp--;
                countForPlayer++;

                scoreForPlayer+=table[currentColumn][currentRow].getScore();
                scoreForComp-=table[currentColumn][currentRow].getScore();
            }
            listOfColumn.add(currentColumn);
            listOfRow.add(currentRow);
            //если используется не только для подсчета, то меняем цвет у пройденных фишек
            if (change) {
                table[currentColumn][currentRow].setColor(color);
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

    public boolean checker(int column, int row, Unit.Color color, int t, Unit[][] table, boolean change){

        int temp = t+1;

        //хранит значение направления движения
        Line k = l[t];

        //текущие положения точки
        int currentColumn;
        int currentRow;

        //для вражеской фишки
        boolean enemyUnit = false;

        if (table[column][row].getColor() == Unit.Color.Transparent) {

            //начинаем с шага в опр. сторону
            currentRow = row + k.h;
            currentColumn = column + k.w;

            //если не вышли за поле
            while (currentColumn >= 0 && currentColumn <= 7 && currentRow >= 0 && currentRow <= 7 &&
                    (table[currentColumn][currentRow].getColor() != Unit.Color.Transparent) ) {

                //если был найден вражеский unit и далее есть unit нашего цвета, то ход валидный,
                // рекурсия заканчивается
                if (enemyUnit && table[currentColumn][currentRow].getColor() == color) {
                    //меняем цвет всех фишек, что мы прошли
                    changer(column,row,currentColumn,currentRow,color,t,table, change);
                    score--;
                    if (color == Unit.Color.Black) {
                        //изменяем счет в пользу компьютера если наш основой цвет - черный
                        countForComp++;
                        scoreForComp+=table[currentColumn][currentRow].getScore();
                    }else {
                        countForPlayer++;
                        scoreForPlayer+=table[currentColumn][currentRow].getScore();
                    }
                    if (change) {
                        table[column][row].setColor(color);
                    }
                    return true;
                    //если наткнулись на свою же фишку, то в этом направлении не ищем
                }else if (!enemyUnit && table[currentColumn][currentRow].getColor() == color)
                    break;


                //если нашли вражеский юнит, то запускаем с поиском в том же направлении
                //поэтому вместо temp  используем t
                //вместо row column используем текущие
                if (table[currentColumn][currentRow].getColor() != color) {
                    enemyUnit = true;
                    currentRow+= k.h;
                    currentColumn+= k.w;
                }
            }
            //если закончились направления
            if (temp==8)
                return false;
            //меняем направление поиска
            return checker(column, row, color, temp,table, change);
        }
        //если клетка не пустая, ход не валидный
        return false;
    }
}