package model;
import java.util.ArrayList;

public class AI {
    private Unit[][] matrixTableAI = MatrixForGame.matrixTable;
    Game game = new Game();
    Unit result;
    private int bestForComp = -1;
    private int bestForPlayer = 100;


    private ArrayList<Unit> findAllMoves(Unit[][] currentTable, Unit.Color color){
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
                if (game.checker(i,j,color,0,currentTable,false)){
                    allMoves.add(currentTable[i][j]);
                }

        game.setScore(tempScore);
        game.setScoreForComp(tempScoreForComp);
        game.setScoreForPlayer(tempScoreForPlayer);
        game.setCountForComp(tempCountForComp);
        game.setCountForPlayer(tempCountForPlayer);

        return allMoves;
    }

    /*public Unit algorithm(Unit[][] table, Unit.Color color, int deep){
        if (deep==4)
            if (color== Unit.Color.Black) {
                return new Unit(color, game.getScoreForComp(), -1, -1);
            }else if (color== Unit.Color.White)
                return new Unit(color, game.getScoreForPlayer(), -1, -1);

        ArrayList<Unit> moves = findAllMoves(table,color);
        deep++;
        //
        for (Unit move:moves){
            //добавляем фигуру на поле, счет позволяем изменять
            game.checker(move.getRow(),move.getColumn(),color,0,table,true);
            //персистентные струуктры данных
            if (color== Unit.Color.Black) {
                color = Unit.Color.White;
            }
            else if (color== Unit.Color.White){
                color = Unit.Color.Black;
            }
            result = algorithm(table,color,deep);

        }

    }
     */
}
