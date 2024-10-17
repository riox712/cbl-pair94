import javax.swing.*;
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

			// Sets the background color
			this.getContentPane().setBackground(new Color(157, 220, 242));

			// Changes the application's icon
			ImageIcon image = new ImageIcon("23926137_l.jpg");
			this.setIconImage(image.getImage());

			// Set the layout of the frame explicitly to BorderLayout (optional)
			this.setLayout(new BorderLayout());

			// Create a JPanel with GridLayout for 9 rows and 9 columns
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(9, 9)); // 9x9 grid

			// Add 81 buttons to the panel
			for (int i = 1; i <= 81; i++) {
				JButton button = new JButton(String.valueOf(i)); // Create a button with text
				button.setBackground(Color.LIGHT_GRAY); // Set background color
				panel.add(button); // Add the button to the panel
			}

			// Add the panel to the center of the frame (BorderLayout.CENTER)
			this.add(panel, BorderLayout.CENTER); // Ensure the panel is added to the center

			// Make the frame visible
			this.setVisible(true);
		}

		// Main method to launch the application
		public static void main(String[] args) {
			new MyFrame();
		}
	}
