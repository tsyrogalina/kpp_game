package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PacmanDraw {

    private Image image;

    void init(Image image){
        this.image = image;
    }

    public void draw(GraphicsContext gc, Rect srcR, Rect destR){
        TextureManager.drawTexture(gc, image, srcR, destR);
    }
}
