package model;
import controller.Controller;

import java.util.ArrayList;

import static controller.Controller.*;
import static model.MatrixForGame.matrixTable;

public class AI {
    //создаем игру с копией рабочего поля
    Unit result;


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
                if (game.checker(i,j,color,0, game.getTable(), false)){
                    game.getTable()[i][j].setColor(color);
                    allMoves.add(game.getTable()[i][j]);
                }

        game.setScore(tempScore);
        game.setScoreForComp(tempScoreForComp);
        game.setScoreForPlayer(tempScoreForPlayer);
        game.setCountForComp(tempCountForComp);
        game.setCountForPlayer(tempCountForPlayer);

        return allMoves;
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

        alpha = -1;
        beta = 100;

        //макс глубина
        if (deep==4)
            return Controller.UNMODUNIT;


        ArrayList<Unit> moves = findAllMoves(game,color);
        deep++;
        //создаем стол до изменений
        int best = 0;
        Game gamePreTurn = new Game(game.getScore(), game.getCountForPlayer(), game.getCountForComp(),
                game.getScoreForPlayer(), game.getScoreForComp(), game.getTable());

        for (Unit move:moves){
            gamePreTurn = new Game(game.getScore(), game.getCountForPlayer(), game.getCountForComp(),
                    game.getScoreForPlayer(), game.getScoreForComp(), game.getTable());

            game.checker(move.getRow(),move.getColumn(),color,0, game.getTable(), true);


            //нужны персистентные структры данных


            if (color == Unit.Color.Black) {
                color = Unit.Color.White;
            }
            else if (color == Unit.Color.White){
                color = Unit.Color.Black;
            }

            result = algorithm(game,color,deep,alpha,beta);

            //!!!!закоментить!!!!
            int checkpointScoreComp = game.getScoreForComp()+ game.getCountForComp();
            int checkpointScorePlayer = game.getScoreForPlayer()+ game.getCountForPlayer();

            if (checkpointScoreComp>alpha)
                alpha=checkpointScoreComp;

            if (checkpointScorePlayer<beta)
                beta=checkpointScorePlayer;

            if (beta>alpha){
                //завершаем просмотр этой ветки
                break;
            }

            //если достигли глубины
            if (result == Controller.UNMODUNIT && checkpointScoreComp>best)
                best = checkpointScoreComp;
        }
        //возвращаем старое положение до хода
        game.setScore(gamePreTurn.getScore());
        game.setCountForPlayer(gamePreTurn.getCountForPlayer());
        game.setCountForComp(gamePreTurn.getCountForPlayer());
        game.setScoreForPlayer(gamePreTurn.getScoreForPlayer());
        game.setScoreForComp(best);
        game.setTable(gamePreTurn.getTable());
        return result;
    }
}