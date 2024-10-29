import javax.swing.*;
import java.awt.*;

public class EndScreen extends JFrame {

    EndScreen(){
        // Sets the frame's title
        this.setTitle("End Screen!");

        // Changes the application's icon
        ImageIcon image = new ImageIcon("images/23926137_l.jpg");
        this.setIconImage(image.getImage());

        // Closes the application
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Get the screen size and set the frame to be maximized
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        this.setSize(screenSize.width / 2, screenSize.height / 2);
        this.setBounds(screenSize.width / 4, screenSize.height / 4, screenSize.width / 2, screenSize.height / 2);

        // Set the background color
        this.getContentPane().setBackground(new Color(30, 100, 100));


        ImageIcon originalGG = new ImageIcon("images/gg.png");
        Image scaledGG = originalGG.getImage().getScaledInstance(screenSize.width / 2, screenSize.height / 6, Image.SCALE_SMOOTH);
        ImageIcon scaledGGIcon = new ImageIcon(scaledGG);

        int buttonWidth = screenSize.width / 14;
        int buttonHeight = screenSize.height / 2;

        ImageIcon originalPlayAgain = new ImageIcon("images/circle.png");
        Image scaledPlayAgain = originalPlayAgain.getImage().getScaledInstance(buttonWidth, buttonHeight / 4, Image.SCALE_SMOOTH);
        ImageIcon scaledPlayAgainIcon = new ImageIcon(scaledPlayAgain);

        ImageIcon originalExit = new ImageIcon("images/cross.png");
        Image scaledExit = originalExit.getImage().getScaledInstance(buttonWidth, buttonHeight / 4, Image.SCALE_SMOOTH);
        ImageIcon scaledExitIcon = new ImageIcon(scaledExit);

        JLabel ggLabel = new JLabel(scaledGGIcon);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.setPreferredSize(new Dimension(screenSize.width / 4,screenSize.height / 4 ));
        mainPanel.setBackground(new Color(57, 155, 200));

        mainPanel.add(ggLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.setPreferredSize(new Dimension(screenSize.width / 4,screenSize.height / 8 ));
        buttonPanel.setBackground(new Color(57, 155, 200));

        // Play again button
        JButton playAgain = new JButton();
        playAgain.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        playAgain.setContentAreaFilled(false);
        playAgain.setBorderPainted(false);
        playAgain.setIcon(scaledPlayAgainIcon);
        playAgain.addActionListener(e -> {
            // Retrieve all windows
            java.awt.Window[] win = java.awt.Window.getWindows();
            for (int i = 0; i < win.length; i++) {
                // Close all windows
                win[i].dispose();
            }
            // Open new window
            MyFrame myFrame = new MyFrame();
            myFrame.setVisible(true);
        });

        // Exit button
        JButton exit = new JButton();
        exit.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        exit.setContentAreaFilled(false);
        exit.setBorderPainted(false);
        exit.setIcon(scaledExitIcon);
        exit.addActionListener(e -> {
            System.exit(0);
        });

        buttonPanel.add(playAgain);

        buttonPanel.add(exit);

        mainPanel.add(buttonPanel);
        this.add(mainPanel);

        this.setVisible(true);
    }

    // Main method to launch the application
    public static void main(String[] args) {
        new EndScreen();
    }
}
