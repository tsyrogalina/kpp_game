package sample;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;

public class Game {

    private final double SCALE = 30;
    private final double WIDTH = 28;
    private final double HEIGHT = 31;

    private VBox box;
    private Canvas c;
    private GraphicsContext gc;
    private Scene scene;

    private Image image;

    private Map map;
    private Pacman pacman;
    private PacmanDraw pacmanDraw;

    private int score;
    private List<Opponent> opponents;

    public void init(Stage primaryStage){

        box = new VBox();
        c = new Canvas(WIDTH * SCALE, HEIGHT * SCALE);
        box.getChildren().add(c);
        gc = c.getGraphicsContext2D();
        scene = new Scene(box, WIDTH * SCALE, HEIGHT * SCALE);

        image = TextureManager.loadTexture("C:\\Users\\Галина\\Desktop\\KPP_Game-master\\sprite\\spritesheet.png");

        map = new Map();
        map.init(SCALE, WIDTH, HEIGHT);

        opponents = new ArrayList<>();
        opponents.add(new RedOpponent());
       opponents.add(new PinkOpponent());
       opponents.add(new OrangeOpponent());
       opponents.add(new BlueOpponent());

        for (Opponent o : opponents){
            o.init(map,SCALE,HEIGHT, WIDTH);
        }

        pacman = new Pacman();
        pacmanDraw = new PacmanDraw();
        pacman.init(SCALE,WIDTH);
        pacmanDraw.init(image);

        score = 0;
        primaryStage.setScene(scene);
        primaryStage.setTitle("PACMAN");
        primaryStage.show();

    }

    public void update()  {

        Rect pacmanR = pacman.getDestR();             //получаем текущее положение pacman

        pacman.update(scene);

        for (Opponent o : opponents){

            o.update(pacmanR,pacman.getDirection(),score);
        }

        for (Rect r : map.getBorderArray()) {
            if (Collision.AABB(pacmanR, r)) {
                pacman.stopRun();                    //если забор то останавливаем персонажа
                break;                               //проверяем все заборики
            }
        }

        for (Rect r : map.getBonusArray()) {
            if (Collision.AABB(pacmanR, r)) {          //проверяем если еда
                map.getBonusArray().remove(r);              //то удаляем еду с экрана
                score++;
                if(map.getBonusArray().isEmpty())
                {
                    gc.setFill(Color.WHITE);                                   //цвет счетчика
                    gc.setFont(new Font("", SCALE*5));                                 //уставнавливаем счетчик
                    gc.fillText("You WIN!" , 5, SCALE-5);//you win


                }
                break;
            }
        }
    }

    public void draw(){
        gc.clearRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);             //очищаем  игровое окно

        gc.setFill(Color.DARKBLUE);
        gc.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);             //отрисовываем синий экран

        map.draw(gc);
        pacmanDraw.draw(gc, pacman.getSrcR(), pacman.getDestR());

        for (Opponent o : opponents){
            pacmanDraw.draw(gc, o.setSrcRect(), o.setDestRect());
        }

        gc.setFill(Color.WHITE);                                   //цвет счетчика
        gc.setFont(new Font("", SCALE));                                 //уставнавливаем счетчик
        gc.fillText("Score: " + score, 5, SCALE-5);
    }
}
