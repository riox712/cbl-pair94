import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyFrame extends JFrame {

	public static boolean crossTurn = false;
	public static int round = 0;
	
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
			  3: Greggman
			  4: death
			  5: agni
			  6:
			  7:
		 */


		// Creates an array to mark if there has been a winner in one of the grids
		int[] boxCompleted = new int[9];

		//int buttonWidth = screenSize.width / 22 - 10;
		//int buttonHeight = screenSize.height * 8/99 - 10;
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

						turn();
						
						// Get current value before marking
						int powerup = gridArray[finalBox][finalRow][finalCol];
						// Check if it was a power-up
						checkPowerupCollected(powerup);
						
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

						int currentBox = finalBox;
						int nextBox = (finalRow * 3) + finalCol;

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
		// load platform
		//ImageIcon originalPlatform = new ImageIcon("platform.png");
		//Image scaledPlatform = originalPlatform.getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
		//ImageIcon scaledPlatformIcon = new ImageIcon(scaledPlatform);

		//JLabel platformLabel = new JLabel(scaledPlatformIcon);

		//this.add(platformLabel);

		// Center the main panel using GridBagLayout
		this.add(mainPanel, gbc);

		mainPanel.revalidate();
		mainPanel.repaint();

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
							buttonGrid[box][row][col].setIcon(icons[3]); // Greg icon
							buttonGrid[box][row][col].setDisabledIcon(icons[3]);
							break;
						case 4:
							buttonGrid[box][row][col].setIcon(icons[4]); // Death icon
							buttonGrid[box][row][col].setDisabledIcon(icons[4]);
							break;
						case 5:
							buttonGrid[box][row][col].setIcon(icons[5]); // Agni icon
							buttonGrid[box][row][col].setDisabledIcon(icons[5]);
							break;
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
                announceWinner(boxCompleted[pattern[0]]);
				System.out.println("BIG");
                return;
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
		ImageIcon originalEmpty = new ImageIcon("empty.png");
		ImageIcon originalEmptyLocked = new ImageIcon("emptyLocked.png");
		ImageIcon originalEmptyHover = new ImageIcon("emptyHover.png");
		ImageIcon originalCross = new ImageIcon("cross.png");
		ImageIcon originalCircle = new ImageIcon("circle.png");
		ImageIcon originalGreg = new ImageIcon("greggman.png");
		ImageIcon originalDeath = new ImageIcon("death.png");
		ImageIcon originalAgni = new ImageIcon("agni.png");

		// Scale images
		Image scaledCross = originalCross.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledCrossIcon = new ImageIcon(scaledCross);

		Image scaledCircle = originalCircle.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledCircleIcon = new ImageIcon(scaledCircle);

		Image scaledGreg = originalGreg.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledGregIcon = new ImageIcon(scaledGreg);
		
		Image scaledDeath = originalDeath.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledDeathIcon = new ImageIcon(scaledDeath);
		
		Image scaledAgni = originalAgni.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledAgniIcon = new ImageIcon(scaledAgni);

		Image scaledEmpty = originalEmpty.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledEmptyIcon = new ImageIcon(scaledEmpty);

		Image scaledEmptyLocked = originalEmptyLocked.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledEmptyLockedIcon = new ImageIcon(scaledEmptyLocked);

		Image scaledEmptyHover = originalEmptyHover.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledEmptyHoverIcon = new ImageIcon(scaledEmptyHover);

		// Return both icons as an array
		return new ImageIcon[] {scaledEmptyIcon, scaledCrossIcon, scaledCircleIcon, scaledGregIcon, scaledDeathIcon, scaledAgniIcon, null, null , scaledEmptyLockedIcon, scaledEmptyHoverIcon};
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
	            // Adds an additional turn for the player
	            round++;
	            break;
	        case 4:
	            System.out.println("Power-up Type 4 collected!");
	            
	            break;
	        case 5:
	            System.out.println("Power-up Type 5 collected!");
	            
	            break;
	        default:
	            break;
	    }
	}

	// Main method to launch the application
	public static void main(String[] args) {
		new MyFrame();
	}
}
