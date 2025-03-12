

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;
/**
 *Dahnia Belizaire
 *CEN 3024C- Software Developement 1
 * March 12, 2025
 * Name: ExerciseLogScreen
 * This class represents an exercise logging system where users can:
 * 1. Add exercise entries manually or from a file.
 * 2. Remove or update an exercise entry.
 * 3. Display all exercise entries.
 * 4. Navigate to a progress screen or stay on the exercise screen.
 *  The objective of this class is to track user exercises and manage the exercise log.
 */

public class ExerciseLogScreen extends JFrame {
    private JPanel panel;
    public static List<ExerciseEntry> exerciseList;
    private Image backgroundImage;

    /**
     * Name: ExerciseLogScreen
     * Purpose:Display exercise GUI with different button options
     *         and proceed accordingly with user choice
     * Arguments:None
     * return value: None
     */
    public ExerciseLogScreen() {
        backgroundImage = new ImageIcon("Picture1.jpg").getImage(); // Make sure the image exists

        exerciseList = new ArrayList<>();
        setTitle("Exercise Log Screen");
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
        JLabel welcomeLabel = new JLabel("Exercise Log Screen");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.BLACK);
        panel.add(welcomeLabel, gbc);

        JButton addButton = createStyledButton("Add Exercise");
        JButton removeButton = createStyledButton("Remove Exercise");
        JButton updateButton = createStyledButton("Update Exercise");
        JButton displayButton = createStyledButton("Display All ");
        JButton backToHomeButton = createStyledButton("Back to Home");


        addButton.addActionListener(e -> showAddExerciseOptions());
        removeButton.addActionListener(e -> removeExercise());
        updateButton.addActionListener(e -> updateExercise());
        displayButton.addActionListener(e -> displayExerciseLog());
        backToHomeButton.addActionListener(e -> goToHomeScreen());

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
     * Method Name: showAddExerciseOptions
     * Purpose: Upon clicking add button, it prompt the user to chose
     *          to add either manually or from a text file
     * Arguments: nond
     * Return value: void
     */

