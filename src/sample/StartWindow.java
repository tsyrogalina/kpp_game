package sample;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartWindow extends Application {
    Image image;
    Pane root;
    @Override
    public void start(Stage primaryStage)
    {
        root =new Pane();
        image = TextureManager.loadTexture("C:\\Users\\Галина\\Desktop\\KPP_Game-master\\sprite\\spritesheet.png");
    }
}
