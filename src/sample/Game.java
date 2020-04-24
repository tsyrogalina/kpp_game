package sample;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    EventHandler<WindowEvent> handler1;

    boolean flag = false;
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

        flag = true;

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

        handler1 = new EventHandler<>(){
            @Override
            public void handle(WindowEvent event) {
                if (flag){
                main.stopGame();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Dialog confirmation");

                alert.setContentText("Save an unfinished game before exiting?");
                alert.setHeaderText ("Have you decided to quit the game?");

                ButtonType buttonYes = new ButtonType("Yes");
                ButtonType buttonNo = new ButtonType("No");
                ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonYes,buttonNo,buttonCancel);
                Optional<ButtonType> result = alert.showAndWait();





                if (result.get() == buttonYes) {
                    saveGame();
                }
                else if (result.get() == buttonNo) {


                }
                else
                {
                    event.consume();
                    main.continueGame();
                }

            }}
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
        flag = true;
        score = 0;
        for (Opponent o : opponents){
            o.restart();
        }
        scene.removeEventFilter(KeyEvent.KEY_PRESSED, handler);

       // primaryStage.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, handler1);
        map.restart();
        pacman.restart();

    }

    public void update()  {

        primaryStage.setOnCloseRequest(handler1);


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
               exit();
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

    private void exit(){
        primaryStage.removeEventFilter(WindowEvent.ANY, handler1);
        primaryStage.removeEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, handler1);
        flag = false;
    }

    private void saveGame()
    {


        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream("saveGame\\saveGame.bin",true)))
        {
            //PACMAN

            dos.writeInt(pacman.getFrame());
            dos.writeInt(pacman.getPos());

            dos.writeDouble(pacman.getDestR().x);
            dos.writeDouble(pacman.getDestR().y);
            dos.writeDouble(pacman.getDestR().h);
            dos.writeDouble(pacman.getDestR().w);

            dos.writeDouble(pacman.getSrcR().x);
            dos.writeDouble(pacman.getSrcR().y);
            dos.writeDouble(pacman.getSrcR().h);
            dos.writeDouble(pacman.getSrcR().w);

            switch (pacman.getDirection())
            {
                case LEFT: dos.writeInt(0);
                break;
                case RIGHT:dos.writeInt(1);
                break;
                case UP:dos.writeInt(2);
                break;
                case DOWN:dos.writeInt(3);
                break;
                case STOP:dos.writeInt(4);

            }
            switch (pacman.getNewDirection())
            {
                case LEFT: dos.writeInt(0);
                    break;
                case RIGHT:dos.writeInt(1);
                    break;
                case UP:dos.writeInt(2);
                    break;
                case DOWN:dos.writeInt(3);
                    break;
                case STOP:dos.writeInt(4);

            }
            ///Opponents
            for(Opponent o:opponents)
            {
                dos.writeInt(o.getFrame());
                dos.writeInt(o.getPos());

                dos.writeDouble(o.getDestR().x);
                dos.writeDouble(o.getDestR().y);
                dos.writeDouble(o.getDestR().h);
                dos.writeDouble(o.getDestR().w);

                dos.writeDouble(o.getSrcR().x);
                dos.writeDouble(o.getSrcR().y);
                dos.writeDouble(o.getSrcR().h);
                dos.writeDouble(o.getSrcR().w);

                dos.writeDouble(o.getGoalTile().x);
                dos.writeDouble(o.getGoalTile().y);
                dos.writeDouble(o.getGoalTile().h);
                dos.writeDouble(o.getGoalTile().w);

                switch (o.getDirection())
                {
                    case LEFT: dos.writeInt(0);
                        break;
                    case RIGHT:dos.writeInt(1);
                        break;
                    case UP:dos.writeInt(2);
                        break;
                    case DOWN:dos.writeInt(3);
                        break;
                    case STOP:dos.writeInt(4);

                }

                switch(o.getOpponentMode())
                {
                    case CHASE:dos.writeInt(0);
                    break;
                    case SCATTER:dos.writeInt(1);
                    break;
                    case SCARE:dos.writeInt(2);
                }

                dos.writeBoolean(o.getFlagEatBonus());

            }
            //map
            int num = map.getBonusArray().size();
            dos.writeInt(num);
            for (int i = 0; i <num;i++)
            {
                dos.writeDouble(map.getBonusArray().get(i).x);
                dos.writeDouble(map.getBonusArray().get(i).y);
                dos.writeDouble(map.getBonusArray().get(i).h);
                dos.writeDouble(map.getBonusArray().get(i).w);
            }
            num = map.getSuperBonusArray().size();
            dos.writeInt(num);
            for (int i = 0; i <num;i++)
            {
                dos.writeDouble(map.getSuperBonusArray().get(i).x);
                dos.writeDouble(map.getSuperBonusArray().get(i).y);
                dos.writeDouble(map.getSuperBonusArray().get(i).h);
                dos.writeDouble(map.getSuperBonusArray().get(i).w);
            }
            num = map.getSuperBonusArraySource().size();
            dos.writeInt(num);
            for (int i = 0; i <num;i++)
            {
                dos.writeDouble(map.getSuperBonusArraySource().get(i).x);
                dos.writeDouble(map.getSuperBonusArraySource().get(i).y);
                dos.writeDouble(map.getSuperBonusArraySource().get(i).h);
                dos.writeDouble(map.getSuperBonusArraySource().get(i).w);
            }


        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    public void continueGame()
    {
        try(DataInputStream dis = new DataInputStream(new FileInputStream("saveGame\\saveGame.bin")))
        {
            //pacman
            pacman.setFrame(dis.readInt());
            pacman.setPos(dis.readInt());

            pacman.setDestR(new Rect(dis.readDouble(),dis.readDouble(),dis.readDouble(),dis.readDouble()));
            pacman.setSrcR(new Rect(dis.readDouble(),dis.readDouble(),dis.readDouble(),dis.readDouble()));

            switch(dis.readInt())
            {
                case 0: pacman.setDirection(DIR.LEFT); break;
                case 1: pacman.setDirection(DIR.RIGHT); break;
                case 2: pacman.setDirection(DIR.UP); break;
                case 3: pacman.setDirection(DIR.DOWN); break;
                case 4: pacman.setDirection(DIR.STOP); break;
            }
            switch(dis.readInt())
            {
                case 0: pacman.setNewDirection(DIR.LEFT); break;
                case 1: pacman.setNewDirection(DIR.RIGHT); break;
                case 2: pacman.setNewDirection(DIR.UP); break;
                case 3: pacman.setNewDirection(DIR.DOWN); break;
                case 4: pacman.setNewDirection(DIR.STOP); break;
            }

            //opponents
            for(Opponent o:opponents) {
                o.setFrame(dis.readInt());
                o.setPos(dis.readInt());

                o.setDestR(new Rect(dis.readDouble(),dis.readDouble(),dis.readDouble(),dis.readDouble()));
                o.setSrcR(new Rect(dis.readDouble(),dis.readDouble(),dis.readDouble(),dis.readDouble()));
                o.setGoalTile(new Rect(dis.readDouble(),dis.readDouble(),dis.readDouble(),dis.readDouble()));

                switch(dis.readInt())
                {
                    case 0: o.setDirection(DIR.LEFT); break;
                    case 1: o.setDirection(DIR.RIGHT); break;
                    case 2: o.setDirection(DIR.UP); break;
                    case 3: o.setDirection(DIR.DOWN); break;
                    case 4: o.setDirection(DIR.STOP); break;
                }

                switch (dis.readInt())
                {
                    case 0:o.setOpponentMode(OpponentMode.CHASE); break;
                    case 1:o.setOpponentMode(OpponentMode.SCATTER); break;
                    case 2:o.setOpponentMode(OpponentMode.SCARE); break;
                }

               o.setFlagEatBonus(dis.readBoolean());
            }
               ////map
               int num =dis.readInt();
               map.getBonusArray().clear();
               for(int i = 0; i < num;i++)
               {
                   map.getBonusArray().add(new Rect(dis.readDouble(),dis.readDouble(),dis.readDouble(),dis.readDouble()));
               }
                num =dis.readInt();
                map.getSuperBonusArray().clear();
                for(int i = 0; i < num;i++)
                {
                    map.getSuperBonusArray().add(new Rect(dis.readDouble(),dis.readDouble(),dis.readDouble(),dis.readDouble()));
                }
                num =dis.readInt();
                map.getSuperBonusArraySource().clear();
                for(int i = 0; i < num;i++)
                {
                    map.getSuperBonusArraySource().add(new Rect(dis.readDouble(),dis.readDouble(),dis.readDouble(),dis.readDouble()));
                }




        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        try{
            File file = new File("saveGame\\saveGame.bin");
            file.delete();
        }
        catch(Exception e)
        {
            e.printStackTrace();
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
