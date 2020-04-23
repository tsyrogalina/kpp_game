package sample;


import java.util.Vector;

public interface Opponent {

     final int delay = 5;
     public void init(Map map,double scale,int height,int wight);
     public boolean update(Rect pacmanR, DIR pacmanDIR, int score);
     public void animation();
     public void typicalBehavior( );

     public void setMode(Rect pacmanR,DIR pacmanDIR,int score, Vector<Rect> vect);
     public void restart();
     public Rect setSrcRect();
     public Rect setDestRect();

}
