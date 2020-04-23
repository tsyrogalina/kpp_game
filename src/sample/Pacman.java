package sample;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class Pacman {

    private Rect srcR;              //место расположеняи пакмана на спрайте
    private Rect destR;             //место расположения пакмана на карте
    private double scale;
    private final int startX = 14;
    private final int startY = 11;

    private DIR direction = DIR.STOP;
    private DIR newDirection = DIR.STOP;

    private double speed;
    private double width;

    private int frame = 0;
    private final int delay = 5;
    private int pos = 0;

    public void init(double scale,double width) {
        frame =0;
        direction = DIR.STOP;
        newDirection = DIR.STOP;
        this.width=width;
        this.scale = scale;
        srcR = new Rect(82.5, 2.5, 15, 15);
        destR = new Rect(startX*scale+0.5, startY*scale+0.5, scale-1, scale-1);

        speed = scale/10;

    }

    public void update(Scene scene){

        frame++;
        if(frame%10==0){
            direction = newDirection;
            frame = 0;
        }

       /* if (destR.x == -(scale)+0.5){
            destR.x = (scale * width) + 0.5;

        }

        else if(destR.x == scale * (width)+0.5)
        {
            destR.x= -scale + 0.5;
        }*/
        if (destR.x == -(scale)+0.5-speed){
            destR.x = (scale * width) + 0.5;
            frame--;

        }

        else if(destR.x == scale * (width)+speed+0.5)
        {
            destR.x= -scale + 0.5;
            frame--;
        }


        switch (direction) {
            case UP:
                destR.y -= speed;
                break;
            case DOWN:
                destR.y += speed;
                break;
            case LEFT:
                destR.x -= speed;
                break;
            case RIGHT:
                destR.x += speed;
                break;
        }

        keyBoardController(scene);
        animtion();

    }



    private void keyBoardController(Scene scene) {

        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.UP) {
                newDirection = DIR.UP;
            }
            if (key.getCode() == KeyCode.DOWN) {
                newDirection = DIR.DOWN;
            }
            if (key.getCode() == KeyCode.RIGHT) {
                newDirection = DIR.RIGHT;
            }
            if (key.getCode() == KeyCode.LEFT) {
                newDirection = DIR.LEFT;
            }
            if (key.getCode() == KeyCode.W) {
                newDirection = DIR.UP;
            }
            if (key.getCode() == KeyCode.S) {
                newDirection = DIR.DOWN;
            }
            if (key.getCode() == KeyCode.D) {
                newDirection = DIR.RIGHT;
            }
            if (key.getCode() == KeyCode.A) {
                newDirection = DIR.LEFT;
            }
        });
    }



    private void animtion(){



        if (frame % delay == 0){
            pos++;
        }

        switch (direction){
            case LEFT:      srcR.y = 2.5;   break;
            case RIGHT:     srcR.y = 22.5;  break;
            case UP:        srcR.y = 42.5;  break;
            case DOWN:      srcR.y = 62.5;  break;
        }

        if (pos % 3 == 0) { srcR.x = 82.5; }
        if (pos % 3 == 1) { srcR.x = 102.5; }
        if (pos % 3 == 2) { srcR.x = 122.5; }


    }

    public void stopRun() {
        switch (direction) {
            case UP:
                destR.y += speed;
                break;
            case DOWN:
                destR.y -= speed;
                break;
            case LEFT:
                destR.x += speed;
                break;
            case RIGHT:
                destR.x -= speed;
                break;
        }

        direction = DIR.STOP;                                 //и сотановим персонажа
    }

    public Rect getDestR(){ return destR;}

    public DIR getDirection() {
        return direction;
    }
    public void restart(){

        frame = 0;
        newDirection = DIR.STOP;
        direction = DIR.STOP;
        destR = new Rect(startX*scale+0.5, startY*scale+0.5, scale-1, scale-1);

    }

    public Rect getSrcR() { return srcR; }
    public int  getFrame(){return frame;}
}
