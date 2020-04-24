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
     public Rect getDestR();

     public DIR getDirection();

     public Rect getSrcR() ;

     public int getFrame();

     public int getPos() ;


     public Rect getGoalTile() ;

     public OpponentMode getOpponentMode();
     public boolean getFlagEatBonus();


     public void setDestR(Rect destR);

     public void setDirection(DIR direction) ;
     public void setFlagEatBonus(boolean flagEatBonus) ;

     public void setFrame(int frame) ;

     public void setGoalTile(Rect goalTile) ;

     public void setOpponentMode(OpponentMode opponentMode) ;

     public void setPos(int pos) ;

     public void setSrcR(Rect srcR) ;

}
