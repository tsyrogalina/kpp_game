package sample;


import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.File;
import java.util.Optional;


public class StartWindow{
    private VBox box;
    private Canvas c;
    private GraphicsContext gc;
    private Stage primaryStage;
    private Scene scene;
    private Image image;
    private Image imagePacmanText;
    private Image imageBg;
    private Pane root;
    private Game game = null;
    private int height ;
    private int width;
    private double scale;
    private DifficultyLevel lvl=DifficultyLevel.EASY;


    public void init(Stage primaryStage,Main main)
    {
        this.primaryStage = primaryStage;
        scale = main.getScale();
        width = main.getWIDTH();
        height = main.getHEIGHT();
        root = new Pane();
        box = new VBox(scale);



        image = TextureManager.loadTexture("sprite\\gameOver.gif");
        imagePacmanText = TextureManager.loadTexture("sprite\\pacmanText.png");
        imageBg = TextureManager.loadTexture("sprite\\bg.png");
        ImageView img = new ImageView(image);
        ImageView img2 = new ImageView(imagePacmanText);
        ImageView img3 = new ImageView(imageBg);
        img.setFitHeight(270*((width*scale)/480));
        img.setFitWidth(width*scale);
        img2.setX(scale*width/9*2);
        img2.setY(scale*height/2);
        img2.setFitWidth(scale*width*5/9);
        img2.setFitHeight(771*5*scale*width/9/2950);

        img3.setFitHeight(scale*height);
        img3.setFitWidth(scale*width);

        scene = new Scene(root,width*scale,height*scale);
        scene.setFill(Color.BLACK);

        root.getChildren().addAll(img3,img,img2);

        root.getChildren().add(box);
        StackPane newGame = addButton("New game");

        box.setTranslateX(width*scale/3);
        box.setTranslateY(height*scale/2+scale*5);
        box.getChildren().addAll(newGame);
        newGame.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Dialog confirmation");

            //alert.setContentText("Wnat to continue? If you choose NO, you will lose your saved data.");
            alert.setHeaderText ("Choose level.");

            ButtonType buttonEasy = new ButtonType("Easy");
            ButtonType buttonMedium = new ButtonType("Medium");
            ButtonType buttonDifficult = new ButtonType("Difficult");


            alert.getButtonTypes().setAll(buttonEasy,buttonMedium,buttonDifficult);
            Optional<ButtonType> result = alert.showAndWait();




            if (result.get() == buttonDifficult) {
                lvl = DifficultyLevel.DIFFICULT;

            }
            else if (result.get() == buttonMedium) {
                lvl = DifficultyLevel.MEDIUM;
            }
            else {
                lvl = DifficultyLevel.EASY;
            }
            main.getGame().setLvl(lvl);
            main.startGame(); });




        primaryStage.setTitle("PacMan");
        primaryStage.setScene(scene);
        primaryStage.show();

        if(new File("saveGame\\saveGame.bin").isFile())
        {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Dialog confirmation");

                alert.setContentText("Wnat to continue? If you choose NO, you will lose your saved data.");
                alert.setHeaderText ("You have an unfinished game.");

                ButtonType buttonYes = new ButtonType("Yes");
                ButtonType buttonNo = new ButtonType("No",ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonYes,buttonNo);
                Optional<ButtonType> result = alert.showAndWait();



                if (result.get() == buttonYes) {
                    main.startGame();

                    main.getGame().continueGame();

                }
                else if (result.get() == buttonNo) {
                    try{
                        File file = new File("saveGame\\saveGame.bin");
                        file.delete();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }



            }


    }

    public void setStartScreen()
    {


        primaryStage.setTitle("PacMan");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StackPane addButton(String str)
    {
        StackPane sp = new StackPane();
        Rectangle bg = new Rectangle(width*scale/3,scale*2,Color.GOLDENROD);
        Text text = new Text(str);
        text.setFill(Color.BLACK);
        text.setFont(Font.font("Arial", FontWeight.NORMAL,scale));

        sp.setAlignment(Pos.CENTER);
        sp.getChildren().addAll(bg,text);
        FillTransition  st = new FillTransition(Duration.seconds(0.5),bg);
        sp.setOnMouseEntered(event -> {
            st.setFromValue(Color.DARKGOLDENROD);
            st.setToValue(Color.LIGHTSALMON);
            //st.setCycleCount(Animation.INDEFINITE);
            //st.setAutoReverse(true);
            st.play();
        });
        sp.setOnMouseExited(event ->{
            st.stop();
            bg.setFill(Color.GOLDENROD);
        });
        return sp;
    }
}
