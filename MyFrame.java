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

		JButton[][][] buttonGrid = new JButton[9][3][3];
		int[][][] gridArray = new int[9][3][3];
		
		// Creates an array to mark if there has been a winner in one of the grids
		boolean[] gridCompleted = new boolean[9];

		// Creates 9 panels for each 3x3 grid
		for (int box = 0; box < 9; box++) {
			JPanel groupPanel = new JPanel(new GridLayout(3, 3)); // 3x3 grid for buttons
			groupPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Optional: border for each group

			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					// Create a new button
					JButton button = new JButton("" + (row * 3 + col + 1));

					// Add button to the 2D array
					buttonGrid[box][row][col] = button;

					// Set color
					button.setBackground(Color.LIGHT_GRAY); // Set background color
					button.setFocusPainted(false); // Optional: removes the button border on focus

					// Adds action to the button press
					int finalBox = box;
					int finalRow = row;
					int finalCol = col;
					
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
							gridArray[finalBox][finalRow][finalCol] = 1;
							System.out.println(finalBox + " " + finalRow + " " + finalCol + " " + "X");

							button.setIcon(scaledCrossIcon);
							button.setDisabledIcon(scaledCrossIcon);
						} else {
							gridArray[finalBox][finalRow][finalCol] = 2;
							System.out.println(finalBox + " " + finalRow + " " + finalCol + " " + "O");

							button.setIcon(scaledCircleIcon);
							button.setDisabledIcon(scaledCircleIcon);
						}

						// Disables the button
						button.setEnabled(false);
						
						// Checks if any of the players won
						checkWin(gridArray, gridCompleted);

					});

					groupPanel.add(button); // Add the button to the group panel
				}

				// Add the group panel to the main panel
				mainPanel.add(groupPanel);
			}
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
	
	public static void checkWin(int[][][] gridArray, boolean[] gridCompleted) {
			
		for (int i = 0; i < 9; i++) {
			
			// If there is a winner in this grid, skip it
			if (gridCompleted[i]) {
				continue;
			}
			
			for (int j = 0; j < 3; j++) {
				
					// Check horizontal
				if (gridArray[i][j][0] == gridArray[i][j][1] && gridArray[i][j][1] == gridArray[i][j][2] && gridArray[i][j][0] != 0) {
					announceWinner(gridArray[i][j][0], i, gridCompleted);
					return;
				}
				
				 // Check vertical
	            if (gridArray[i][0][j] == gridArray[i][1][j] && gridArray[i][1][j] == gridArray[i][2][j] && gridArray[i][0][j] != 0) {
	                announceWinner(gridArray[i][0][j], i, gridCompleted);
	                return;
	            }
	            
	            // Check diagonal
	            if (gridArray[i][0][0] == gridArray[i][1][1] && gridArray[i][1][1] == gridArray[i][2][2] && gridArray[i][0][0] != 0) {
	                announceWinner(gridArray[i][0][0], i, gridCompleted);
	                return;
	            }
	            if (gridArray[i][0][2] == gridArray[i][1][1] && gridArray[i][1][1] == gridArray[i][2][0] && gridArray[i][0][2] != 0) {
	                announceWinner(gridArray[i][0][2], i, gridCompleted);
	                return;
	            }
			}
		}
	}
	
	public static void announceWinner(int player, int i, boolean[] gridCompleted) {
		
		if (player == 1) {
			
	        System.out.println("Cross wins!");
	        
	    } else if (player == 2) {
	    	
	        System.out.println("Circle wins!");
	        
	    }
		
		gridCompleted[i] = true;
		
	}

	// Main method to launch the application
	public static void main(String[] args) {
		new MyFrame();
	}
}
