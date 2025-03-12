package com.tm471a.intelligenthealthylifestyle.features.nutrition;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tm471a.intelligenthealthylifestyle.data.model.MealPlan;
import com.tm471a.intelligenthealthylifestyle.data.repository.NutritionRepository;
import java.util.List;

public class NutritionViewModel extends ViewModel {
    private final NutritionRepository repository = new NutritionRepository();
    private final MutableLiveData<List<MealPlan>> mealPlans = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void loadNutritionData() {

    }

    public LiveData<List<MealPlan>> getMealPlans() {
        return mealPlans;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
