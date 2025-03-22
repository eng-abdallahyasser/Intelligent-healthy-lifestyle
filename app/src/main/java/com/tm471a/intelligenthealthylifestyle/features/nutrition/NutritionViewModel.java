package com.tm471a.intelligenthealthylifestyle.features.nutrition;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.tm471a.intelligenthealthylifestyle.data.model.NutritionAdvice;
import com.tm471a.intelligenthealthylifestyle.data.repository.NutritionRepository;

public class NutritionViewModel extends ViewModel {
    private final NutritionRepository repository = new NutritionRepository();
    private final MutableLiveData<NutritionAdvice> nutritionAdvice = new MutableLiveData<>();
    private final MutableLiveData<String> statusMessage = new MutableLiveData<>("Initiating...");
    private final Gson gson;

    public NutritionViewModel() {
        this.gson = new Gson();
        repository.getIsInitialized().observeForever(isReady->{
            if (isReady) generateNutritionAdvice();
        });
    }

    public void generateNutritionAdvice() {
        statusMessage.postValue("Generating Nutrition Advice For You...");

        repository.generateNutritionAdvice( new NutritionRepository.ResponseCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    String cleanedJson = response.replaceAll("```json\n|```", "");
                    NutritionAdvice generatedNutritionAdvice = gson.fromJson(cleanedJson, NutritionAdvice.class);

                    if(generatedNutritionAdvice!=null){
                    nutritionAdvice.postValue(generatedNutritionAdvice);}
                    else {
                        statusMessage.postValue("NutritionViewModel : generatedNutritionAdvice is null");
                    }
                    statusMessage.postValue("done");
                    Log.d("NutritionViewModel", "generateNutritionAdvice() called");
                } catch (Exception e) {
                    // Handle parsing errors
                    statusMessage.postValue(e.getMessage());
                }
            }
            @Override
            public void onError(String error) {
                // Handle errors
                statusMessage.postValue(error);
            }
        });

    }

    public MutableLiveData<NutritionAdvice> getNutritionAdvice() {
        return nutritionAdvice;
    }

    public MutableLiveData<String> getStatusMessage() {
        return statusMessage;
    }
}
