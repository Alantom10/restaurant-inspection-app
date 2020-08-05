package com.example.restauranthealthinspectionbrowser.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranthealthinspectionbrowser.R;
import com.example.restauranthealthinspectionbrowser.model.FilterSettings;
import com.example.restauranthealthinspectionbrowser.model.Inspection;

public class FilterActivity extends AppCompatActivity {
    private boolean favourites;
    private String hazStr;
    private int minViolation;
    private int maxViolation;

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, InspectionActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Button button = (Button) findViewById(R.id.update_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton fav = (RadioButton) findViewById(R.id.fav_radio);
                favourites = fav.isChecked();

                RadioGroup hazardLev = (RadioGroup) findViewById(R.id.radioGroup2);
                int rad = hazardLev.getCheckedRadioButtonId();

                RadioButton x = (RadioButton) findViewById(rad);
                try{
                    hazStr = (String) x.getText();
                } catch(NullPointerException ex) {
                    hazStr = "Low Medium High";
                }
                Log.d("myTag", hazStr);



                Log.d("violationText2", "valueMax " + maxViolation);

                FilterSettings filterSet = FilterSettings.getFilterSettings();
                filterSet.setFavSetting(favourites);
                filterSet.setHazardSetting(hazStr);
                filterSet.setMinSetting(minViolation);
                filterSet.setMaxSetting(maxViolation);
            }
        });
    }
}
