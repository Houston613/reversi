package controller;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import model.AI;
import model.Game;
import model.MatrixForGame;
import model.Unit;

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

    Game game = new Game(SCORE, COUNT_FOR_PLAYER, COUNT_FOR_COMP, SCORE_FOR_PLAYER, SCORE_FOR_COMP,matrixTable);
    AI test = new AI();

    private void start(){
        MatrixForGame.fill();
        Pane.add(new Circle(45.0, BLACK),4,3);
        Pane.add(new Circle(45.0, BLACK),3,4);
        Pane.add(new Circle(45.0, GRAY),3,3);
        Pane.add(new Circle(45.0, GRAY),4,4);
        matrixTable[4][3].setColor(Unit.Color.Black);
        matrixTable[3][4].setColor(Unit.Color.Black);
        matrixTable[3][3].setColor(Unit.Color.White);
        matrixTable[4][4].setColor(Unit.Color.White);
    }

    private void setScore(){
        Score.setText(String.valueOf(game.getScore()));
        ScoreForComp.setText(String.valueOf(game.getCountForComp()));
        ScoreForPlayer.setText(String.valueOf(game.getCountForPlayer()));
    }


    public void addUnit(int row, int column, boolean turn){
        int t = 0;
        if (turn) {
            if (game.checker(row, column, Unit.Color.White, t, matrixTable,true)) {
                Pane.add(new Circle(45.0, GRAY), column, row);
                while (!listOfRow.isEmpty() && !listOfColumn.isEmpty()){
                    Pane.add(new Circle(45.0, GRAY), listOfColumn.get(0), listOfRow.get(0));
                    listOfRow.remove(0);
                    listOfColumn.remove(0);
                }

            }
        }else
            if (game.checker(row, column, Unit.Color.Black, t, matrixTable,true)) {
                Pane.add(new Circle(45.0, BLACK),column,row);
                matrixTable[row][column].setColor(Unit.Color.Black);
                while (!listOfRow.isEmpty() && !listOfColumn.isEmpty()){
                    Pane.add(new Circle(45.0, BLACK), listOfColumn.get(0), listOfRow.get(0));
                    listOfRow.remove(0);
                    listOfColumn.remove(0);
                }
            }
            setScore();

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

    public static final Unit UNMODUNIT = new Unit(Unit.Color.Transparent,0,-1,-1);

    private Unit mouseHandler(Unit mid, double xCoord, double yCoord,int tempRow, int tempColumn) {
        //если вышли за границы
        if (mid.getRow() < 0 || mid.getColumn() < 0
                || mid.getRow() > 7  || mid.getColumn() > 7) {
            mid = UNMODUNIT;

        } else {
            Bounds bounds = Pane.getCellBounds(mid.getColumn(), mid.getRow());

            //если попали в границы
            if (xCoord > bounds.getMinX() && xCoord < bounds.getMaxX() &&
                    yCoord > bounds.getMinY() && yCoord < bounds.getMaxY()) {
                return mid;

                //левый верхний угол
            } else if (xCoord < bounds.getMinX() && yCoord < bounds.getMinY()) {
                if (tempColumn==0 && tempRow==0) {
                    tempRow++;
                    tempColumn++;
                }
                mid.setRow(mid.getRow() - tempRow);
                mid.setColumn(mid.getColumn() - tempRow);
                mouseHandler(mid,xCoord,yCoord,tempRow-1,tempColumn-1);
            }
            //правый верхний угол
            else if (xCoord > bounds.getMinX() && yCoord < bounds.getMinY()) {
                //если у нас остались 4 клетки, то двигаться нужно иначе, что мы и задаем в if
                //теперь нужно просто подняться на одну клетку вверх чтобы достичь правого верхнего угла
                if (tempRow == 0)
                    tempRow = 1;
                mid.setRow(mid.getRow() - tempRow);
                mid.setColumn(mid.getColumn() + tempColumn);
                mouseHandler(mid, xCoord, yCoord, tempRow - 1,tempColumn-1);

            }
            //левый нижний угол
            else if (xCoord < bounds.getMinX() && yCoord > bounds.getMinY()) {
                //если у нас остались 4 клетки, то двигаться нужно иначе, что мы и задаем в if
                //теперь нужно просто сдвинуться на одну клетку влево чтобы достичь левого нижнего угла
                if (tempColumn == 0)
                    tempColumn = 1;
                mid.setRow(mid.getRow() + tempRow);
                mid.setColumn(mid.getColumn() - tempColumn);
                mouseHandler(mid,xCoord,yCoord,tempRow-1,tempColumn-1);
            }
            //правый нижний угол
            else if (xCoord > bounds.getMinX() && yCoord > bounds.getMinY()) {
                //если у нас остались 4 клетки, то двигаться нужно иначе, что мы и задаем в if
                //теперь нужно ничего не делать, уже должны выйти
                mid.setRow(mid.getRow() + tempRow);
                mid.setColumn(mid.getColumn() + tempColumn);
                mouseHandler(mid,xCoord,yCoord,tempRow-1,tempColumn-1);
            }
        }
        return mid;
    }


    @FXML
    void initialize() {
        start();
        Pane.setOnMouseClicked(event -> {
            Unit result;
            Unit mid = new Unit(matrixTable[4][4].getColor(),matrixTable[4][4].getScore(),4,4);
            double xCoord = event.getSceneX();
            double yCoord = event.getSceneY();
            result = mouseHandler(mid,xCoord,yCoord,2,2);
            System.out.println(result.getRow());
            System.out.println(result.getColumn());
        });
        


    }
}
