package com.example.restauranthealthinspectionbrowser.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restauranthealthinspectionbrowser.R;
import com.example.restauranthealthinspectionbrowser.adapter.ViolationAdapter;
import com.example.restauranthealthinspectionbrowser.model.Inspection;
import com.example.restauranthealthinspectionbrowser.model.InspectionManager;
import com.example.restauranthealthinspectionbrowser.model.Restaurant;

import java.util.ArrayList;

public class InspectionActivity extends AppCompatActivity {
    ArrayList<String> violations = new ArrayList<String>();
    private Inspection mInspection;
    private Restaurant mRestaurant;

    public InspectionActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);

        ListView listView = (ListView) findViewById(R.id.listViolation);
        setListItemClick(listView);

        getInspection();
        showInfo();
        showViolation();
    }

    private void getInspection() {
        Intent intent = getIntent();
        final String trackingNum = intent.getStringExtra("trackingNum");
        final String inspectionDate = intent.getStringExtra("inspectionDate");

        for (Inspection inspection : InspectionManager.getInstance(this.getBaseContext()).getInspections()) {
            if (inspection.getTrackingNum().equalsIgnoreCase(trackingNum) && inspection.getInspectionDate().equalsIgnoreCase(inspectionDate)) {
                mInspection = inspection;
            }
        }


    }

    void showInfo() {
        //showViolation();

        TextView txtTrackNum = (TextView) findViewById(R.id.dataTracking);
        TextView txtInspectionDate = (TextView) findViewById(R.id.dataInspDate);
        TextView txtInspectionType = (TextView) findViewById(R.id.dataInspType);
        TextView txtNumOfCritical = (TextView) findViewById(R.id.dataNumIssue);
        TextView txtNumOfNonCritical = (TextView) findViewById(R.id.dataNonIssue);
        TextView txtHazardRating = (TextView) findViewById(R.id.dataHazRating);
        txtTrackNum.setText(mInspection.getTrackingNum());
        txtInspectionDate.setText(mInspection.getInspectionDate());
        txtInspectionType.setText(mInspection.getInspectionType());
        txtNumOfCritical.setText(Integer.toString(mInspection.getNumOfCritical()));
        txtNumOfNonCritical.setText(Integer.toString(mInspection.getNumOfNonCritical()));
        txtHazardRating.setText(mInspection.getHazardRating());

        ImageView hazardImage = findViewById(R.id.hazardLogo);
        if(mInspection.getHazardRating().equalsIgnoreCase("Low")){
            txtHazardRating.setTextColor(Color.BLUE);
            hazardImage.setImageResource(R.drawable.green_face);
        } else if (mInspection.getHazardRating().equalsIgnoreCase("Moderate")) {
            txtHazardRating.setTextColor(Color.YELLOW);
            hazardImage.setImageResource(R.drawable.yellow_face);
        } else if (mInspection.getHazardRating().equalsIgnoreCase("High")) {
            txtHazardRating.setTextColor(Color.RED);
            hazardImage.setImageResource(R.drawable.red_face);
        }
        //txtHazardRating.setTextColor(mInspection.getHazardLogo());
    }

    private void showViolation(){
        for (String s : mInspection.getViolation()) {
            violations.add(s);
        }

        ArrayAdapter<String> adapter = new ViolationAdapter(InspectionActivity.this, 0, R.layout.list_item_violation, violations);
        ListView violation = findViewById(R.id.listViolation);

        violation.setAdapter(adapter);
    }

    private void setListItemClick(ListView listView){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View violation = view;
                TextView txtDetailViolation = (TextView) violation.findViewById(R.id.txtViolationDetail);
                String toast = txtDetailViolation.getText().toString();

                showToast(toast);
            }
        });
    }

    private void showToast(String text) {
        Toast.makeText(InspectionActivity.this, text, Toast.LENGTH_LONG).show();
    }
}
