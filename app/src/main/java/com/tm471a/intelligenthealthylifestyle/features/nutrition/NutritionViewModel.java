package com.tm471a.intelligenthealthylifestyle.features.nutrition;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.tm471a.intelligenthealthylifestyle.data.model.NutritionAdvice;
import com.tm471a.intelligenthealthylifestyle.data.repository.NutritionRepository;

public class NutritionViewModel extends ViewModel {
    private final NutritionRepository repository = new NutritionRepository();
    private final MutableLiveData<NutritionAdvice> nutritionAdvice = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final Gson gson;

    public NutritionViewModel() {
        this.gson = new Gson();
    }

    public void generateNutritionAdvice() {

        repository.generateNutritionAdvice( new NutritionRepository.ResponseCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    String cleanedJson = response.replaceAll("```json\n|```", "");
                    NutritionAdvice generatedNutritionAdvice = gson.fromJson(cleanedJson, NutritionAdvice.class);
                    NutritionAdvice testAdvice = new NutritionAdvice();
                    testAdvice.setSeoTitle("Test Title");
                    testAdvice.setDescription("This is a test description.");
                    testAdvice.setGoal("Weight Loss");
                    if(generatedNutritionAdvice!=null){
                    nutritionAdvice.postValue(generatedNutritionAdvice);}
                    else {
                        nutritionAdvice.postValue(testAdvice);
                    }
                    Log.d("NutritionViewModel", "generateNutritionAdvice() called");
                } catch (Exception e) {
                    // Handle parsing errors
                }
            }

            @Override
            public void onError(String error) {
                // Handle errors

            }
        });

    }

    public MutableLiveData<NutritionAdvice> getNutritionAdvice() {
        return nutritionAdvice;
    }

}
