
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.*;

public class MyFrame extends JFrame {

	MyFrame(){
		
		// Sets the frame's title
		this.setTitle("Super Ultimate Tic-Tac-Toe");
		
		// Closes the application
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        // Gets the screen size
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        // Sets the frame size to the full screen size
        this.setSize(screenSize.width, screenSize.height);

        // Sets the frame to be maximized
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		// Makes the frame visible
		this.setVisible(true);
		
		// Sets the background color
		this.getContentPane().setBackground(new Color(157, 220, 242));
		
		// Changes the application's icon
		ImageIcon image = new ImageIcon("23926137_l.jpg");
		this.setIconImage(image.getImage());
		
	}
	
}
