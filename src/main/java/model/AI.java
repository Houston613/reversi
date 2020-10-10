package model;
import controller.Controller;

import java.util.ArrayList;

import static controller.Controller.*;
import static model.MatrixForGame.matrixTable;

public class AI {
    private int tempBest = 0;
    private int best = 0;
    private Unit result = null;
    public void setToDefault(){
        best=0;
        result = null;
    }
    public Renderer renderer = new Renderer();
    private ArrayList<Unit> findAllMoves(Game game, Unit.Color color){
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

        game.setScore(tempScore);
        game.setScoreForComp(tempScoreForComp);
        game.setScoreForPlayer(tempScoreForPlayer);
        game.setCountForComp(tempCountForComp);
        game.setCountForPlayer(tempCountForPlayer);

        return allMoves;
    }

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
     * @param deep - глубина, которой уже достингли
     * @param alpha - максимальное значение для компа, меньше которого никогда не выбирать
     * @param beta - минимальное значение для игрока, больше которого не будет выбрано
     * @return
     */

    public Unit algorithm(Game game, Unit.Color color, int deep, int alpha, int beta){
        //макс глубина
        if (deep==3)
            return Controller.UNMODUNIT;
        Unit tempResult;
        deep++;
        //создаем стол до изменений
        ArrayList<Unit> moves = findAllMoves(game,color);
        Game gamePreTurn = new Game(game.getScore(), game.getCountForPlayer(), game.getCountForComp(), game.getScoreForPlayer(), game.getScoreForComp(), game.getTable(true));

        for (Unit move:moves){
            game.checker(move.getColumn(),move.getRow(),color,0, game.getTable(false), true);
            //создаем игру с копией рабочего поля
            //нужны персистентные структры данных

            if (color== Unit.Color.Black)
            tempResult = algorithm(game, Unit.Color.White,deep,alpha,beta);
            else tempResult = algorithm(game, Unit.Color.Black,deep,alpha,beta);

            //!!!!закоментить!!!!
            int checkpointScoreComp = game.getScoreForComp() + game.getCountForComp();
            int checkpointScorePlayer = game.getScoreForPlayer() + game.getCountForPlayer();

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

            if (deep==1) {
                if (best < tempBest) {
                    best = tempBest;
                    result = move;
                }
                tempBest = 0;
            }
        }
        return result;
    }
}