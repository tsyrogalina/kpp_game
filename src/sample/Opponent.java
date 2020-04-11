package sample;




public interface Opponent {

     final int delay = 5;
     public void init(Map map,double scale,double height,double wight);
     public void update(Rect pacmanR,DIR pacmanDIR,int score);
     public void animation();
     public void typicalBehavior( );

     public void setMode(Rect pacmanR,DIR pacmanDIR,int score);

     public Rect setSrcRect();
     public Rect setDestRect();

}
