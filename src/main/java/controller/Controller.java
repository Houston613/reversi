package controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import model.AI;
import model.Game;
import model.MatrixForGame;
import model.Unit;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.*;
import static model.Game.listOfColumn;
import static model.Game.listOfRow;
import static model.MatrixForGame.matrixTable;

public class Controller {
    //начальные данные
    public static final int SCORE = 60;
    public static final int COUNT_FOR_PLAYER = 2;
    public static final int COUNT_FOR_COMP = 2;
    public static final int SCORE_FOR_PLAYER = 2;
    public static final int SCORE_FOR_COMP = 2;
    //true - ход белых. false - черные
    private boolean turn = true;
    Game game = new Game(SCORE, COUNT_FOR_PLAYER, COUNT_FOR_COMP, SCORE_FOR_PLAYER, SCORE_FOR_COMP, matrixTable);
    AI test = new AI();

    private void changeTurn() {
        turn = !turn;
    }

    private Ellipse createFigure(Color color){

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                System.out.println("Height: " + Pane.getHeight() + " Width: " + Pane.getWidth());
        Pane.widthProperty().addListener(stageSizeListener);
        Pane.heightProperty().addListener(stageSizeListener);

        Ellipse result = new Ellipse();
        result.setFill(color);
        if (color==GOLDENROD){
            result.radiusXProperty().bind(Pane.widthProperty().divide(32));
            result.radiusYProperty().bind(Pane.heightProperty().divide(32));
        }else {
            result.radiusXProperty().bind(Pane.widthProperty().divide(16).subtract(4));
            result.radiusYProperty().bind(Pane.heightProperty().divide(16).subtract(4));
        }
        return result;
    }
    private void start() {
        //заполняем первоначальную таблицу пустыми клетками
        MatrixForGame.fill();
        //рисуем начальную позицию
        Pane.add(createFigure(BLACK), 4, 3);
        Pane.add(createFigure(BLACK), 3, 4);
        Pane.add(createFigure(IVORY), 3, 3);
        Pane.add(createFigure(IVORY), 4, 4);
        //меняем цвет у первоначльных клеток
        game.getTable(false)[4][3].setColor(Unit.Color.Black);
        game.getTable(false)[3][4].setColor(Unit.Color.Black);
        game.getTable(false)[3][3].setColor(Unit.Color.White);
        game.getTable(false)[4][4].setColor(Unit.Color.White);
        Score.setText(String.valueOf(SCORE));
        ScoreForComp.setText(String.valueOf(SCORE_FOR_COMP));
        ScoreForPlayer.setText(String.valueOf(SCORE_FOR_PLAYER));
    }

    private void restart(){
        Pane.setGridLinesVisible(false);
        Pane.getChildren().clear();
        Pane.setGridLinesVisible(true);
        game = new Game(SCORE, COUNT_FOR_PLAYER, COUNT_FOR_COMP, SCORE_FOR_PLAYER, SCORE_FOR_COMP, matrixTable);
        turn = true;
        test = new AI();
        start();
        setScore();

    }

    private void setScore() {
        //выводит счет на экран
        Score.setText(String.valueOf(game.getScore()));
        ScoreForComp.setText(String.valueOf(game.getCountForComp()));
        ScoreForPlayer.setText(String.valueOf(game.getCountForPlayer()));
    }

