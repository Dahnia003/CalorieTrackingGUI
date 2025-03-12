import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;

/**
 *Dahnia Belizaire
 *CEN 3024C- Software Developement 1
 * March 12, 2025
 * Name: FoodLogScreen
 * This class represents a food item/meal logging system where users can:
 * 1. Add food entries manually or from a file.
 * 2. Remove or update a food entry.
 * 3. Display all food entries.
 * 4. Navigate to the exercise screen or stay on the food screen after a command.
 * 5. Go back to Home Screen
 *  The objective of this class is to track user food items/ meals and manage the food log.
 */

public class FoodLogScreen extends JFrame {
    private JPanel panel;
    private JTextArea foodLogTextArea;
    public static List<FoodEntry> foodList;
    private Image backgroundImage;

    /**
     * Name: FoodLogScreen
     * Purpose:Display exercise GUI with different button options
     *         and proceed accordingly with user choice
     * Arguments:None
     * return value: None
     */
    public FoodLogScreen() {
        backgroundImage = new ImageIcon("Picture1.jpg").getImage(); // Make sure the image exists

        foodList = new ArrayList<>();
        setTitle("Food Log Screen");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set background image
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
        JLabel welcomeLabel = new JLabel("Food Log Screen");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.BLACK);
        panel.add(welcomeLabel, gbc);

        JButton addButton = createStyledButton("Add Food");
        JButton removeButton = createStyledButton("Remove Food");
        JButton updateButton = createStyledButton("Update Food");
        JButton displayButton = createStyledButton("Display All ");
        JButton backToHomeButton = createStyledButton("Back to Home");


        addButton.addActionListener(e -> showAddFoodOptions());
        removeButton.addActionListener(e -> removeFoodEntry());
        updateButton.addActionListener(e -> updateFoodEntry());
        displayButton.addActionListener(e -> displayFoodLog());
        backToHomeButton.addActionListener(e -> {
            new HomeScreen();
            dispose();
        });

        gbc.gridy++;
        panel.add(addButton, gbc);
        gbc.gridy++;
        panel.add(removeButton, gbc);
        gbc.gridy++;
        panel.add(updateButton, gbc);
        gbc.gridy++;
        panel.add(displayButton, gbc);
        gbc.gridy++;
        panel.add(backToHomeButton, gbc);
        // Initialize the food log JTextArea
        foodLogTextArea = new JTextArea(10, 40); // Adjust size as needed
        foodLogTextArea.setEditable(false); // Make it read-only
        JScrollPane scrollPane = new JScrollPane(foodLogTextArea);
        gbc.gridy++;
        panel.add(scrollPane, gbc);
        // Add panel to JFrame
        add(panel);

        setVisible(true);
    }
