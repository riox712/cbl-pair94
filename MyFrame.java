import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

	MyFrame() {
		// Sets the frame's title
		this.setTitle("Super Ultimate Tic-Tac-Toe");

		// Closes the application
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Get the screen size and set the frame to be maximized
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		this.setSize(screenSize.width, screenSize.height);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Set the background color
		this.getContentPane().setBackground(new Color(157, 220, 242));

		// Set a layout manager that centers the panel
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Create the main panel to hold the 3x3 groups
		JPanel mainPanel = new JPanel(new GridLayout(3, 3, 0, 0)); // 3x3 layout with gaps

		// Create 9 panels for each 3x3 grid
		for (int i = 0; i < 9; i++) {
			JPanel groupPanel = new JPanel(new GridLayout(3, 3)); // 3x3 grid for buttons
			groupPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Optional: border for each group

			// Add 9 buttons to the group panel
			for (int j = 0; j < 9; j++) {
				JButton button = new JButton(); // Empty button
				button.setBackground(Color.LIGHT_GRAY); // Set background color
				button.setFocusPainted(false); // Optional: removes the button border on focus
				groupPanel.add(button); // Add the button to the group panel
			}

			// Add the group panel to the main panel
			mainPanel.add(groupPanel);
		}

		// Center the main panel using GridBagLayout
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(mainPanel, gbc);

		// Make the frame visible
		this.setVisible(true);
	}

	// Main method to launch the application
	public static void main(String[] args) {
		new MyFrame();
	}
}
