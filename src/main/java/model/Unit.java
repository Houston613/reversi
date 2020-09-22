package model;
//класс для клеток, которые далее заполнят матрицу/игровой стол
public class Unit {

    public Unit(Color color, int score) {
        this.color = color;
        this.score = score;
    }

    public enum Color {
        White, Black, Transparent;
    }

    private int score;
    //кол-во очков которые дает данное поле
    private Color color;
    //цвет поля


    public int getColumn() { return score; }
    public void setColumn(int column) { this.score = column; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }



}
