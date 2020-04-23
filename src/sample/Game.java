package sample;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;



import java.util.ArrayList;
import java.util.List;

public class Game {

    private  double SCALE = 0;
    private int WIDTH = 0;
    private int HEIGHT = 0;

    private VBox box;
    private Canvas c;
    private GraphicsContext gc;
    private Scene scene;
    private Stage primaryStage;

    private Image image;
    private Main main;
    private Map map;
    private Pacman pacman;
    private PacmanDraw pacmanDraw;
    EventHandler<KeyEvent> handler;

    private int score;
    private List<Opponent> opponents;

    public void init(Stage primaryStage,Main main){
        this.SCALE = main.getScale();
        this.HEIGHT = main.getHEIGHT();
        this.WIDTH =main.getWIDTH();
        this.primaryStage = primaryStage;
        this.main = main;
        box = new VBox();
        c = new Canvas(WIDTH * SCALE, HEIGHT * SCALE);
        box.getChildren().add(c);
        gc = c.getGraphicsContext2D();
        scene = new Scene(box, WIDTH * SCALE, HEIGHT * SCALE);

        image = TextureManager.loadTexture("sprite\\spritesheet.png");

        map = new Map();
        map.init(SCALE);

        opponents = new ArrayList<>();
        opponents.add(new RedOpponent());
       opponents.add(new PinkOpponent());
       opponents.add(new OrangeOpponent());
       opponents.add(new BlueOpponent());

         handler = new EventHandler<>(){
            public void handle(KeyEvent event){
                main.showMenu();
            }
        };

        for (Opponent o : opponents){
            o.init(map,SCALE,HEIGHT, WIDTH);
        }

        pacman = new Pacman();
        pacmanDraw = new PacmanDraw();
        pacman.init(SCALE,WIDTH);
        pacmanDraw.init(image);

        score = 0;

        primaryStage.setScene(scene);
        primaryStage.setTitle("PacMan");
        primaryStage.show();
    }

    public void restart(){
        score = 0;
        for (Opponent o : opponents){
            o.restart();
        }
        scene.removeEventFilter(KeyEvent.KEY_PRESSED, handler);
        map.restart();
        pacman.restart();

    }

    public void update()  {

        Rect pacmanR = pacman.getDestR();             //получаем текущее положение pacman

        pacman.update(scene);

        for (Opponent o : opponents){

           if(!( o.update(pacmanR,pacman.getDirection(),score))){
              //main.stopGame(); //game  over
               main.stopGame();

               gc.clearRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);             //очищаем  игровое окно

               gc.setFill(Color.DARKBLUE);
               gc.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
               gc.setFill(Color.WHITE);                                   //цвет счетчика
               gc.setFont(new Font("Arial", SCALE));                                 //уставнавливаем счетчик
               gc.fillText("You lost!\nPress any key" , SCALE*WIDTH/3,SCALE*HEIGHT/2,SCALE*WIDTH/3);
               scene.addEventFilter(KeyEvent.KEY_PRESSED, handler);

                //game  over
           }
        }

        for (Rect r : map.getBorderArray()) {
            if (Collision.AABB(pacmanR, r)) {
                pacman.stopRun();                    //если забор то останавливаем персонажа
                break;                               //проверяем все заборики
            }
        }

        for (Rect r : map.getBonusArray()) {
            if (Collision.AABB(pacmanR, r)&&pacman.getFrame()%10==0) {          //проверяем если еда
                map.getBonusArray().remove(r);              //то удаляем еду с экрана
                score++;
                if(map.getBonusArray().isEmpty())
                {
                    main.stopGame();
                    gc.clearRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);             //очищаем  игровое окно

                    gc.setFill(Color.DARKBLUE);
                    gc.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
                    gc.setFill(Color.WHITE);                                   //цвет счетчика
                    gc.setFont(new Font("Arial", SCALE));                                 //уставнавливаем счетчик
                    gc.fillText("You win!\nPress any key" , SCALE*WIDTH/3,SCALE*HEIGHT/2,SCALE*WIDTH/3);
                    scene.addEventFilter(KeyEvent.KEY_PRESSED, handler);

                }
                break;
            }
        }
    }

    public void setStartScrene(){

        primaryStage.setScene(scene);
        primaryStage.setTitle("PacMan");
        primaryStage.show();
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
