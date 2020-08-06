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
import com.example.restauranthealthinspectionbrowser.model.QueryPreferences;

public class FilterActivity extends AppCompatActivity {
    private boolean favourites = false;
    private String hazStr = null;
    private String minViolation = null;
    private String maxViolation = null;

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, FilterActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        getSupportActionBar().setTitle(getString(R.string.filter_activity_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button update = (Button) findViewById(R.id.update_btn);
        update.setOnClickListener(new View.OnClickListener() {
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
                    if (minViolation.equals("")) {
                        minViolation = null;
                    }
                } catch(NumberFormatException ex) {
                    minViolation = null;
                }
                Log.d("violationText1", "valueMin " + minViolation);

                EditText maxInput = (EditText) findViewById(R.id.max_input);
                try{
                    maxViolation = maxInput.getText().toString();
                    if (maxViolation.equals("")) {
                        maxViolation = null;
                    }
                } catch(NumberFormatException ex) {
                    maxViolation = null;
                }
                Log.d("violationText2", "valueMax " + maxViolation);

                QueryPreferences.setStoredFavouriteQuery(FilterActivity.this, favourites);
                QueryPreferences.setStoredRatingQuery(FilterActivity.this, hazStr);
                QueryPreferences.setStoredMaximumIssuesQuery(FilterActivity.this, maxViolation);
                QueryPreferences.setStoredMinimumIssuesQuery(FilterActivity.this, minViolation);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button clear = (Button) findViewById(R.id.clearFilterBtn);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favourites = false;
                hazStr = null;
                minViolation = null;
                maxViolation = null;

                QueryPreferences.setStoredFavouriteQuery(FilterActivity.this, favourites);
                QueryPreferences.setStoredRatingQuery(FilterActivity.this, hazStr);
                QueryPreferences.setStoredMaximumIssuesQuery(FilterActivity.this, maxViolation);
                QueryPreferences.setStoredMinimumIssuesQuery(FilterActivity.this, minViolation);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
