package model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer {
    //класс watcher, рисующий таблицу игрового поля
    public BufferedImage render(Unit[][] table){
        BufferedImage img =  new BufferedImage(8,8,BufferedImage.TYPE_INT_RGB);
        Graphics graphics = img.getGraphics();
        graphics.setColor(Color.GRAY);
        graphics.fillRect(0,0,8,8);
        for (int i = 0; i<=7; i++){
            for (int j = 0; j<=7; j++){
                if (table[i][j].getColor()== Unit.Color.Black) {
                    img.setRGB(i, j, 0);
                }
                if (table[i][j].getColor()== Unit.Color.White){
                    img.setRGB(i,j,255);
                }
            }
        }
        return img;
    }
}
