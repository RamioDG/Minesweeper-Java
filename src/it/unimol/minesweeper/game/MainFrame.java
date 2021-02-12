package it.unimol.minesweeper.game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.*;

public class MainFrame extends JFrame implements MouseListener, KeyListener {
    private JMenuBar menuBar;

    private JMenuItem easy;
    private JMenuItem normal;
    private JMenuItem hard;
    private JMenuItem expert;
    private JMenuItem master;
    private JMenuItem inhuman;
    private JMenuItem custom;

    private Screen screen;
    private World world;
    private Font font;

    private BufferedImage icon = (ImageLoader.loadImage("icon.png"));

    private int insetLeft;
    private int insetTop;

    public MainFrame() {
        super("MineSweeper");
        initMainFrame();
    }

    private void initMainFrame() {
        this.setIconImage(icon);

        initMenuBar();

        world = new World(20, 20, 25);

        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addMouseListener(this);
        addKeyListener(this);

        screen = new Screen();
        add(screen);

        File saveFile = new File("resume.save");
        File settingsFile = new File("game.config");
        if(saveFile.exists()) {
            readFile();
            String fileName = Paths.get("").toAbsolutePath().toString() + "/resume.save";
            try {
                Files.delete(Paths.get(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(settingsFile.exists()) {
            readSettings();
            String fileName = Paths.get("").toAbsolutePath().toString() + "/game.config";
            try {
                Files.delete(Paths.get(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        colorMenuItem();

        pack();
        insetLeft = getInsets().left;
        insetTop = getInsets().top;
        setSize(world.getWidth()*Tile.getWidth() + insetLeft + getInsets().right,
                world.getHeight()*Tile.getHeight() + insetTop + getInsets().bottom + menuBar.getHeight());
        setLocationRelativeTo(null);
        setVisible(true);

        font = new Font("SansSerif", Font.PLAIN, 12);
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();

        JMenu settings = new JMenu("Opzioni");

        JMenuItem restart = new JMenuItem("Ricomincia");
        JMenuItem quit = new JMenuItem("Chiudi");

        settings.add(restart);
        settings.add(quit);

        menuBar.add(settings);

        JMenu menu = new JMenu("Scegli Difficoltà");

        easy = new JMenuItem("Novizio (dimensioni: 20x10 bombe: 10)");
        normal = new JMenuItem("Medio (dimensioni: 20x20 bombe: 25)");
        hard = new JMenuItem("Adepto (dimensioni: 25x20 bombe: 50)");
        expert = new JMenuItem("Esperto (dimensioni: 25x25 bombe: 100)");
        master = new JMenuItem("Maestro (dimensioni: 40x30 bombe: 250)");
        inhuman = new JMenuItem("Inumano (dimensioni: 50x35 bombe: 350)");
        custom = new JMenuItem("Personalizza");

		menu.add(easy);
		menu.add(normal);
		menu.add(hard);
		menu.add(expert);
		menu.add(master);
		menu.add(inhuman);
		menu.add(custom);

        menuBar.add(menu);

        restart.addActionListener(actionEvent -> {
            world.reset();
            screen.repaint();
        });

        quit.addActionListener(actionEvent -> this.dispose());

		easy.addActionListener(actionEvent -> {
            world = new World(20, 10, 10);
            colorMenuItem();
            this.resize(world.getWidth()*Tile.getWidth() + insetLeft + getInsets().right,
                    world.getHeight()*Tile.getHeight() + insetTop + getInsets().bottom + menuBar.getHeight());
            screen.repaint();
            setLocationRelativeTo(null);
        });

		normal.addActionListener(actionEvent -> {
            world = new World(20, 20, 25);
            colorMenuItem();
            this.resize(world.getWidth()*Tile.getWidth() + insetLeft + getInsets().right,
                    world.getHeight()*Tile.getHeight() + insetTop + getInsets().bottom + menuBar.getHeight());
            screen.repaint();
            setLocationRelativeTo(null);
        });

        hard.addActionListener(actionEvent -> {
            world = new World(25, 20, 50);
            colorMenuItem();
            this.resize(world.getWidth()*Tile.getWidth() + insetLeft + getInsets().right,
                    world.getHeight()*Tile.getHeight() + insetTop + getInsets().bottom + menuBar.getHeight());
            screen.repaint();
            setLocationRelativeTo(null);
        });

        expert.addActionListener(actionEvent -> {
            world = new World(25, 25, 100);
            colorMenuItem();
            this.resize(world.getWidth()*Tile.getWidth() + insetLeft + getInsets().right,
                    world.getHeight()*Tile.getHeight() + insetTop + getInsets().bottom + menuBar.getHeight());
            screen.repaint();
            setLocationRelativeTo(null);
        });

        master.addActionListener(actionEvent -> {
            world = new World(40, 30, 250);
            colorMenuItem();
            this.resize(world.getWidth()*Tile.getWidth() + insetLeft + getInsets().right,
                    world.getHeight()*Tile.getHeight() + insetTop + getInsets().bottom + menuBar.getHeight());
            screen.repaint();
            setLocationRelativeTo(null);
        });

        inhuman.addActionListener(actionEvent -> {
            world = new World(50, 35, 350);
            colorMenuItem();
            this.resize(world.getWidth()*Tile.getWidth() + insetLeft + getInsets().right,
                    world.getHeight()*Tile.getHeight() + insetTop + getInsets().bottom + menuBar.getHeight());
            screen.repaint();
            setLocationRelativeTo(null);
        });

        custom.addActionListener(actionEvent -> {
            JTextField customWidthField = new JTextField();
            JTextField customHeightField = new JTextField();
            JTextField customBombsField = new JTextField();

            int customWidth;
            int customHeight;
            int customBombs;

            JOptionPane optionPane = new JOptionPane();

            optionPane.add(customWidthField);
            optionPane.add(customHeightField);
            optionPane.add(customBombsField);

            customWidth = Integer.parseInt(JOptionPane.showInputDialog(screen ,"Larghezza"));
            customHeight = Integer.parseInt(JOptionPane.showInputDialog(screen ,"Lunghezza"));
            customBombs = Integer.parseInt(JOptionPane.showInputDialog(screen ,"Numero Bombe"));


            world = new World(customWidth, customHeight, customBombs);
            this.resize(world.getWidth()*Tile.getWidth() + insetLeft + getInsets().right,
                    world.getHeight()*Tile.getHeight() + insetTop + getInsets().bottom + menuBar.getHeight());
            screen.repaint();
            setLocationRelativeTo(null);
        });

		this.setJMenuBar(menuBar);
	}

	private void colorMenuItem() {
        if(world.getNumberOfBombs() == 10) {
            easy.setBackground(Color.yellow);
            normal.setBackground(Color.lightGray);
            hard.setBackground(Color.lightGray);
            expert.setBackground(Color.lightGray);
            master.setBackground(Color.lightGray);
            inhuman.setBackground(Color.lightGray);
        }

        if(world.getNumberOfBombs() == 25) {
            easy.setBackground(Color.lightGray);
            normal.setBackground(Color.yellow);
            hard.setBackground(Color.lightGray);
            expert.setBackground(Color.lightGray);
            master.setBackground(Color.lightGray);
            inhuman.setBackground(Color.lightGray);
        }

        if(world.getNumberOfBombs() == 50) {
            easy.setBackground(Color.lightGray);
            normal.setBackground(Color.lightGray);
            hard.setBackground(Color.yellow);
            expert.setBackground(Color.lightGray);
            master.setBackground(Color.lightGray);
            inhuman.setBackground(Color.lightGray);
        }

        if(world.getNumberOfBombs() == 100) {
            easy.setBackground(Color.lightGray);
            normal.setBackground(Color.lightGray);
            hard.setBackground(Color.lightGray);
            expert.setBackground(Color.yellow);
            master.setBackground(Color.lightGray);
            inhuman.setBackground(Color.lightGray);
        }

        if(world.getNumberOfBombs() == 250) {
            easy.setBackground(Color.lightGray);
            normal.setBackground(Color.lightGray);
            hard.setBackground(Color.lightGray);
            expert.setBackground(Color.lightGray);
            master.setBackground(Color.yellow);
            inhuman.setBackground(Color.lightGray);
        }

        if(world.getNumberOfBombs() == 350) {
            easy.setBackground(Color.lightGray);
            normal.setBackground(Color.lightGray);
            hard.setBackground(Color.lightGray);
            expert.setBackground(Color.lightGray);
            master.setBackground(Color.lightGray);
            inhuman.setBackground(Color.yellow);
        }
    }

    private void writeFile(World world1, Tile[][] tiles) {
        try (
                FileOutputStream file = new FileOutputStream("resume.save");
                ObjectOutputStream output = new ObjectOutputStream(file)
        ) {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(world1);
            objects.add(tiles);

            output.writeObject(objects);
        } catch (IOException e) {
            System.out.println("Errore in scittura");
            e.printStackTrace();
        }
    }

    private void readFile() {
        try (
                FileInputStream file = new FileInputStream("resume.save");
                ObjectInputStream input = new ObjectInputStream(file)
        ) {
            ArrayList<Object> objects = (ArrayList<Object>) input.readObject();

            world = (World) objects.get(0);
            Tile[][] tiles = (Tile[][]) objects.get(1);

            for(int x = 0; x < world.getWidth(); x++) {
                for(int y = 0; y < world.getHeight(); y++) {
                    tiles[x][y].initImages();
                }
            }

            world.setTiles(tiles);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Errore in lettura");
            e.printStackTrace();
        }
    }

    private void writeSettings() {
        try (
                FileOutputStream file = new FileOutputStream("game.config");
                ObjectOutputStream output = new ObjectOutputStream(file)
        ) {
            World world1 = new World(world.getWidth(), world.getHeight(), world.getNumberOfBombs());
            output.writeObject(world1);
        } catch (IOException e) {
            System.out.println("Errore in scittura");
            e.printStackTrace();
        }
    }

    private void readSettings() {
        try (
                FileInputStream file = new FileInputStream("game.config");
                ObjectInputStream input = new ObjectInputStream(file)
        ) {
            World tempWorld = (World) input.readObject();
            world = new World(tempWorld.getWidth(), tempWorld.getHeight(), tempWorld.getNumberOfBombs());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Errore in lettura");
            e.printStackTrace();
        }
    }

    public void dispose() {
        if(!world.isDead() && !world.isFinish())
            writeFile(world, world.getTiles());
        else if(world.isFinish())
            writeSettings();
        else
            writeSettings();

        super.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == 1)
            world.clickedLeft(e.getX() - insetLeft, e.getY() - insetTop - menuBar.getHeight());
        if(e.getButton() == 3)
            world.clickedRight(e.getX() - insetLeft, e.getY() - insetTop - menuBar.getHeight());
        if(e.getButton() == 2)
            world.clickedCenter(e.getX() - insetLeft, e.getY() - insetTop - menuBar.getHeight());

        world.openAllBombs();
        screen.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_R) {
            world.reset();
            screen.repaint();
        }
        if(e.getKeyCode() == KeyEvent.VK_Q) {
            this.dispose();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public class Screen extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            g.setFont(font);
            world.draw(g);
        }
    }
}
