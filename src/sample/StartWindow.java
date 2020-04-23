package sample;


import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
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
import javafx.util.Duration;


public class StartWindow{
    private VBox box;
    private Canvas c;
    private GraphicsContext gc;
    private Stage primaryStage;
    private Scene scene;
    private Image image;
    private Image imagePacmanText;
    private Pane root;
    private Game game = null;
    private int height ;
    private int width;
    private double scale;


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
        ImageView img = new ImageView(image);
        ImageView img2 = new ImageView(imagePacmanText);
        img.setFitHeight(270*((width*scale)/480));
        img.setFitWidth(width*scale);
        img2.setX(scale*width/9*2);
        img2.setY(scale*height/2);
        img2.setFitWidth(scale*width*5/9);
        img2.setFitHeight(771*5*scale*width/9/2950);

        root.getChildren().addAll(img,img2);

        root.getChildren().add(box);
        StackPane newGame = addButton("New game");
        StackPane records = addButton("Table of records");
        StackPane rules = addButton("Game rules");
        box.setTranslateX(width*scale/3);
        box.setTranslateY(height*scale/2+scale*5);
        box.getChildren().addAll(newGame, records,rules);
        newGame.setOnMouseClicked(event -> { main.startGame(); });
        scene = new Scene(root,width*scale,height*scale,Color.BLACK);
        primaryStage.setTitle("PacMan");
        primaryStage.setScene(scene);
        primaryStage.show();
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
