package model;
//класс для клеток, которые далее заполнят матрицу/игровой стол
public class Unit {

    public Unit(Color color, int score, int column, int row) {
        this.color = color;
        this.score = score;
        this.column = column;
        this.row = row;
    }

    public enum Color {
        White, Black, Transparent;
    }
    //кол-во очков которые дает данное поле
    private int score;
    public int getScore() { return score; }
    public void setScore(int column) { this.score = column; }

    //цвет поля
    private Color color;
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    private int column;
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }

    private int row;
    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }









}
