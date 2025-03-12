import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends JFrame {

    private JPanel panel;
    private List<FoodEntry> foodList = new ArrayList<>();
    private List<ExerciseEntry> exerciseLogs = new ArrayList<>();
    private Image backgroundImage;

    public HomeScreen() {
        // Load background image
        backgroundImage = new ImageIcon("Picture3.png").getImage(); // Make sure the image exists

        // Setup JFrame
        setTitle("Calories Tracking App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel with a background image
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Create a welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the Calories Tracking App!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.BLACK);
        panel.add(welcomeLabel, gbc);

        // Create buttons
        JButton foodLogButton = createStyledButton("Food Log");
        JButton exerciseLogButton = createStyledButton("Exercise Log");
        JButton progressButton = createStyledButton("Progress");

        // Action listeners
        foodLogButton.addActionListener(e -> {
            new FoodLogScreen().setVisible(true);
            dispose();
        });

        exerciseLogButton.addActionListener(e -> {
            new ExerciseLogScreen().setVisible(true);
            dispose();
        });

        progressButton.addActionListener(e -> {
            new ProgressScreen().setVisible(true);
            dispose();
        });

        // Add buttons to the panel
        gbc.gridy++;
        panel.add(foodLogButton, gbc);
        gbc.gridy++;
        panel.add(exerciseLogButton, gbc);
        gbc.gridy++;
        panel.add(progressButton, gbc);

        // Add panel to JFrame
        add(panel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(255, 200, 150)); // Light orange
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomeScreen::new);
    }
}
