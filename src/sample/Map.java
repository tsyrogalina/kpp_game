package sample;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Map {


    private double SCALE;
    private double WIDTH;
    private double HEIGHT;
    private Vector<Rect> bonusArray;
    private List<Rect> borderArray;
    private Vector<Rect> superBonusArray;
    private Vector<Rect> superBonusArraySource;
    private Image image;

    private final int[][] map =
                    {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                    {1,3,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,3,1},
                    {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
                    {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
                    {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,0,1},
                    {1,0,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,0,1},
                    {1,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1},
                    {1,1,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,1,1},
                    {1,1,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,1,1},
                    {1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1},
                    {1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1},
                    {1,1,1,1,1,1,0,1,1,0,1,1,2,2,2,2,1,1,0,1,1,0,1,1,1,1,1,1},
                    {0,0,0,0,0,0,0,0,0,0,1,1,2,2,2,2,1,1,0,0,0,0,0,0,0,0,0,0},
                    {1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1},
                    {1,0,0,0,0,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,0,0,0,0,1},
                    {1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1},
                    {1,0,0,0,0,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,0,0,0,0,1},
                    {1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
                    {1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
                    {1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1},
                    {1,1,1,0,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1},
                    {1,1,1,0,1,1,0,1,1,0,1,1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1},
                    {1,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1},
                    {1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1},
                    {1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1},
                    {1,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,1},
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}


            };

    public void init( final double SCALE) {
        image = TextureManager.loadTexture("sprite\\spritesheet.png");
        this.SCALE = SCALE;
        this.WIDTH = map[0].length;
        this.HEIGHT = map.length;

        borderArray = new ArrayList<>();
        bonusArray = new Vector<Rect>();
        superBonusArray = new Vector<Rect>();
        superBonusArraySource = new Vector<Rect>();
        fillArray();
    }


    public void fillArray()
    {

        double offset= SCALE/3;
        double srcY = 160.5;
        for(int i = 0, end = map.length; i < end; i++) {
            for(int j = 0, endL = map[i].length; j < endL; j++) {
                if(map[i][j] == 0) {
                  bonusArray.add(new Rect(j*SCALE + offset,i*SCALE+offset,offset,offset)) ;

                }
                if(map[i][j]==1)
                {
                    borderArray.add(new Rect(j*SCALE,i*SCALE,SCALE,SCALE));

                }
                if(map[i][j]==3)
                {
                    superBonusArray.add(new Rect(j*SCALE+0.5,i*SCALE+0.5,SCALE-1,SCALE-1));

                    superBonusArraySource.add(new Rect(167.5,srcY,20,20));
                    srcY+=20;
                }


            }

        }


    }

    public Vector<Rect> getSuperBonusArraySource() {
        return superBonusArraySource;
    }

    public void draw(GraphicsContext gc){



        for (int i = 0; i<bonusArray.size();i++)
        {
            gc.setFill(Color.GOLD);
            gc.fillOval(bonusArray.get(i).x,bonusArray.get(i).y, bonusArray.get(i).h, bonusArray.get(i).w);
        }
        for(int i = 0; i<superBonusArray.size();i++)
        {
            TextureManager.drawTexture(gc, image, superBonusArraySource.get(i), superBonusArray.get(i));
        }

       for(Rect border : borderArray)
        {
            gc.setFill(Color.GREEN);
            gc.fillRect(border.x, border.y, border.h, border.w);

            gc.setFill(Color.RED);
            gc.fillRect(border.x + 1, border.y + 1, border.h - 2, border.w - 2);
        }



    }

    public List<Rect> getBorderArray() {
        return borderArray;
    }

    public void restart(){
        double offset= SCALE/3;
        bonusArray.clear();
        superBonusArray.clear();
        superBonusArraySource.clear();
        double srcY = 160.5;
        for(int i = 0, end = map.length; i < end; i++) {
            for(int j = 0, endL = map[i].length; j < endL; j++) {
                if(map[i][j] == 0) {
                    bonusArray.add(new Rect(j*SCALE + offset,i*SCALE+offset,offset,offset)) ;

                }
                if(map[i][j]==3)
                {
                    superBonusArray.add(new Rect(j*SCALE+0.5,i*SCALE+0.5,SCALE-1,SCALE-1));

                    superBonusArraySource.add(new Rect(167.5,srcY,20,20));
                    srcY+=20;
                }


            }

        }


    }

     public int getWidth(){
        return map[0].length;
     }
     public int getHeigth()
     {
         return map.length;
     }
     public Vector<Rect> getSuperBonusArray()
     {
         return superBonusArray;
     }

    public Vector<Rect> getBonusArray() {
        return bonusArray;
    }
}