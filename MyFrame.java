import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

	public static boolean crossTurn = false;
	public static int round = 1;
	
	MyFrame() {
		// Sets the frame's title
		this.setTitle("Super Ultimate Tic-Tac-Toe");

		// Changes the application's icon
		ImageIcon image = new ImageIcon("23926137_l.jpg");
		this.setIconImage(image.getImage());

		// Closes the application
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Get the screen size and set the frame to be maximized
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		this.setSize(screenSize.width, screenSize.height);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Set the background color
		this.getContentPane().setBackground(new Color(30, 100, 100));

		// Set a layout manager that centers the panel
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Create the main panel to hold the 3x3 groups
		JPanel mainPanel = new JPanel(new GridLayout(3, 3, 10, 10)); // 3x3 layout with gaps
		int mainPanelSize = Math.min(screenSize.width, screenSize.height) / 4 * 3; // 3/4 the screen size
		mainPanel.setPreferredSize(new Dimension(mainPanelSize,mainPanelSize));
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 10)); // Optional: border for each group

		// Create 9 panels for each 3x3 grid
		for (int i = 0; i < 9; i++) {
			JPanel groupPanel = new JPanel(new GridLayout(3, 3)); // 3x3 grid for buttons
			groupPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Optional: border for each group

			// Add 9 buttons to the group panel
			for (int j = 0; j < 9; j++) {
				JButton button = new JButton(); // Empty button
				button.setBackground(Color.LIGHT_GRAY); // Set background color
				button.setFocusPainted(false); // Optional: removes the button border on focus
				
				// Adds action to the button press
			    button.addActionListener(e -> {
			    	
			    	turn();
			    	
			        // Loads image
			    	ImageIcon originalCross = new ImageIcon("cross.png");
			    	ImageIcon originalCircle = new ImageIcon("circle.png");
			        
			        // Scales the image to the button's size
			        Image scaledCross = originalCross.getImage().getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
			        ImageIcon scaledCrossIcon = new ImageIcon(scaledCross);
			        
			        Image scaledCircle = originalCircle.getImage().getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
			        ImageIcon scaledCircleIcon = new ImageIcon(scaledCircle);
			        
			        // Sets the button icon when pressed
			        if (crossTurn) {
				        button.setIcon(scaledCrossIcon);
				        button.setDisabledIcon(scaledCrossIcon);
			        } else {
				        button.setIcon(scaledCircleIcon);
				        button.setDisabledIcon(scaledCircleIcon);
			        }
			        
			        // Disables the button
			        button.setEnabled(false);
			        
			    });
				
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
	
	public static void turn() {
		
		round++;
		
		if (round%2==0) {
			crossTurn = true;
		} else {
			crossTurn = false;
		}
		
		
	}


	// Main method to launch the application
	public static void main(String[] args) {
		new MyFrame();
	}
}