    private void showAddExerciseOptions() {
        String[] options = {"Manually", "From File"};
        int choice = JOptionPane.showOptionDialog(
                this, "How do you want to add food?", "Choose Input Method",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]
        );
        if (choice == 0) addExerciseManually();
        else if (choice == 1) addExerciseFromFile();
    }

    /**-------------------------------------------------------------------------------------------------------------
     * -------------------------------------------------------------------------------------------------------------
     */

    /**
     * Name: addExerciseManually
     * Purpose:Add an exercise entry manually
     * Arguments:None
     * return value: ExerciseEntry
     */

    private ExerciseEntry addExerciseManually() {
        String exerciseID= validateExerciseID();
        String exerciseType;
        while (true) {
            exerciseType = JOptionPane.showInputDialog("Enter Exercise Name:");
            if (exerciseType == null || exerciseType.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Exercise type cannot be empty.");
                continue;
            }
            break;
        }
        String exerciseName;
        while (true) {
            exerciseName = JOptionPane.showInputDialog("Enter Exercise Name:");
            if (exerciseName == null || exerciseName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Exercise name cannot be empty.");
                continue;
            }
            break;
        }
        int duration= getValidInt();

        String[] intensityOptions = {"Low", "Medium", "High", "Mixed"};
        String intensity = (String) JOptionPane.showInputDialog(this, "Select Intensity Type:",
                "Intensity Type", JOptionPane.QUESTION_MESSAGE, null, intensityOptions, intensityOptions[0]);
        if (intensity == null) return null;

        LocalDate exerciseDate= validateAndParseDate();
        LocalTime exerciseTime= validateAndParseTime();
        float calBurned= calculateCaloriesBurned(intensity, duration);
        userChoice2();
        return new ExerciseEntry(exerciseID, exerciseType, exerciseName, duration, intensity, exerciseDate, exerciseTime, calBurned );
    }
    /**
     * Name: validateAndParseDate
     * Purpose:Validate exercise date in addExerciseManually method
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
     * Name: validateAndParseTime
     * Purpose:Validate exercise time in addExerciseManually method
     * Arguments:None
     * return value: LocalTime
     */
    private LocalTime validateAndParseTime() {
        while (true) {
            // Prompt the user for time input
            String timeStr = JOptionPane.showInputDialog(this, "Enter time (hh:mm a):");

            // Check if the user clicked cancel
            if (timeStr == null) {
                return null;  // Return null to stop the process if user clicks cancel
            }

            // Trim spaces and check if the input is empty
            timeStr = timeStr.trim();
            if (timeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Time cannot be empty.");
                continue;  // Re-prompt for input if time is empty
            }

            try {
                // Use a DateTimeFormatter for 12-hour format with AM/PM
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

                // Parse the time string using the formatter
                return LocalTime.parse(timeStr, formatter);
            } catch (DateTimeParseException e) {
                // If parsing fails, show an error message and prompt again
                JOptionPane.showMessageDialog(this, "Invalid time format. Please enter a valid time in hh:mm a format.");
            }
        }
    }
    /**
     * Name: validateExerciseID
     * Purpose: valid ID for addExerciseFromFile method
     * Arguments:None
     * return value:String
     */

    private String validateExerciseID() {
        while (true) {
            String exerciseID = JOptionPane.showInputDialog("Enter an Exercise ID (E0000000):");
            if (exerciseID == null ){
                return null;
            }
            if (exerciseID.trim().isEmpty() || !exerciseID.matches("^E\\d{7}")) {
                JOptionPane.showMessageDialog(this,  "Invalid ID! Please enter an alphanumeric ID with exactly 7 digits (starting with 'E').");
                continue;
            }
            boolean idExists = exerciseList.stream().anyMatch(exercise -> exercise.getExerciseID().equals(exerciseID));
            if (idExists) {
                JOptionPane.showMessageDialog(this, "Exercise ID already exists. Please enter a unique Food ID.");
                continue;
            }
            return exerciseID;
        }
    }/**
     * Name: getValidInt
     * Purpose:Validate duration is int upon entry in addExerciseManually method
     * Arguments:Scanner scanner, Sting prompt
     * return value: int
     */
    private int getValidInt() {
        while (true) {
           String duration = JOptionPane.showInputDialog("Enter Duration (In minutes):");
            if (duration == null || duration.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Field cannot be empty: ");
                continue;
            }
            try {
                return Integer.parseInt(duration);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please enter a valid number.");
            }
        }
    }

    /**-------------------------------------------------------------------------------------------------------------
     * -------------------------------------------------------------------------------------------------------------
     */

    /**
     * Name: addExerciseFromFile
     * Purpose: enable user to add entries by uploading a file
     * Arguments:None
     * return value: List<ExerciseEntry>
     */

    public List<ExerciseEntry> addExerciseFromFile() {
        String fileName = JOptionPane.showInputDialog(this,"Enter the file name to load exercises: ");

        if (fileName == null || fileName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "File name cannot be empty. No file loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            return exerciseList; // Return the current food list without modifications
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {

                    if (line.trim().isEmpty()){
                        continue;
                    }

                    String[] fields = line.split("-");
                    if (fields.length == 7) {
                        String exerciseID= fields[0].trim();
                        String type = fields[1].trim();
                        String name = fields[2].trim();
                        int duration = Integer.parseInt(fields[3].trim());
                        String intensity = fields[4].trim();

                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        LocalDate date = LocalDate.parse(fields [5]. trim(), dateFormatter);

                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
                        LocalTime time = LocalTime.parse(fields[6].trim(), timeFormatter);

                        if (!isValidExerciseID(exerciseID)) {
                            continue;
                        }
                        if (!isValidInt(duration)){
                            continue;
                        }
                        if (!isValidIntensity(intensity)) {
                            continue;
                        }
                        if (!isValidTime( fields[6].trim())){
                            continue;
                        }
                        if (!isValidDate( fields[5].trim())){
                            continue;
                        }


                        float caloriesBurned = calculateCaloriesBurned(intensity, duration);

                        ExerciseEntry exerciseEntry = new ExerciseEntry(exerciseID, type, name, duration, intensity, date, time, caloriesBurned);
                        exerciseList.add(exerciseEntry);
                    } else {
                        JOptionPane.showMessageDialog(this,"Skipping invalid line (incorrect number of fields): " + line);
                    }
                } displayExerciseLog();
                userChoice2();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading the file.");
            }
        return exerciseList;
    }

    /**
     * Name: isValidExerciseID
     * Purpose: valid ID for addExerciseFromFile method
     * Arguments:String exerciseID
     * return value: boolean
     */
    private boolean isValidExerciseID(String exerciseID) {
        // Check if the exercise ID matches the required pattern (e.g., E0000000)
        if (!exerciseID.matches("^E\\d{7}$")) {
            JOptionPane.showMessageDialog(this,"Error: Invalid Exercise ID format! It must follow the format E0000000.");
            return false;  // Invalid format
        }
        // Check if the exercise ID already exists in the list
        for (ExerciseEntry exercise : exerciseList) {
            if (exercise.getExerciseID().equalsIgnoreCase(exerciseID)) {
                JOptionPane.showMessageDialog(this,"Error: Exercise ID " + exerciseID + " already exists. Please use a unique ID.");
                return false;  // ID already exists in the list
            }
        }
        // ID is valid and does not already exist
        return true;
    }
    /**
     * Name: isValidDate
     * Purpose: ensure valid date is entered for addExerciseFromFile method
     * Arguments:String dateString
     * return value: boolean
     */
    private boolean isValidDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            return true;  // Valid date
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + dateString);
            return false;
        }
    }
    /**
     * Name: isValidTime
     * Purpose: ensure valid time is entered for addExerciseFromFile method
     * Arguments:String timeString
     * return value: boolean
     */
    private boolean isValidTime(String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        try {
            LocalTime time = LocalTime.parse(timeString, formatter);
            return true;  // Valid time
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format: " + timeString);
            return false;
        }
    }
    /**
     * Name: isValidIntensity
     * Purpose: valid intensity for addExerciseFromFile method
     * Arguments:String intensity
     * return value: boolean
     */

    private boolean isValidIntensity(String intensity) {
        List<String> validIntensities = List.of("Low", "Medium", "High", "Mixed");
        return validIntensities.contains(intensity);  // Check if intensity is valid
    }
    /**
     * Name: isValidInt
     * Purpose: validate duration for addExerciseFromFile method
     * Arguments:int duration
     * return value: boolean
     */
    private boolean isValidInt(int duration) {
        return duration > 0;  // Duration must be positive
    }

    /**-------------------------------------------------------------------------------------------------------------
     * -------------------------------------------------------------------------------------------------------------
     */
    /**
     * Name: removeExercise
     * Purpose: Enable user to remove an entry successfully
     * Arguments:NOne
     * return value: boolean
     */

    private boolean removeExercise() {
        String exerciseID = JOptionPane.showInputDialog("Enter Exercise ID to Remove:");
        Iterator<ExerciseEntry> iterator = exerciseList.iterator();
        boolean removed = false;

        // Iterate over the list
        while (iterator.hasNext()) {
            ExerciseEntry exercise = iterator.next();

            // Compare the exercise ID
            if (exercise.getExerciseID().equals(exerciseID)) {
                iterator.remove();  // Remove the exercise entry from the list
                removed = true;
                JOptionPane.showMessageDialog(this,"Exercise entry removed successfully.");
                break;  // Exit the loop once the exercise is removed
            }
        }

        // If no match is found, print an error message
        if (!removed) {
            JOptionPane.showMessageDialog(this,"Exercise ID not found.");
        }

        return removed;
    }
    /**
     * Name: updateExerciseEntry
     * Purpose: Enable user to update an entry successfully
     * Arguments:Scanner scanner
     * return value: ExerciseEntry
     */

    private ExerciseEntry updateExercise() {
        String exerciseId = JOptionPane.showInputDialog(this, "Enter Exercise ID to Update:");

        if (exerciseId == null || exerciseId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Exercise ID is invalid or empty. Update failed.");
            return null;
        }

        Iterator<ExerciseEntry> iterator= exerciseList.iterator();
        ExerciseEntry exerciseToUpdate= null;

        while (iterator.hasNext()) {
            ExerciseEntry exercise = iterator.next();
            if (exercise.getExerciseID().trim().equals(exerciseId)) {
                exerciseToUpdate = exercise;
                break;
            }
        }

        if (exerciseToUpdate == null) {
            JOptionPane.showMessageDialog(this, "Exercise ID not found.");
            return null;  // Return null if the exercise ID is not found
        }

        // Display the current exercise details to the user for review
        String exerciseDetails = "Exercise ID: " + exerciseToUpdate.getExerciseID() + "\n" +
                "Exercise Type: " + exerciseToUpdate.getExerciseType() + "\n" +
                "Exercise Name: " + exerciseToUpdate.getExerciseName() + "\n" +
                "Duration: " + exerciseToUpdate.getDuration() + " minutes\n" +
                "Intensity: " + exerciseToUpdate.getIntensity() + "\n" +
                "Date: " + exerciseToUpdate.getExerciseDate() + "\n" +
                "Time: " + exerciseToUpdate.getExerciseTime() + "\n" +
                "Calories Burned: " + exerciseToUpdate.getCalBurned();

        JOptionPane.showMessageDialog(this, exerciseDetails);

        // Provide the user with update options (similar to the food entry update)
        String[] options = {"Exercise Type", "Exercise Name", "Duration", "Intensity", "Date", "Time"};
        int updateChoice = JOptionPane.showOptionDialog(this, "Which field would you like to update?",
                "Update Exercise", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (updateChoice == JOptionPane.CLOSED_OPTION) {
            return null;  // Return null if the user cancels the update
        }

        switch (updateChoice) {
            case 0:  // Exercise Type
                String newType = JOptionPane.showInputDialog(this, "Enter new Exercise Type:", exerciseToUpdate.getExerciseType());
                if (newType != null && !newType.trim().isEmpty()) {
                    exerciseToUpdate.setExerciseType(newType);
                } else {
                    JOptionPane.showMessageDialog(this, "Exercise type cannot be empty.");
                    return null;
                }
                break;

            case 1:  // Exercise Name
                String newName = JOptionPane.showInputDialog(this, "Enter new Exercise Name:", exerciseToUpdate.getExerciseName());
                if (newName != null && !newName.trim().isEmpty()) {
                    exerciseToUpdate.setExerciseName(newName);
                } else {
                    JOptionPane.showMessageDialog(this, "Exercise name cannot be empty.");
                    return null;
                }
                break;

            case 2:  // Duration
                int newDuration = getValidInt();
                exerciseToUpdate.setDuration(newDuration);
                exerciseToUpdate.setCalBurned(calculateCaloriesBurned(exerciseToUpdate.getIntensity(), newDuration));
                break;

            case 3:  // Intensity
                String[] intensityOptions = {"Low", "Medium", "High", "Mixed"};
                String newIntensity = (String) JOptionPane.showInputDialog(this, "Select Intensity:", "Intensity", JOptionPane.QUESTION_MESSAGE, null, intensityOptions, intensityOptions[0]);
                if (newIntensity == null) return null;
                exerciseToUpdate.setIntensity(newIntensity);
                exerciseToUpdate.setCalBurned(calculateCaloriesBurned(newIntensity, exerciseToUpdate.getDuration()));
                break;

            case 4:  // Date
                LocalDate newDate = validateAndParseDate();
                exerciseToUpdate.setExerciseDate(newDate);
                break;

            case 5:  // Time
                LocalTime newTime = validateAndParseTime();
                exerciseToUpdate.setExerciseTime(newTime);
                break;

            default:
                JOptionPane.showMessageDialog(this, "Invalid option. Update failed.");
                return null;  // Return null if an invalid option is selected
        }

        // Recalculate calories burned after updating the exercise entry
        float newCaloriesBurned = calculateCaloriesBurned(exerciseToUpdate.getIntensity(), exerciseToUpdate.getDuration());
        exerciseToUpdate.setCalBurned(newCaloriesBurned);

        // Notify the user that the exercise entry was updated
        JOptionPane.showMessageDialog(this, "Exercise updated successfully!");
        // Optionally refresh or display the updated list
        displayExerciseLog();

        return exerciseToUpdate;  // Return the updated ExerciseEntry object
    }
    /**
     * Method Name: displayExerciseLog
     * Purpose: This method generates and returns a formatted string representing the current exercise log.
     * Arguments: None
     * Return Value: void
     */
    private void displayExerciseLog() {
        StringBuilder logContent2 = new StringBuilder();


        if (exerciseList.isEmpty()) {
            logContent2.append("No exercise entries available.\n");
        } else {
            logContent2.append("Exercise Log:\n");
            for (ExerciseEntry exercise : exerciseList) {
                logContent2.append(exercise).append("\n");  // Appending food entry to the log content
            }
        }

        // Display the log content in the JTextArea (GUI component)
        JOptionPane.showMessageDialog(this, logContent2.toString(), "Exercise Log Entries", JOptionPane.INFORMATION_MESSAGE);
    }

    private void goToHomeScreen() {
        // Logic for going to Home Screen
        JOptionPane.showMessageDialog(this, "Navigating to Home Screen...");
        new HomeScreen();
        dispose();
    }


    /**
     * Name: calculateCaloriesBurned
     * Purpose: this method calculates the calories burned for an entry based on
     *          duration and intensity. Low :5x, Medium: 8x, High: 12x, Mixed 10x
     * Arguments:int duration, String intensity
     * return value: float
     */

    private float calculateCaloriesBurned(String intensity, int duration) {
        float multiplier;
        switch (intensity) {
            case "low":
                multiplier = 5;
                break;
            case "medium":
                multiplier = 8;
                break;
            case "high":
                multiplier = 12;
                break;
            case "mixed":
                multiplier = 10;
                break;
            default:
                    multiplier = 6;
       }
        return duration * multiplier;
    }
    /**
     * Name: calculateTotalCaloriesBurnedByDate
     * Purpose: this method calculates the calories burned for entries on the same date
     * Arguments: None
     * return value: Map<LocalDate, Float>
     */

    public static Map<LocalDate, Float> calculateTotalCaloriesBurnedByDate() {
        // Create a map to store total calories burned by each date
        Map<LocalDate, Float> calburnByDate = new HashMap<>();

        // Loop through each food entry in the list
        for (ExerciseEntry exerciseEntry : exerciseList) {
            // Get the meal date and calories from the food entry
            LocalDate date = exerciseEntry.getExerciseDate();
            float caloriesBurned = exerciseEntry.getCalBurned();

            // Add the calories to the corresponding date in the map
            calburnByDate.put(date, calburnByDate.getOrDefault(date, 0f) + caloriesBurned);
        }

        // Format the output to display in a table-like format
        StringBuilder result = new StringBuilder();
        result.append("Date\t\t\tTotal Calories Burned\n");
        result.append("--------------------------------------------------\n");

        for (Map.Entry<LocalDate, Float> entry : calburnByDate.entrySet()) {
            // Format the date and calories for display
            result.append(String.format("%s\t\t%.2f\n", entry.getKey(), entry.getValue()));
        }

        // Display the results in a JOptionPane
        JOptionPane.showMessageDialog(null, result.toString(), "Total Calories Burned by Date", JOptionPane.INFORMATION_MESSAGE);

        // Return the map for further use if necessary
        return calburnByDate;
    }

    /**
     * Name: userChoice2
     *Purpose: Ask user if they want to stay on current screen
     *        or move to the next
     *Arguments:NOne
     * Return Value: void
     */
   private void userChoice2() {

        int choice = JOptionPane.showConfirmDialog(this, "Do you want to move to the Progress screen?", "Navigation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            new ProgressScreen();
            dispose();
        }
    }

    /**
     * Method Name: main
     * Purpose: This is the entry point of the class.
     *           It initializes `ExerciseLogScreen` object and
     *           displays the GUI for the user.
     * Arguments: String[] args
     * Return Value: void
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExerciseLogScreen frame = new ExerciseLogScreen();
            frame.setVisible(true); // Make JFrame visible
        });
    }
}