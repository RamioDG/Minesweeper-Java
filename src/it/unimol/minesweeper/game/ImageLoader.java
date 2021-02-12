package it.unimol.minesweeper.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

public class ImageLoader {
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(ImageLoader.class.getClassLoader().getResourceAsStream(path)));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