/**
 * Method Name: createStyledButton
 * Purpose: Set the format for the buttons
 * Arguments: String text
 * Return value: JButton
 */

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
     * Method Name: showAddFoodOptions
     * Purpose: Upon clicking add button, it prompt the user to chose
     *          to add either manually or from a text file
     * Arguments: nond
     * Return value: void
     */
    private void showAddFoodOptions() {
        String[] options = {"Manually", "From File"};
        int choice = JOptionPane.showOptionDialog(
                this, "How do you want to add food?", "Choose Input Method",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]
        );
        if (choice == 0) enterFoodManually();
        else if (choice == 1) addFoodFromFile();
    }
    /**
     * Name: enterFoodManually
     * Purpose:Add a food entry manually
     * Arguments:None
     * return value: FoodEntry
     */

    private FoodEntry enterFoodManually() {
        String foodID = validateFoodID();

        String foodName;
        while (true) {
            foodName = JOptionPane.showInputDialog("Enter Food Name:");
            if (foodName == null || foodName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Food name cannot be empty.");
                continue;
            }
            break;
        }

        float calories = getValidFloat("Enter Calories:");
        float carbs = getValidFloat("Enter Carbs (grams):");
        float fat = getValidFloat("Enter Fat (grams):");
        float protein = getValidFloat("Enter Protein (grams):");

        String[] mealOptions = {"Breakfast", "Lunch", "Dinner", "Snack", "Other"};
        String mealType = (String) JOptionPane.showInputDialog(this, "Select Meal Type:", "Meal Type", JOptionPane.QUESTION_MESSAGE, null, mealOptions, mealOptions[0]);
        if (mealType == null) return null;

        LocalDate mealDate = validateAndParseDate();

        userChoice();
        return new FoodEntry(foodID, foodName, calories, carbs, protein, fat, mealType, mealDate);
    }

    /**
     * Name: validateAndParseDate
     * Purpose:Validate date for enterFoodManually
     * Arguments:None
     * return value: LocalDate
     */
    private LocalDate validateAndParseDate() {
        while (true) {
            String dateStr = JOptionPane.showInputDialog(this, "Enter new date (MM/dd/yyyy):");
            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please enter in MM/dd/yyyy format.");
            }
        }
    }
    /**
     * Name: validateFoodID
     * Purpose:Validate ID for addFoodFromFile
     * Arguments:None
     * return value: boolean
     */

    private String validateFoodID() {
        while (true) {
            String foodID = JOptionPane.showInputDialog("Enter a Food ID (F0000000):");
            if (foodID == null || foodID.trim().isEmpty() || !foodID.matches("^F\\d{7}")) {
                JOptionPane.showMessageDialog(this, "Invalid ID! Please enter an alphanumeric ID with exactly 7 digits (starting with 'F').");
                continue;
            }
            boolean idExists = foodList.stream().anyMatch(food -> food.getFoodID().equals(foodID));
            if (idExists) {
                JOptionPane.showMessageDialog(this, "Food ID already exists. Please enter a unique Food ID.");
                continue;
            }
            return foodID;
        }
    }

    /**
     * Name: getValidFloat
     * Purpose:Validate calories, carbs, protein, and fat are float values
     * Arguments:Scanner scanner, String prompt
     * return value: float
     */

    private float getValidFloat(String prompt) {
        while (true) {
            String input = JOptionPane.showInputDialog(prompt);
            try {
                return Float.parseFloat(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid entry! Please enter a valid float.");
            }
        }
    }
//-------------------------------------------------------------
// ---------------------------------------------------------------------
    /**
     * Name: isValidFoodID
     * Purpose:Validate ID for addFoodFromFile
     * Arguments:String foodID
     * return value: boolean
     */

    private boolean isValidFoodID(String foodID) {
        // Check if the exercise ID matches the required pattern (e.g., E0000000)
        if (!foodID.matches("^F\\d{7}$")) {
            JOptionPane.showMessageDialog(this, "Error: Invalid Exercise ID format! It must follow the format E0000000.");
            return false;  // Invalid format
        }
        // Check if the exercise ID already exists in the list
        for (FoodEntry food : foodList) {
            if (food.getFoodID().equalsIgnoreCase(foodID)) {
                JOptionPane.showMessageDialog(this, "Error: Food ID " + foodID + " already exists. Please use a unique ID.");
                return false;  // ID already exists in the list
            }
        }
        // ID is valid and does not already exist
        return true;
    }
    /**
     * Name: addFoodFromFile
     * Purpose: Enable user to add food entry from loading a file
     * Arguments:None
     * return value: List<FoodEntry>
     */
    public List<FoodEntry> addFoodFromFile() {
        String fileName = JOptionPane.showInputDialog(this, "Enter the file name to load food entries:");

        // If the user cancels the input or provides an empty string, return early
        if (fileName == null || fileName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "File name cannot be empty. No file loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            return foodList; // Return the current food list without modifications
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                String[] data = line.split("-");
                if (data.length == 8) {
                    String foodID = data[0].trim();
                    String foodName = data[1].trim();
                    float calories = Float.parseFloat(data[2].trim());
                    float carbs= Float.parseFloat(data[3].trim());
                    float protein = Float.parseFloat(data[4].trim());
                    float fat = Float.parseFloat(data[5].trim());
                    String mealType = data[6].trim();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    LocalDate date = LocalDate.parse(data [7]. trim(), dateFormatter);

                    // Validate fields using the new methods
                    if (!isValidFoodID(foodID)) {
                        continue;  // Skip this entry if the ID is invalid or already exists
                    }

                    if (!isValidMealType(mealType)) {
                        System.out.println("Skipping invalid meal type: " + mealType);
                        continue;  // Skip this entry if the intensity is invalid
                    }
                    if (!isValidDate(data[7].trim())) {
                        System.out.println("Skipping invalid date: " + date);
                        continue;  // Skip this entry if the date is invalid
                    }

                // If all validations pass, create a new FoodEntry and add it to the list


                FoodEntry foodEntry = new FoodEntry(foodID, foodName, calories, carbs, protein, fat, mealType, date);
                foodList.add(foodEntry);

            }else {
                    JOptionPane.showMessageDialog(this,"Skipping invalid line (incorrect number of fields): " + line);
                }
            } displayFoodLog();
            userChoice();


        }catch (IOException e) {
            JOptionPane.showMessageDialog(this,"Error reading file: " + e.getMessage());
        }
        return foodList;
    }



    // Example validation function for ID format
    /**
     * Name: isValidDate
     * Purpose:Validate date format
     * Arguments:String dateString
     * return value: boolean
     */
        private boolean isValidDate(String dateString) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            try {
                LocalDate date = LocalDate.parse(dateString, formatter);
                return true;  // Valid date
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format: " + dateString);
                return false;
            }
        }

    /**
     * Name: isValidMealType
     * Purpose:Validate proper entries of meal type in addFoodFromFile
     * Arguments:String mealType
     * return value: boolean
     */

    private boolean isValidMealType(String mealType) {
        List<String> validMealTypes = List.of("Breakfast", "Lunch", "Dinner", "Snack", "Other");
        return validMealTypes.contains(mealType);
    }
