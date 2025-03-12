import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Map;
/**
 *Dahnia Belizaire
 *CEN 3024C- Software Developement 1
 * March 12, 2025
 * Name: ProgressScreen
 * This class represents a progress tracking system where users can:
 * 1. See total calories intake
 * 2. See total calories expenditure
 * 4. Navigate back to other screens
 */


public class ProgressScreen extends JFrame {
    private FoodLogScreen foodLogScreen;
    private ExerciseLogScreen exerciseLogScreen;
    private HomeScreen homeScreen;
    private JPanel panel;
    private JTextArea calorieTextArea;
    private Image backgroundImage;

    // Constructor to initialize the screens and setup the GUI
    public ProgressScreen() {
        this.foodLogScreen = new FoodLogScreen(); // Initialize with the FoodLogScreen
        this.exerciseLogScreen = new ExerciseLogScreen(); // Initialize with the ExerciseLogScreen
        this.homeScreen = new HomeScreen(); // Home screen initialization

        // Load the background image
        backgroundImage = new ImageIcon("Picture5.jpg").getImage(); // Provide the path to your image here

        // Setup JFrame
        setTitle("Progress Screen");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel to hold components
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image, stretching it to the size of the panel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create a JTextArea to display calorie information
        StringBuilder calorieInfo = new StringBuilder();

        // Get calories burned and intake by date (this part should be adapted to your actual logic)
        Map<LocalDate, Float> totalCaloriesIntakeByDate = FoodLogScreen.calculateTotalCaloriesIntakeByDate();
        Map<LocalDate, Float> totalCaloriesBurnedByDate = ExerciseLogScreen.calculateTotalCaloriesBurnedByDate();

        // Append calorie intake information to the JTextArea
        calorieInfo.append("Calories Intake by Date:\n");
        for (Map.Entry<LocalDate, Float> entry : totalCaloriesIntakeByDate.entrySet()) {
            calorieInfo.append(entry.getKey())  // Date
                    .append(" - ")
                    .append("Total Calories Intake: ")
                    .append(entry.getValue())  // Total calories
                    .append("\n");
        }

        // Now that the progress screen only needs to display the calories burned info
        // It will just call the relevant method to handle it, and display it.
        calorieInfo.append("\nCalories Burned by Date:\n");
        for (Map.Entry<LocalDate, Float> entry : totalCaloriesBurnedByDate.entrySet()) {
            calorieInfo.append(entry.getKey())  // Date
                    .append(" - ")
                    .append("Total Calories Burned: ")
                    .append(entry.getValue())  // Total calories
                    .append("\n");
        }

        // Set up the JTextArea with the calories data
        calorieTextArea = new JTextArea(calorieInfo.toString());
        calorieTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        calorieTextArea.setEditable(false); // Make the text area non-editable
        calorieTextArea.setBackground(Color.WHITE); // Solid white background
        calorieTextArea.setWrapStyleWord(true);
        calorieTextArea.setLineWrap(true);
        calorieTextArea.setCaretPosition(0); // Move the caret to the top of the text

        // Make the JTextArea scrollable
        JScrollPane scrollPane = new JScrollPane(calorieTextArea);
        scrollPane.setPreferredSize(new Dimension(500, 150));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(scrollPane); // Add the JScrollPane with JTextArea to the panel

        // Create buttons for navigation
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton homeButton = createStyledButton("Home");
        JButton foodLogButton = createStyledButton("Food Log");
        JButton exerciseLogButton = createStyledButton("Exercise Log");
        JButton exitButton = createStyledButton("Exit");

        // Action listeners for buttons
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomeScreen().setVisible(true); // Open HomeScreen
                dispose(); // Close the current screen
            }
        });

        foodLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FoodLogScreen().setVisible(true); // Open FoodLogScreen
                dispose(); // Close the current screen
            }
        });

        exerciseLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ExerciseLogScreen().setVisible(true); // Open ExerciseLogScreen
                dispose(); // Close the current screen
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Exiting system. Goodbye!");
                System.exit(0); // Exit the application
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(homeButton);
        buttonPanel.add(foodLogButton);
        buttonPanel.add(exerciseLogButton);
        buttonPanel.add(exitButton);

        // Add the button panel at the bottom of the main panel
        panel.add(Box.createVerticalStrut(20)); // Add some space between labels and buttons
        panel.add(buttonPanel);

        // Add the main panel to the frame
        add(panel);
        setVisible(true);
    }
    /**
     * Method Name: createStyledButton
     * Purpose: Set the format for the buttons
     * Arguments: String text
     * Return value: JButton
     */
    // Create a styled button
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(255, 200, 150)); // Light orange
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }
    /**
     * Method Name: main
     * Purpose: This is the entry point of the class.
     *           It initializes `ProgressScreen` object and
     *           calls the `displayProgress` method to display the options for the user.
     * Arguments: String[] args
     * Return Value: void
     */
    // Main method to launch ProgressScreen
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProgressScreen().setVisible(true));
    }
}
