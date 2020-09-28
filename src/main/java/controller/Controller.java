package controller;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
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
    Game game = new Game();
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

    @FXML
    void initialize() {
        start();
        


    }
}
