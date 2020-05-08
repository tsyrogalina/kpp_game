package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private final double SCALE = 30;
    private int WIDTH = 0;
    private int HEIGHT = 0;
    private Stage primaryStage;
    private AnimationTimer at;

    private final int FPS = 60;
    private final int frameDelay = 1000000000 / FPS;
    private Game game = null;
    private StartWindow sw = null;
    @Override
    public void start(Stage primaryStage) throws Exception {

        at = new AnimationTimer() {
            long lastTick = 0;

            public void handle(long now) {

                if (now - lastTick > frameDelay) {
                    lastTick = now;
                    update();
                }
            }

        };

        this.primaryStage = primaryStage;
        Map map  = new Map();
       HEIGHT = map.getHeigth();
       WIDTH = map.getWidth();
        primaryStage.setResizable(false);
        game = new Game();
        game.init(primaryStage,this);

         sw = new StartWindow();
        sw.init(primaryStage, this);




    }

    public Game getGame() {
        return game;
    }

    public int getHEIGHT(){return HEIGHT;}
    public int getWIDTH(){return WIDTH;}
    public double getScale(){return SCALE;}
    public void gameWindow(Scene scene){

    }

    public void update() {
        game.draw();
        game.update();
    }

    public void startGame(){


        game.restart();
        at.start();
        game.setStartScrene();
    }
    public void stopGame(){

        at.stop();
        //sw.setStartScreen();
    }
    public void continueGame()
    {
        at.start();
    }

    public  void showMenu(){
        sw.setStartScreen();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