//----------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Name: removeFoodEntry
     * Purpose: Enable user to remove food entry
     * Arguments:None
     * return value: boolean
     */

    public boolean removeFoodEntry() {
        // Ask the user to input the Food ID to remove using JOptionPane or Scanner
        String foodID = JOptionPane.showInputDialog("Enter the Food ID to remove:").trim();

        // If the user cancels the dialog, return false
        if (foodID == null || foodID.isEmpty()) {
            JOptionPane.showMessageDialog(this,"No Food ID entered. Operation canceled.");
            return false; // Return false if no ID is provided or the user cancels
        }

        Iterator<FoodEntry> iterator = foodList.iterator();
        boolean removed = false;

        // Iterate through the list and find the matching food entry
        while (iterator.hasNext()) {
            FoodEntry food = iterator.next();
            if (food.getFoodID().trim().equalsIgnoreCase(foodID)) {
                iterator.remove();  // Remove the food entry using the iterator
                removed = true;
                break;  // Exit the loop once the food entry is removed
            }
        }

        if (removed) {
            JOptionPane.showMessageDialog(this,"Food entry removed successfully.");
        } else {
            JOptionPane.showMessageDialog(this,"Food ID not found.");
        }

        return removed;
    }
//-----------------------------------------------------------------------------
//--------------------------------------------------------------------------------------

    /**
     * Name: updateFoodEntry
     * Purpose: Enable user to update a food entry
     * Arguments:None
     * return value: FoodEntry
     */
    public FoodEntry updateFoodEntry() {
        String foodID = JOptionPane.showInputDialog(this, "Enter the Food ID to update (e.g., F000000):");

        if (foodID == null || foodID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Food ID is invalid or empty. Update failed.");
            return null;  // Return null if the ID is invalid
        }

        Iterator<FoodEntry> iterator = foodList.iterator();
        FoodEntry foodToUpdate = null;

        // Iterate through the list to find the matching FoodEntry
        while (iterator.hasNext()) {
            FoodEntry food = iterator.next();
            if (food.getFoodID().trim().equals(foodID)) {
                foodToUpdate = food;
                break;  // Stop the loop once the food entry is found
            }
        }

        // Search for the food entry in the list
        if (foodToUpdate == null) {
            JOptionPane.showMessageDialog(this, "Food ID not found. Update failed.");
            return null;  // Return null if the food entry is not found
        }

        // Display the food entry for the user to review
        String foodDetails = "Food entry found:\n" +
                foodToUpdate.getFoodID() + "- " + foodToUpdate.getFoodName() + "- " +
                foodToUpdate.getCalories() + "- " + foodToUpdate.getCarbs() + "- " +
                foodToUpdate.getProtein() + "- " + foodToUpdate.getFat() + "- " +
                foodToUpdate.getMealType() + "- " + foodToUpdate.getMealDate();

        JOptionPane.showMessageDialog(this, foodDetails);

        // Provide the user with update options
        String[] options = {"Name", "Calories", "Carbs", "Fat", "Protein", "Meal Type", "Date"};
        int updateChoice = JOptionPane.showOptionDialog(this, "Which field would you like to update?",
                "Update Field", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (updateChoice == JOptionPane.CLOSED_OPTION) {
            return null;  // Return null if the user cancels
        }

        switch (updateChoice) {
            case 0:  // Name
                String newName = JOptionPane.showInputDialog(this, "Enter new food name:");
                if (newName != null && !newName.trim().isEmpty()) {
                    foodToUpdate.setFoodName(newName);
                } else {
                    JOptionPane.showMessageDialog(this, "Food name cannot be empty.");
                    return null;
                }
                break;

            case 1:  // Calories
                float newCalories = getValidFloat("Enter new calories:");
                foodToUpdate.setCalories(newCalories);
                break;

            case 2:  // Carbs
                float newCarbs = getValidFloat("Enter new carbs (in grams):");
                foodToUpdate.setCarbs(newCarbs);
                break;

            case 3:  // Fat
                float newFat = getValidFloat("Enter new fat (in grams):");
                foodToUpdate.setFat(newFat);
                break;

            case 4:  // Protein
                float newProtein = getValidFloat("Enter new protein (in grams):");
                foodToUpdate.setProtein(newProtein);
                break;

            case 5:  // Meal Type
                String[] mealOptions = {"Breakfast", "Lunch", "Dinner", "Snack", "Other"};
                String newMealType = (String) JOptionPane.showInputDialog(this, "Select Meal Type:", "Meal Type", JOptionPane.QUESTION_MESSAGE, null, mealOptions, mealOptions[0]);
                if (newMealType == null) return null;
                foodToUpdate.setMealType(newMealType);
                break;

            case 6:  // Date
                LocalDate newDate = validateAndParseDate();
                    foodToUpdate.setMealDate(newDate);

                break;

            default:
                JOptionPane.showMessageDialog(this, "Invalid option. Update failed.");
                return null;
        }

        JOptionPane.showMessageDialog(this, "Food entry updated successfully!");
        displayFoodLog();
        return foodToUpdate;
    }
    /**
     * Name: displayFoodLog
     * Purpose: Enable user to display all food entries
     * Arguments:None
     * return value: String
     */

    private void displayFoodLog() {
        StringBuilder logContent = new StringBuilder();

        if (foodList.isEmpty()) {
            logContent.append("No food entries available.\n");
        } else {
            logContent.append("Food Log:\n");
            for (FoodEntry food : foodList) {
                logContent.append(food).append("\n");  // Appending food entry to the log content
            }
        }

        // Display the log content in the JTextArea (GUI component)
        JOptionPane.showMessageDialog(this, logContent.toString(), "Food Log Entries", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Name: userChoice
     *Purpose: Ask user if they want to stay on current screen
     *        or move to the next
     *Arguments:None
     * Return Value: void
     */
    private void userChoice() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to move to the Exercise screen?", "Navigation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            new ExerciseLogScreen();
            dispose();
        }
    }

    /**
     * Name: calculateTotalCaloriesBurnedByDate
     * Purpose: Calculate total calories burned by each date for food entries
     * Arguments:none
     * return value: FoodEntry
     */

    public static Map<LocalDate, Float> calculateTotalCaloriesIntakeByDate() {
        // Create a map to store total calories burned by each date
        Map<LocalDate, Float> caloriesByDate = new HashMap<>();

        // Loop through each food entry in the list
        for (FoodEntry foodEntry : foodList) {
            // Get the meal date and calories from the food entry
            LocalDate date = foodEntry.getMealDate();
            float calories = foodEntry.getCalories();

            // Add the calories to the corresponding date in the map
            caloriesByDate.put(date, caloriesByDate.getOrDefault(date, 0f) + calories);
        }

        // Format the output to display in a table-like format
        StringBuilder result = new StringBuilder();
        result.append("Date\t\t\tTotal Calories Intake\n");
        result.append("--------------------------------------------------\n");

        for (Map.Entry<LocalDate, Float> entry : caloriesByDate.entrySet()) {
            // Format the date and calories for display
            result.append(String.format("%s\t\t%.2f\n", entry.getKey(), entry.getValue()));
        }

        // Display the results in a JOptionPane
        JOptionPane.showMessageDialog(null, result.toString(), "Total Calories Burned by Date", JOptionPane.INFORMATION_MESSAGE);

        // Return the map for further use if necessary
        return caloriesByDate;
    }
    /**
     * Method Name: main
     * Purpose: This is the entry point of the class.
     *           It initializes `FoodLogScreen` object and calls
     *           the run` method to display the options for the user.
     * Arguments: String[] args
     * Return Value: void
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FoodLogScreen frame = new FoodLogScreen();
            frame.setVisible(true); // Make JFrame visible
        });
    }

}
