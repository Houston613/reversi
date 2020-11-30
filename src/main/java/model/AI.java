package model;
import controller.Controller;

import java.util.ArrayList;

import static controller.Controller.*;
import static model.MatrixForGame.matrixTable;

public class AI {
    //временный лучший результат
    private int tempBest = 0;
    //конечный лучший результат
    private int best = 0;
    //лучший ход
    private Unit result = null;
    //после того как нашли и передали лучшие значение, с помощью этого метод возвращаем их в первоначальное положение
    public void setToDefault(){
        best=0;
        result = null;
    }


    /**
     *
     * @param game - игра, в которой ищем ходы
     * @param color - цвет, для коотрого ишем возможные ходы
     * @return - массив всех возможных ходов
     */
    public ArrayList<Unit> findAllMoves(Game game, Unit.Color color){
        ArrayList<Unit> allMoves = new ArrayList<>();
        //тк методы Checker и Changer изменяют значения очков,
        // надо вернуть их в первоначальное состояние после того как найдем все ходы
        int tempScore = game.getScore();
        int tempScoreForComp = game.getScoreForComp();
        int tempScoreForPlayer = game.getScoreForPlayer();
        int tempCountForComp = game.getCountForComp();
        int tempCountForPlayer = game.getCountForPlayer();

        for (int i = 0; i<=7; i++)
            for (int j = 0; j<=7; j++)
                //если ход валидный, добавляем в пул возможных ходов
                if (game.checker(i,j,color,0, game.getTable(false), false)){
                    allMoves.add(game.getTable(false)[i][j]);
                }
        //возвращаем значения игры в первоначальное состояние.
        //тк мы только считали и не добавляли элементы на игровое поле, с полем ничего делать не нужно
        game.setScore(tempScore);
        game.setScoreForComp(tempScoreForComp);
        game.setScoreForPlayer(tempScoreForPlayer);
        game.setCountForComp(tempCountForComp);
        game.setCountForPlayer(tempCountForPlayer);

        return allMoves;
    }

    /**
     *
     * @param game - игра которую нужно вернуть в прошлое состояние
     * @param gamePreTurn - игра, к значениям которой возвращаем состояние
     */

    private void gameReturn(Game game, Game gamePreTurn){
        game.setScore(gamePreTurn.getScore());
        game.setCountForPlayer(gamePreTurn.getCountForPlayer());
        game.setCountForComp(gamePreTurn.getCountForComp());
        game.setScoreForPlayer(gamePreTurn.getScoreForPlayer());
        game.setScoreForComp(gamePreTurn.getScoreForComp());
        game.setTable(gamePreTurn.getTable(false),true);
    }
    /**
     *
     * @param game - игровое поле, с которым работаем в данный момент
     * @param color - цвет
     * @param deep - глубина, которой уже достигли
     * @param alpha - максимальное значение для компа, меньше которого никогда не выбирать
     * @param beta - минимальное значение для игрока, больше которого не будет выбрано
     * @return
     */

    public Unit algorithm(Game game, Unit.Color color, int deep, int alpha, int beta){
        if (deep==3)
            //если достигли максимальной глубины, выходим из итерации и
            // передаем результат, показывающий что достигли максимальной глубины
            return Controller.UNMODUNIT;
        //хранит последний сделанный на итерации ход
        Unit tempResult;
        //глубина
        deep++;
        //создаем стол до изменений
        ArrayList<Unit> moves = findAllMoves(game,color);
        //сохраняем игру до изменений
        Game gamePreTurn = new Game(game.getScore(), game.getCountForPlayer(), game.getCountForComp(), game.getScoreForPlayer(), game.getScoreForComp(), game.getTable(true));
        //запускам цикл по всем возможным ходам
        for (Unit move:moves){
            //устанавливаем на игровое поле фигуру
            game.checker(move.getColumn(),move.getRow(),color,0, game.getTable(false), true);

            //запускаем следующую итерацию с другим цветом
            if (color== Unit.Color.Black)
            tempResult = algorithm(game, Unit.Color.White,deep,alpha,beta);
            else tempResult = algorithm(game, Unit.Color.Black,deep,alpha,beta);

            int checkpointScoreComp = game.getScoreForComp();
            int checkpointScorePlayer = game.getScoreForPlayer();

            if (checkpointScoreComp>alpha)
                alpha=checkpointScoreComp;

            if (checkpointScorePlayer<beta)
                beta=checkpointScorePlayer;

            if (beta>alpha){
                gameReturn(game,gamePreTurn);
                //завершаем просмотр этой ветки
                break;
            }

            //если достигли глубины
            if (tempResult == Controller.UNMODUNIT && checkpointScoreComp > tempBest)
                tempBest = checkpointScoreComp;

            //возвращаем старое положение до хода
            gameReturn(game,gamePreTurn);

            //если находимся на первой итерации и результат, переданный из веток самый лучший, то сохраняем его
            // и сохраняем ход как лучший
            if (deep==1) {
                if (best < tempBest) {
                    best = tempBest;
                    result = move;
                }
                //тк мы опять начинаем погружение с целью найти лучший резульат, необходимо обнулить эту перменную
                tempBest = 0;
            }
        }
        return result;
    }
}