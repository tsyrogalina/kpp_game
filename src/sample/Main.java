package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private final int FPS = 60;
    private final int frameDelay = 1000000000 / FPS;
    private Game game = null;

    @Override
    public void start(Stage primaryStage) throws Exception {


        game = new Game();
        game.init(primaryStage);


        new AnimationTimer() {
            long lastTick = 0;

            public void handle(long now) {

                if (now - lastTick > frameDelay) {
                    lastTick = now;
                    update();
                }
            }

        }.start();


    }


    public void update() {
        game.draw();
        game.update();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
