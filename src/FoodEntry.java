import java.time.LocalDate;

public class FoodEntry {
    private String foodID;
    private String foodName;
    private float calories;
    private float carbs;
    private float protein;
    private float fat;
    private String mealType;
    private LocalDate mealDate;

    public FoodEntry(String foodID, String foodName, float calories, float carbs, float protein, float fat, String mealType, LocalDate mealDate) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.mealType = mealType;
        this.mealDate = mealDate;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getCarbs() {
        return carbs;
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    @Override
    public String toString() {
        return foodID + ": " + foodName + " (" + mealType + ") - Calories: " + calories + ", Carbs: " + carbs + "g, Protein: " + protein + "g, Fat: " + fat + "g";
    }

    public LocalDate getMealDate() {
        return mealDate;
    }

    public void setMealDate(LocalDate date) {
        this.mealDate = date;
    }
}

