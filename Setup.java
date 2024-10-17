import javax.swing.*;
import java.awt.*;

public class Setup {

    public static JPanel createGridOfButtons() {
        // Create a JPanel with GridLayout for 9 rows and 9 columns
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 9)); // 9x9 grid

        // Add 81 buttons to the panel
        for (int i = 1; i <= 81; i++) {
            JButton button = new JButton(String.valueOf(i)); // Create a button with text
            panel.add(button); // Add the button to the panel
        }

        return panel; // Return the panel containing the buttons
    }

}
