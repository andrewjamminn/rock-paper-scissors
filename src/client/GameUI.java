package client;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class GameUI {

    private static void createGUI() throws IOException{
        // Set up and create the JFrame window
        JFrame frame = new JFrame("Rock Paper Scissors");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Display image
        BufferedImage picture = ImageIO.read(new File("resources/paper.png"));
        JLabel picLabel = new JLabel(new ImageIcon(picture.getScaledInstance(300, 300, Image.SCALE_FAST)));
        frame.add(picLabel);

        // Display the window
        frame.pack();
        frame.setSize(600,500);
        frame.setVisible(true);
    }
    public static void main(String[] args) throws IOException{
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createGUI();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
