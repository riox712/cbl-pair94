import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

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
		JPanel mainPanel = new JPanel(new GridLayout(3, 3, 50, 50)); // 3x3 layout with gaps
		int mainPanelSize = Math.min(screenSize.width, screenSize.height) / 4 * 3; // 3/4 the screen size
		mainPanel.setPreferredSize(new Dimension(mainPanelSize,mainPanelSize));
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 10)); // Optional: border for each group

		JButton[][][] buttonGrid = new JButton[9][3][3];

		// Creates array to store the values of each grid
		int[][][] gridArray = new int[9][3][3];
		/*
		  ID Index:
			  0: Empty
			  1: Cross
			  2: Circle
			  3: Greggman
			  4:
			  5:
			  6:
			  7:
		 */
		

		// Creates an array to mark if there has been a winner in one of the grids
		int[] boxCompleted = new int[9];

		// Initialize button dimensions
		int buttonWidth = 0;
		int buttonHeight = 0;

		// Creates 9 panels for each 3x3 grid
		for (int box = 0; box < 9; box++) {
			JPanel groupPanel = new JPanel(new GridLayout(3, 3)); // 3x3 grid for buttons
			groupPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Optional: border for each group

			// TEST BUTTON FOR WIDTH AND HEIGHT
			if (box == 0) {
				JButton testButton = new JButton();
				testButton.setPreferredSize(new Dimension(100, 100)); // Set a preferred size

				// Add the button to a temporary panel to ensure layout
				JPanel tempPanel = new JPanel();
				tempPanel.add(testButton);
				tempPanel.doLayout(); // Force layout

				// Add the temporary panel to a JFrame to get sizes
				JFrame tempFrame = new JFrame();
				tempFrame.add(tempPanel);
				tempFrame.pack(); // Calculate the preferred size
				tempFrame.setVisible(true); // Make the frame visible

				// Get the size of the button after layout
				buttonWidth = testButton.getWidth(); // Width
				buttonHeight = testButton.getHeight(); // Height

				// Print the button size for confirmation
				System.out.println("Button width: " + buttonWidth + ", Button height: " + buttonHeight);

				// Remove the temporary frame from the screen
				tempFrame.dispose(); // Clean up the temporary frame
			}

			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					// Create a new button
					JButton button = new JButton();

					// Add button to the 2D array
					buttonGrid[box][row][col] = button;

					// Set color
					button.setBackground(Color.LIGHT_GRAY); // Set background color
					button.setFocusPainted(false); // Optional: removes the button border on focus

					int finalBox = box;
					int finalRow = row;
					int finalCol = col;

					// Adds action to the button press
					int finalButtonWidth = buttonWidth;
					int finalButtonHeight = buttonHeight;

					button.addActionListener(e -> {

						turn();

						// Sets the button icon when pressed
						if (crossTurn) {
							gridArray[finalBox][finalRow][finalCol] = 1;
							System.out.println(finalBox + " " + finalRow + " " + finalCol + " " + "X");
						} else {
							gridArray[finalBox][finalRow][finalCol] = 2;
							System.out.println(finalBox + " " + finalRow + " " + finalCol + " " + "O");
						}
						
						

						// Draw the grid (re-scale icons when button size is known)
						drawGrid(gridArray, buttonGrid, finalButtonWidth, finalButtonHeight);

						// Disables the button
						button.setEnabled(false);

						// Checks if any of the players won
						checkWin(gridArray, boxCompleted);

						int currentBox = finalBox;
						int nextBox = (finalRow * 3) + finalCol;

						lockBoxes(boxCompleted, buttonGrid, gridArray, nextBox, currentBox);
						
						powerupGenerator(boxCompleted, gridArray);

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

	public static void drawGrid(int[][][] gridArray, JButton[][][] buttonGrid, int buttonWidth, int buttonHeight) {
		// Load images
		ImageIcon[] icons = loadImages(buttonWidth, buttonHeight);

		for (int box = 0; box < 9; box++) {
			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {

					switch (gridArray[box][row][col]) {
						case 0:
							buttonGrid[box][row][col].setIcon(icons[0]);
							break;
						case 1:
							buttonGrid[box][row][col].setIcon(icons[1]); // Cross icon
							buttonGrid[box][row][col].setDisabledIcon(icons[1]);
							break;
						case 2:
							buttonGrid[box][row][col].setIcon(icons[2]); // Circle icon
							buttonGrid[box][row][col].setDisabledIcon(icons[2]);
							break;
						case 3:
							buttonGrid[box][row][col].setIcon(icons[3]); // Greg icon
							buttonGrid[box][row][col].setDisabledIcon(icons[3]);
							break;
					}
				}
			}
		}
	}

    public static void checkWin(int[][][] gridArray, int[] boxCompleted) {
			
		for (int i = 0; i < 9; i++) {
			
			// If there is a winner in this grid, skip it
			if (boxCompleted[i] != 0) {
				continue;
			}
			
			for (int j = 0; j < 3; j++) {
				
					// Check horizontal
				if (gridArray[i][j][0] == gridArray[i][j][1] && gridArray[i][j][1] == gridArray[i][j][2] && gridArray[i][j][0] != 0) {
					if (crossTurn) {
						boxCompleted[i] = 1;
					} else if (!crossTurn) {
						boxCompleted[i] = 2;
					}
										
					announceWinner(gridArray[i][j][0]);
					checkBigWin(boxCompleted);
					return;
				}
				
				 // Check vertical
	            if (gridArray[i][0][j] == gridArray[i][1][j] && gridArray[i][1][j] == gridArray[i][2][j] && gridArray[i][0][j] != 0) {
	            	if (crossTurn) {
						boxCompleted[i] = 1;
					} else if (!crossTurn) {
						boxCompleted[i] = 2;
					}
						
	                announceWinner(gridArray[i][0][j]);
	                checkBigWin(boxCompleted);
	                return;
	            }
	            
	            // Check diagonal
	            if (gridArray[i][0][0] == gridArray[i][1][1] && gridArray[i][1][1] == gridArray[i][2][2] && gridArray[i][0][0] != 0) {
	            	if (crossTurn) {
						boxCompleted[i] = 1;
					} else if (!crossTurn) {
						boxCompleted[i] = 2;
					}
						
	                announceWinner(gridArray[i][0][0]);
	                checkBigWin(boxCompleted);
	                return;
	            }
	            if (gridArray[i][0][2] == gridArray[i][1][1] && gridArray[i][1][1] == gridArray[i][2][0] && gridArray[i][0][2] != 0) {
	            	if (crossTurn) {
						boxCompleted[i] = 1;
					} else if (!crossTurn) {
						boxCompleted[i] = 2;
					}
						
	                announceWinner(gridArray[i][0][2]);
	                checkBigWin(boxCompleted);
	                return;
	            }

			}
			
		}
	}
    
    public static void checkBigWin(int[] boxCompleted) {
        int[][] winPatterns = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Horizontal
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Vertical
            {0, 4, 8}, {2, 4, 6}             // Diagonals
        };
        
        for (int[] pattern : winPatterns) {
            if (boxCompleted[pattern[0]] == boxCompleted[pattern[1]] &&
                boxCompleted[pattern[1]] == boxCompleted[pattern[2]] &&
                boxCompleted[pattern[0]] != 0) {
                announceWinner(boxCompleted[pattern[0]]);
                return;
            }
        }
    }

    public static void lockBoxes(int[] boxCompleted, JButton[][][] buttonGrid, int[][][] gridArray, int nextBox, int currentBox) {
        for (int box = 0; box < 9; box++) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    
                    // If there is no winner in the target box (nextBox)
                    if (boxCompleted[nextBox] == 0) {
                        // If we're not in the nextBox, disable all cells
                        if (box != nextBox) {
                            buttonGrid[box][row][col].setEnabled(false);
                        } else {
                            // Enable cells in the nextBox unless they already contain a cross (1) or circle (2)
                            if (gridArray[nextBox][row][col] == 1 || gridArray[nextBox][row][col] == 2) {
                                buttonGrid[nextBox][row][col].setEnabled(false);
                            } else {
                                buttonGrid[nextBox][row][col].setEnabled(true);
                            }
                        }
                    } else {
                        // When nextBox has a winner or we're in any completed box, enforce locks across the grid
                        if (box == currentBox || boxCompleted[box] != 0) {
                            buttonGrid[box][row][col].setEnabled(false);
                        } else {
                            // Enable any cells that don’t have crosses, circles, or completed grids (but keep powerups active)
                            if (gridArray[box][row][col] == 1 || gridArray[box][row][col] == 2) {
                                buttonGrid[box][row][col].setEnabled(false);
                            } else {
                                buttonGrid[box][row][col].setEnabled(true);
                            }
                        }
                        // Ensure all cells in the nextBox are locked after a win there
                        buttonGrid[nextBox][row][col].setEnabled(false);
                    }
                }
            }
        }
    }
	
	public static void announceWinner(int player) {
		
		if (player == 1) {
			
	        System.out.println("Cross wins!");
	        
	    } else if (player == 2) {
	    	
	        System.out.println("Circle wins!");
	        
	    }
		
	}

	public static ImageIcon[] loadImages(int buttonWidth, int buttonHeight) {
		// Load images
		ImageIcon originalCross = new ImageIcon("cross.png");
		ImageIcon originalCircle = new ImageIcon("circle.png");
		ImageIcon originalGreg = new ImageIcon("greggman.png");

		// Scale images
		Image scaledCross = originalCross.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledCrossIcon = new ImageIcon(scaledCross);

		Image scaledCircle = originalCircle.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledCircleIcon = new ImageIcon(scaledCircle);

		Image scaledGreg = originalGreg.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledGregIcon = new ImageIcon(scaledGreg);

		// Return both icons as an array
		return new ImageIcon[] {null, scaledCrossIcon, scaledCircleIcon, scaledGregIcon};
	}
	
	
	public static void powerupGenerator(int[] boxCompleted, int[][][] gridArray) {
		if (round % 5 == 0) {
	        while (true) {
	            // Generate random positions for the power-up within valid ranges
	            int randomBox = ThreadLocalRandom.current().nextInt(0, 9);
	            int randomRow = ThreadLocalRandom.current().nextInt(0, 3);
	            int randomCol = ThreadLocalRandom.current().nextInt(0, 3);

	            // Check if selected box is completed or cell is already occupied
	            if (boxCompleted[randomBox] == 0 && gridArray[randomBox][randomRow][randomCol] == 0) {
	                // Place the power-up in an empty cell of an uncompleted box
	                gridArray[randomBox][randomRow][randomCol] = 3;
	                break;
	            }
	        }
	    }
	}
	

	// ADD A METHOD WHICH CHECKS IF A POWERUP HAS BEEN COLLECTED (gridArrays value changes from 3 to 1 or 2) !!!!!!!
	// THE METHOD BELOW SHOULD BE CALLED THEN
	public static void powerupPicker() {
		
		// Randomly chooses a power-up within the range
		int randomPup = ThreadLocalRandom.current().nextInt(0, 3);
		
		switch(randomPup) {
			// Gives a certain player an additional round
			case 0:
				round++;
				System.out.println("POWERUP 1");
			    break;
			
			case 1:
				System.out.println("POWERUP 2");
			    break;
			case 2:
				System.out.println("POWERUP 3");
				 break;
			default:
			    // code block
			}

		
	}

	// Main method to launch the application
	public static void main(String[] args) {
		new MyFrame();
	}
}
