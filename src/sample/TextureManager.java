package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TextureManager {

    public static Image loadTexture(String path) {
        try {

            FileInputStream inputStream = new FileInputStream(path);
            return new Image(inputStream);

        } catch (FileNotFoundException e) {
            System.out.println("Error LoadTexture in TextureManager");
        }
        return null;
    }

    public static void drawTexture(GraphicsContext gc, Image img, Rect srcR, Rect destR) {
        gc.drawImage(img, srcR.x, srcR.y, srcR.w, srcR.h, destR.x, destR.y, destR.w, destR.h);
    }
}