    public void addUnit(int column, int row, boolean turn) {
        int t = 0;
        if (turn) {
            if (game.checker(column, row, Unit.Color.White, t, game.getTable(false), true)) {
                Pane.add(createFigure(IVORY), column, row);
                game.getTable(false)[column][row].setColor(Unit.Color.White);
                //checker сам меняет цвет вражеских фишек,
                // поэтому нам нужно пройтись по списку который заполнеяет changer и нарисовать нужные unit'ы
                while (!listOfRow.isEmpty() && !listOfColumn.isEmpty()) {
                    Pane.add(createFigure(IVORY), listOfColumn.get(0), listOfRow.get(0));
                    //удаляем первый элемент,тк его уже нарисовали
                    listOfRow.remove(0);
                    listOfColumn.remove(0);
                }

            }
        } else if (game.checker(column, row, Unit.Color.Black, t, game.getTable(false), true)) {
            Pane.add(createFigure(BLACK), column, row);
            game.getTable(false)[column][row].setColor(Unit.Color.Black);
            while (!listOfRow.isEmpty() && !listOfColumn.isEmpty()) {
                Pane.add(createFigure(BLACK), listOfColumn.get(0), listOfRow.get(0));
                listOfRow.remove(0);
                listOfColumn.remove(0);
            }
        }
        //показываем счет на экран
        setScore();
        //меняем ход
        changeTurn();

    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label ScoreForPlayer;

    @FXML
    private Label ScoreForComp;

    @FXML
    private Label Score;

    @FXML
    private GridPane Pane;



    public static final Unit UNMODUNIT = new Unit(Unit.Color.Transparent, 0, -1, -1);

    private Unit mouseHandler(Unit mid, double xCoord, double yCoord, int tempRow, int tempColumn) {
        //если вышли за границы
        if (mid.getRow() < 0 || mid.getColumn() < 0
                || mid.getRow() > 7 || mid.getColumn() > 7) {
            mid = UNMODUNIT;

        } else {
            Bounds bounds = Pane.getCellBounds(mid.getColumn(), mid.getRow());

            //если попали в границы
            if (xCoord > bounds.getMinX() && xCoord < bounds.getMaxX() &&
                    yCoord > bounds.getMinY() && yCoord < bounds.getMaxY()) {
                return mid;

                //левый верхний угол
            } else if (xCoord < bounds.getMinX() && yCoord < bounds.getMinY()) {
                if (tempColumn == 0 && tempRow == 0) {
                    tempRow++;
                    tempColumn++;
                }
                mid.setRow(mid.getRow() - tempRow);
                mid.setColumn(mid.getColumn() - tempRow);
                mouseHandler(mid, xCoord, yCoord, tempRow - 1, tempColumn - 1);
            }
            //правый верхний угол
            else if (xCoord > bounds.getMinX() && yCoord < bounds.getMinY()) {
                //если у нас остались 4 клетки, то двигаться нужно иначе, что мы и задаем в if
                //теперь нужно просто подняться на одну клетку вверх чтобы достичь правого верхнего угла
                if (tempRow == 0)
                    tempRow = 1;
                mid.setRow(mid.getRow() - tempRow);
                mid.setColumn(mid.getColumn() + tempColumn);
                mouseHandler(mid, xCoord, yCoord, tempRow - 1, tempColumn - 1);

            }
            //левый нижний угол
            else if (xCoord < bounds.getMinX() && yCoord > bounds.getMinY()) {
                //если у нас остались 4 клетки, то двигаться нужно иначе, что мы и задаем в if
                //теперь нужно просто сдвинуться на одну клетку влево чтобы достичь левого нижнего угла
                if (tempColumn == 0)
                    tempColumn = 1;
                mid.setRow(mid.getRow() + tempRow);
                mid.setColumn(mid.getColumn() - tempColumn);
                mouseHandler(mid, xCoord, yCoord, tempRow - 1, tempColumn - 1);
            }
            //правый нижний угол
            else if (xCoord > bounds.getMinX() && yCoord > bounds.getMinY()) {
                //если у нас остались 4 клетки, то двигаться нужно иначе, что мы и задаем в if
                //теперь нужно ничего не делать, уже должны выйти
                mid.setRow(mid.getRow() + tempRow);
                mid.setColumn(mid.getColumn() + tempColumn);
                mouseHandler(mid, xCoord, yCoord, tempRow - 1, tempColumn - 1);
            }
        }
        return mid;
    }

    private void illuminate() {
        ArrayList<Unit> turnsForPlayer = test.findAllMoves(game, Unit.Color.White);
        for (Unit i : turnsForPlayer) {
            Pane.add(createFigure(GOLDENROD), i.getColumn(), i.getRow());
        }

    }

    private void blur() {
        ArrayList<Unit> turnsForPlayer = test.findAllMoves(game, Unit.Color.White);
        for (Unit i : turnsForPlayer) {
            Pane.add(createFigure(WHITESMOKE), i.getColumn(), i.getRow());
        }
    }

    private boolean checkValidClick(int column, int row) {
        ArrayList<Unit> turnsForPlayer = test.findAllMoves(game, Unit.Color.White);
        for (Unit i : turnsForPlayer) {
            if (i.getColumn() == column && i.getRow() == row)
                return true;
        }
            return false;
    }


    @FXML
    void initialize() {
        GridPane restartPane = Pane;
        start();
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                System.out.println("Height: " + Pane.getHeight() + " Width: " + Pane.getWidth());
        Pane.widthProperty().addListener(stageSizeListener);
        Pane.heightProperty().addListener(stageSizeListener);

        illuminate();

        Pane.setOnMouseClicked(event -> {
            Unit result;
            Unit mid = new Unit(game.getTable(false)[4][4].getColor(), game.getTable(false)[4][4].getScore(), 4, 4);
            double xCoord = event.getSceneX();
            double yCoord = event.getSceneY();
            result = mouseHandler(mid, xCoord, yCoord, 2, 2);
            if (checkValidClick(result.getColumn(),result.getRow())) {
                blur();
                addUnit(result.getColumn(), result.getRow(), turn);
                result = test.algorithm(game, Unit.Color.Black, 0, -1, 100);
                if (result != null) {
                    addUnit(result.getColumn(), result.getRow(), turn);
                    test.setToDefault();
                    illuminate();
                }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game is End");
                    if (game.getCountForPlayer()>=game.getCountForComp())
                        alert.setHeaderText("You win");
                    else
                        alert.setHeaderText("You lose");
                    alert.setContentText("Your Score:"+game.getCountForPlayer()+"\nComp Score:"+game.getCountForComp());
                    alert.showAndWait();
                    restart();
                    illuminate();
                }
            }
        });


    }
}
