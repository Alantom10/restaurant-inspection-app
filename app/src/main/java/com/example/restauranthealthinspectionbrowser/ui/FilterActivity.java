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
    private String minViolation;
    private String maxViolation;

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
                    hazStr = null; //int rad -1 when nothing is selected
                }

                EditText minInput = (EditText) findViewById(R.id.min_input);
                try{
                    minViolation = minInput.getText().toString();
                } catch(NumberFormatException ex) {
                    minViolation = null;
                }
                Log.d("violationText1", "valueMin " + minViolation);

                EditText maxInput = (EditText) findViewById(R.id.max_input);
                try{
                    maxViolation = maxInput.getText().toString();
                } catch(NumberFormatException ex) {
                    maxViolation = null;
                }
                Log.d("violationText2", "valueMax " + maxViolation);

                FilterSettings filterSet = FilterSettings.getFilterSettings();
                filterSet.setFavSetting(favourites);
                filterSet.setHazardSetting(hazStr);
                filterSet.setMinSetting(minViolation);
                filterSet.setMaxSetting(maxViolation);

                finish();
            }
        });

        Button button1 = (Button) findViewById(R.id.clearFilterBtn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button button2 = (Button) findViewById(R.id.cancel_btn);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterSettings filterSet = FilterSettings.getFilterSettings();
                if(favourites == true) {
                    favourites = false;
                }
                filterSet.setFavSetting(favourites);

                hazStr = null;
                filterSet.setHazardSetting(hazStr);

                minViolation = null;
                filterSet.setMinSetting(minViolation);

                maxViolation = null;
                filterSet.setMaxSetting(maxViolation);

                finish();
            }
        });
    }
}
