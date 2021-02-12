package it.unimol.minesweeper.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Tile implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient BufferedImage normal;
    private transient BufferedImage openedImage;
    private transient BufferedImage flagImage;
    private transient BufferedImage bombImage;

    private int x;
    private int y;
    private boolean bomb;
    private boolean opened;
    private boolean flag;
    private int amountOfNearBombs;

    private static final int width = 16;
    private static final int height = 16;

    public Tile(int x, int y,
                BufferedImage normal,
                BufferedImage bomb,
                BufferedImage openedImage,
                BufferedImage flag) {
        this.x = x;
        this.y = y;
        this.normal = normal;
        this.bombImage = bomb;
        this.openedImage = openedImage;
        this.flagImage = flag;
    }

    public void initImages() {
        bombImage = ImageLoader.loadImage("bomb.png");
        flagImage = ImageLoader.loadImage("flag.png");
        openedImage = ImageLoader.loadImage("pressed.png");
        normal = ImageLoader.loadImage("normal.png");
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public boolean isBomb() {
        return bomb;
    }

    public void setAmountOfNearBombs(int amountOfNearBombs) {
        this.amountOfNearBombs = amountOfNearBombs;
    }

    public int getAmountOfNearBombs() {
        return amountOfNearBombs;
    }

    public boolean canOpen() {
        return !opened && !bomb && !flag && amountOfNearBombs >= 0;
    }

    public void placeFlag()  {
        if(flag)
            flag = false;
        else {
            if(!opened)
                flag = true;
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void reset() {
        flag = false;
        bomb = false;
        opened = false;
    }

    public void draw(Graphics g) {
        g.setFont(new Font("SansSerif", Font.BOLD, 15));

        if(!opened) {
            if(!flag)
                g.drawImage(normal, x * width, y * height, null);
            else {
                g.drawImage(flagImage, x * width, y * height, null);
            }
        }
        else {
            if(bomb)
                g.drawImage(bombImage, x * width, y * height, null);
            else {
                g.drawImage(openedImage, x * width, y * height, null);
                if(amountOfNearBombs > 0) {
                    if (amountOfNearBombs == 1)
                        g.setColor(new Color(0, 0, 204));

                    if (amountOfNearBombs == 2)
                        g.setColor(new Color(0, 152, 0));

                    if (amountOfNearBombs == 3)
                        g.setColor(new Color(255, 0, 0));

                    if (amountOfNearBombs == 4)
                        g.setColor(new Color(0, 0, 100));

                    if (amountOfNearBombs == 5)
                        g.setColor(new Color(100, 0, 0));

                    if (amountOfNearBombs == 6)
                        g.setColor(new Color(0, 102, 102));

                    if (amountOfNearBombs == 7)
                        g.setColor(new Color(0, 0, 0));

                    if (amountOfNearBombs == 8)
                        g.setColor(new Color(128, 128, 128));

                    g.drawString("" + amountOfNearBombs, x * width + 5, y * height + height - 2);
                }
            }
        }
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
