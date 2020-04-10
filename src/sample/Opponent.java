package sample;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;


public interface Opponent {
     final int delay = 5;
     public void init(Map map,double scale,double height,double wight, Image image);
     public void draw(GraphicsContext gc);
     public void update(Rect pacmanR,int score);
     public void animation();
     public void typicalBehavior( );
     public void scareBehavior();
     public void setMode(Rect pacmanR,int score);

}
