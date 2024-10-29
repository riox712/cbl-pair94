import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyFrame extends JFrame {

	public static boolean crossTurn = false;
	public static int round = 0;
	public static int pup = 0;
	
	MyFrame() {
		// Sets the frame's title
		this.setTitle("Super Ultimate Tic-Tac-Toe");

		// Changes the application's icon
		ImageIcon image = new ImageIcon("images/23926137_l.jpg");
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
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;

		// Create the main panel to hold the 3x3 boxes
		JPanel mainPanel = new JPanel(new GridLayout(3, 3, 50, 50)); // 3x3 layout with gaps
		int mainPanelSize = Math.min(screenSize.width, screenSize.height) * 3 / 4; // 3/4 the screen size
		mainPanel.setPreferredSize(new Dimension(mainPanelSize, mainPanelSize));
		mainPanel.setBackground(new Color(57, 155, 200));
		mainPanel.setBorder(BorderFactory.createLineBorder(new Color(57, 155, 200), 10));
		//mainPanel.setBorder(BorderFactory.createLineBorder(new Color(11, 26, 59), 2));

		JButton[][][] buttonGrid = new JButton[9][3][3];

		// Creates array to store the values of each grid
		int[][][] gridArray = new int[9][3][3];
		/*
		  ID Index:
			  0: Empty
			  1: Cross
			  2: Circle
			  3: Repeat
			  4: Alien
			  5: Glitch
			  6: Green
		 */


		// Creates an array to mark if there has been a winner in one of the grids
		int[] boxCompleted = new int[9];

		int buttonWidth = mainPanelSize / 11;
		int buttonHeight = mainPanelSize / 11;

		// Load images
		ImageIcon[] icons = loadImages(buttonWidth, buttonHeight);

		// Creates 9 panels for each 3x3 grid
		for (int box = 0; box < 9; box++) {
			JPanel groupPanel = new JPanel(new GridLayout(3, 3)); // 3x3 grid for buttons
			groupPanel.setBackground(new Color(193, 209, 244));
			groupPanel.setBorder(BorderFactory.createLineBorder(new Color(193, 209, 244), 5));

			JPanel[] groupPanelArray = new JPanel[9];
			groupPanelArray[box] = groupPanel;

			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					// Create a new button
					JButton button = new JButton();

					// Add button to the 2D array
					buttonGrid[box][row][col] = button;

					button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));

					button.setContentAreaFilled(false);
					button.setBorderPainted(false);

					button.setIcon(icons[0]);
					button.setDisabledIcon(icons[8]);

					int finalBox = box;
					int finalRow = row;
					int finalCol = col;


					// Add mouse hover effects
					button.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							if (gridArray[finalBox][finalRow][finalCol] == 0) {  // Only change if empty
								button.setIcon(icons[9]);
							}
						}

						@Override
						public void mouseExited(MouseEvent e) {
							if (gridArray[finalBox][finalRow][finalCol] == 0) {  // Only revert if empty
								button.setIcon(icons[0]);
							}
						}
					});

					// Adds action to the button press
					button.addActionListener(e -> {
						
						int currentBox = finalBox;
						int nextBox = (finalRow * 3) + finalCol;
						
						turn();
						
					    if (gridArray[finalBox][finalRow][finalCol] == 6) {
					        System.out.println("Cannot play over a power-up.");
					        return; // Exit if trying to play over a power-up
					    }
						
						
						
						// Get current value before marking
						int powerup = gridArray[finalBox][finalRow][finalCol];
						// Check if it was a power-up
						checkPowerupCollected(powerup);
						
						remover(boxCompleted, gridArray);
						
						replace(boxCompleted, gridArray);
						
						// Sets the button icon when pressed
						if (crossTurn) {
							gridArray[finalBox][finalRow][finalCol] = 1;
							System.out.println(finalBox + " " + finalRow + " " + finalCol + " " + "X");
						} else {
							gridArray[finalBox][finalRow][finalCol] = 2;
							System.out.println(finalBox + " " + finalRow + " " + finalCol + " " + "O");
						}


						
						// Disables the button
						button.setEnabled(false);

						// Checks if any of the players won
						checkWin(gridArray, boxCompleted, groupPanelArray);

						
						
						lockBoxes(boxCompleted, buttonGrid, gridArray, nextBox, currentBox);
						
						

						powerupGenerator(boxCompleted, gridArray);

						// Draw the grid (re-scale icons when button size is known)
						drawGrid(gridArray, buttonGrid, buttonWidth, buttonHeight);
					});

					groupPanel.add(button); // Add the button to the group panel
				}

				// Add the group panel to the main panel
				mainPanel.add(groupPanel);
			}
		}

		// Center the main panel using GridBagLayout
		this.add(mainPanel, gbc);

		mainPanel.revalidate();
		mainPanel.repaint();

		// Make the frame visible
		this.setVisible(true);
	}

	public static void turn() {
		
		round++;

        crossTurn = round % 2 == 0;

	}

	public static void drawGrid(int[][][] gridArray, JButton[][][] buttonGrid, int buttonWidth, int buttonHeight) {
		// Load images
		ImageIcon[] icons = loadImages(buttonWidth, buttonHeight);


		for (int box = 0; box < 9; box++) {
			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {

					switch (gridArray[box][row][col]) {
						case 0:
							if (buttonGrid[box][row][col].isEnabled()) {
								// The button is disabled
								buttonGrid[box][row][col].setIcon(icons[0]);
							} else {
								// The button is enabled
								buttonGrid[box][row][col].setIcon(icons[8]);
							}
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
							buttonGrid[box][row][col].setIcon(icons[3]); // Repeat icon
							buttonGrid[box][row][col].setDisabledIcon(icons[3]);
							break;
						case 4:
							buttonGrid[box][row][col].setIcon(icons[4]); // Alien icon
							buttonGrid[box][row][col].setDisabledIcon(icons[4]);
							break;
						case 5:
							buttonGrid[box][row][col].setIcon(icons[5]); // Glitch icon
							buttonGrid[box][row][col].setDisabledIcon(icons[5]);
							break;
						case 6:
							buttonGrid[box][row][col].setIcon(icons[6]); // Green icon
							buttonGrid[box][row][col].setDisabledIcon(icons[6]);
					}
				}
			}
		}
	}

    public static void checkWin(int[][][] gridArray, int[] boxCompleted, JPanel[] groupPanelArray) {
			
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
					drawBoxWin(boxCompleted, groupPanelArray, i);
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
					drawBoxWin(boxCompleted, groupPanelArray, i);
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
					drawBoxWin(boxCompleted, groupPanelArray, i);
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
					drawBoxWin(boxCompleted, groupPanelArray, i);
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
				EndScreen endScreen = new EndScreen();
            }
        }
    }

	public static void drawBoxWin(int[] boxCompleted, JPanel[] groupPanelArray, int box) {
		if (boxCompleted[box] == 1) {
			groupPanelArray[box].setBackground(new Color(55, 89, 164));
			groupPanelArray[box].setBorder(BorderFactory.createLineBorder(new Color(55, 89, 164), 5));
		} else if (boxCompleted[box] == 2) {
			groupPanelArray[box].setBackground(new Color(207, 50, 50));
			groupPanelArray[box].setBorder(BorderFactory.createLineBorder(new Color(207, 50, 50), 5));
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
                            // Enable any cells that don't have crosses, circles, or completed grids (but keep power-ups active)
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
		ImageIcon originalEmpty = new ImageIcon("images/empty.png");
		ImageIcon originalEmptyLocked = new ImageIcon("images/emptyLocked.png");
		ImageIcon originalEmptyHover = new ImageIcon("images/emptyHover.png");
		ImageIcon originalCross = new ImageIcon("images/cross.png");
		ImageIcon originalCircle = new ImageIcon("images/circle.png");
		ImageIcon originalGreg = new ImageIcon("images/repeat.png");
		ImageIcon originalAlien = new ImageIcon("images/alien.png");
		ImageIcon originalGlitch = new ImageIcon("images/glitch.png");
		ImageIcon originalGreen = new ImageIcon("images/green.png");

		// Scale images
		Image scaledCross = originalCross.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledCrossIcon = new ImageIcon(scaledCross);

		Image scaledCircle = originalCircle.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledCircleIcon = new ImageIcon(scaledCircle);

		Image scaledGreg = originalGreg.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledGregIcon = new ImageIcon(scaledGreg);
		
		Image scaledAlien = originalAlien.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledAlienIcon = new ImageIcon(scaledAlien);
		
		Image scaledGlitch = originalGlitch.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledGlitchIcon = new ImageIcon(scaledGlitch);
		
		Image scaledGreen = originalGreen.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledGreenIcon = new ImageIcon(scaledGreen);

		Image scaledEmpty = originalEmpty.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledEmptyIcon = new ImageIcon(scaledEmpty);

		Image scaledEmptyLocked = originalEmptyLocked.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledEmptyLockedIcon = new ImageIcon(scaledEmptyLocked);

		Image scaledEmptyHover = originalEmptyHover.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledEmptyHoverIcon = new ImageIcon(scaledEmptyHover);

		// Return both icons as an array
		return new ImageIcon[] {scaledEmptyIcon, scaledCrossIcon, scaledCircleIcon, scaledGregIcon, scaledAlienIcon, scaledGlitchIcon, scaledGreenIcon, null , scaledEmptyLockedIcon, scaledEmptyHoverIcon};
	}
	
	public static void powerupGenerator(int[] boxCompleted, int[][][] gridArray) {
		if (round % 5 == 0) {
	        while (true) {
	            // Generate random positions for the power-up within valid ranges
	            int randomBox = ThreadLocalRandom.current().nextInt(0, 9);
	            int randomRow = ThreadLocalRandom.current().nextInt(0, 3);
	            int randomCol = ThreadLocalRandom.current().nextInt(0, 3);

	    		// Randomly chooses a power-up within the range
	    		int randomPup = ThreadLocalRandom.current().nextInt(3, 6);
	            
	            // Check if selected box is completed or cell is already occupied
	            if (boxCompleted[randomBox] == 0 && gridArray[randomBox][randomRow][randomCol] == 0) {
	                // Place the power-up in an empty cell of an uncompleted box
	                gridArray[randomBox][randomRow][randomCol] = randomPup;
	                break;
	            }
	        }
	    }
	}
	
	public static void checkPowerupCollected(int powerup) {
	    	
	    if (powerup == 3 || powerup == 4 || powerup == 5) {
	        
	        // Trigger effect based on power-up type
	    	powerupPicker(powerup);
	        
	    }
	    
	}

	public static void powerupPicker(int powerupType) {
		
	    switch (powerupType) {
	        case 3:
	            System.out.println("Power-up Type 3 collected!");
	            // Player gets an extra turn
	            round--;
	            break;
	        case 4:
	            System.out.println("Power-up Type 4 collected!");
	            // Places a box that cannot be replaced
	            pup = 1;
	            break;
	        case 5:
	            System.out.println("Power-up Type 5 collected!");
	            // Randomly clears an uncompleted box
	            pup = 2;
	            break;
	        default:
	            break;
	    }
	}

	public static void replace(int[] boxCompleted, int[][][] gridArray) {
	    if (pup == 1) {
	        System.out.println("Placing a green square randomly.");
	        
	        while (true) {
	            // Generate random positions for the green square within valid ranges
	            int randomBox = ThreadLocalRandom.current().nextInt(0, 9);
	            int randomRow = ThreadLocalRandom.current().nextInt(0, 3);
	            int randomCol = ThreadLocalRandom.current().nextInt(0, 3);
	            
	            // Check if selected box is completed or cell is already occupied
	            if (boxCompleted[randomBox] == 0 && gridArray[randomBox][randomRow][randomCol] == 0) {
	                // Place the green square in an empty cell of an uncompleted box
	                gridArray[randomBox][randomRow][randomCol] = 6;
	                pup = 0;
	                break;
	            }
	        }
	    }
	}


	
	public static void remover(int[] boxCompleted, int[][][] gridArray) {
	  
	    if (pup != 2) return;
	    
	    System.out.println("Removing nine spaces");

	    int spacesToRemove = 9; // Total spaces to remove
	    int removedCount = 0;   // Counter for removed spaces

	    
	    pup = 0;

	    while (removedCount < spacesToRemove) {
	        // Generate a random box
	        int randomBox = ThreadLocalRandom.current().nextInt(0, 9);

	        // Ensure the selected box is not completed
	        if (boxCompleted[randomBox] == 0) {
	            // Try to remove a random space within this box
	            int randomRow = ThreadLocalRandom.current().nextInt(0, 3);
	            int randomCol = ThreadLocalRandom.current().nextInt(0, 3);
	            gridArray[randomBox][randomRow][randomCol] = 0;
	            removedCount++;
	        }
	    }
	}
	
	
	// Main method to launch the application
	public static void main(String[] args) {
		new MyFrame();
	}
}
