package it.unimol.minesweeper.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Random;

public class World implements Serializable {
    private static final long serialVersionUID = 1L;

    private int width;
    private int height;

    private int numberOfBombs;

    private int flags = 0;
    private int rightFlags = 0;

    private boolean finish;
    private boolean dead;

    private Random random;

    private Tile[][] tiles;

    private transient BufferedImage bomb;
    private transient BufferedImage flag;
    private transient BufferedImage pressed;
    private transient BufferedImage normal;

    public World(int width, int height, int numberOfBombs) {
        this.width = width;
        this.height = height;
        this.numberOfBombs = numberOfBombs;

        initWorld();
    }

    private  void initWorld() {
        random = new Random();

        initImages();

        tiles = new Tile[width][height];

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(x, y, normal, bomb, pressed, flag);
            }
        }

        reset();
    }

    public void initImages() {
        bomb = ImageLoader.loadImage("bomb.png");
        flag = ImageLoader.loadImage("flag.png");
        pressed = ImageLoader.loadImage("pressed.png");
        normal = ImageLoader.loadImage("normal.png");
    }

    private void placeBombs() {
        for(int i = 0; i < numberOfBombs; i++) {
            placeBomb();
        }
    }

    private void placeBomb() {
        int x = random.nextInt(width);
        int y = random.nextInt(height);

        if(!tiles[x][y].isBomb())
            tiles[x][y].setBomb(true);
        else placeBomb();
    }

    private void setNumbers() {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int mx = x - 1;
                int gx = x + 1;
                int my = y - 1;
                int gy = y + 1;

                int amountOfBombs = 0;

                if(mx >= 0 && my >= 0 && tiles[mx][my].isBomb())
                    amountOfBombs++;
                if(mx >= 0 && tiles[mx][y].isBomb())
                    amountOfBombs++;
                if(mx >= 0 && gy < height && tiles[mx][gy].isBomb())
                    amountOfBombs++;

                if(my >= 0 && tiles[x][my].isBomb())
                    amountOfBombs++;
                if(gy < height && tiles[x][gy].isBomb())
                    amountOfBombs++;

                if(gx < width && my >= 0 && tiles[gx][my].isBomb())
                    amountOfBombs++;
                if(gx < width && tiles[gx][y].isBomb())
                    amountOfBombs++;
                if(gx < width && gy < height && tiles[gx][gy].isBomb())
                    amountOfBombs++;

                tiles[x][y].setAmountOfNearBombs(amountOfBombs);
            }
        }
    }

    public void clickedLeft(int x, int y) {
        if(!dead && !finish) {
            int tileX = x / Tile.getWidth();
            int tileY = y / Tile.getHeight();

            if(!tiles[tileX][tileY].isFlag()) {
                tiles[tileX][tileY].setOpened(true);

                if(tiles[tileX][tileY].isBomb())
                    dead = true;
                else {
                    if(tiles[tileX][tileY].getAmountOfNearBombs() == 0) {
                        open(tileX, tileY);
                    }
                }

                checkFinish();
            }
        }
    }

    public void clickedCenter(int x, int y) {
        if(!dead && !finish) {
            int tileX = x / Tile.getWidth();
            int tileY = y / Tile.getHeight();

            if(!tiles[tileX][tileY].isFlag() && !tiles[tileX][tileY].isBomb())
                tiles[tileX][tileY].setOpened(true);

            if(tiles[tileX][tileY].isBomb())
                tiles[tileX][tileY].placeFlag();
            else {
                if(tiles[tileX][tileY].getAmountOfNearBombs() == 0) {
                    open(tileX, tileY);
                }
            }
            checkFinish();

        }
    }

    public void clickedRight(int x, int y) {
        if(!dead && !finish) {


            int tileX = x / Tile.getWidth();
            int tileY = y / Tile.getHeight();
            tiles[tileX][tileY].placeFlag();

            if(!tiles[tileX][tileY].isBomb() && tiles[tileX][tileY].isFlag())
                flags++;

            if(!tiles[tileX][tileY].isBomb() && !tiles[tileX][tileY].isFlag())
                flags--;

            if(tiles[tileX][tileY].isBomb() && tiles[tileX][tileY].isFlag()) {
                rightFlags++;
                flags++;
            }

            if(tiles[tileX][tileY].isBomb() && !tiles[tileX][tileY].isFlag()){
                rightFlags--;
                flags--;
            }

            checkFinish();
        }
    }

    private void open(int x, int y) {
        tiles[x][y].setOpened(true);

        if(tiles[x][y].getAmountOfNearBombs() == 0) {
            int mx = x - 1;
            int gx = x + 1;
            int my = y - 1;
            int gy = y + 1;


            if(mx >= 0 && my >= 0 && tiles[mx][my].canOpen())
                open(mx, my);
            if(mx >= 0 && tiles[mx][y].canOpen())
                open(mx, y);
            if(mx >= 0 && gy < height && tiles[mx][gy].canOpen())
                open(mx, gy);

            if(my >= 0 && tiles[x][my].canOpen())
                open(x, my);
            if(gy < height && tiles[x][gy].canOpen())
                open(x, gy);

            if(gx < width && my >= 0 && tiles[gx][my].canOpen())
                open(gx, my);
            if(gx < width && tiles[gx][y].canOpen())
                open(gx, y);
            if(gx < width && gy < height && tiles[gx][gy].canOpen())
                open(gx, gy);
        }
    }

    public void openAllBombs() {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                if(dead && tiles[x][y].isBomb() && !tiles[x][y].isFlag())
                    tiles[x][y].setOpened(true);
                if(dead && !tiles[x][y].isBomb() && tiles[x][y].isFlag())
                    tiles[x][y].setOpened(true);
            }
        }
    }

    private void checkFinish() {
        finish = true;

        outer : for(int x = 0; x < width; x++) {
            for(int y = 0;y < height;y++) {
                if (numberOfBombs == flags && flags == rightFlags) {
                    finish = true;
                    break outer;
                }

                if(!(tiles[x][y].isOpened() || (tiles[x][y].isBomb() && tiles[x][y].isFlag()))) {
                    finish = false;
                    break outer;
                }
            }
        }
    }

    public void reset() {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                tiles[x][y].reset();
            }
        }

        rightFlags = 0;
        flags = 0;

        dead = false;
        finish = false;

        placeBombs();
        setNumbers();
    }

    public void draw(Graphics g) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                tiles[x][y].draw(g);
            }
        }

        if(dead) {
            g.setColor(Color.lightGray);
            g.fillRect(8, 15, 92, 20);
            g.setColor(Color.BLACK);
            g.drawString("Game Over!", 10, 30);

        }
        else if(finish) {
            g.setColor(Color.lightGray);
            g.fillRect(8, 15, 62, 20);
            g.setColor(Color.BLACK);
            g.drawString("Vittoria!", 10, 30);
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isFinish() {
        return finish;
    }

    public int getNumberOfBombs() {
        return numberOfBombs;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
