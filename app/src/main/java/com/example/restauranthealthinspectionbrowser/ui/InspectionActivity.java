package com.example.restauranthealthinspectionbrowser.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restauranthealthinspectionbrowser.R;
import com.example.restauranthealthinspectionbrowser.model.DateHelper;
import com.example.restauranthealthinspectionbrowser.model.Inspection;
import com.example.restauranthealthinspectionbrowser.model.InspectionManager;

import java.util.ArrayList;

public class InspectionActivity extends AppCompatActivity {
    public static final String EXTRA_TRACKING_NUMBER = "tracking number";
    public static final String EXTRA_INSPECTION_DATE = "inspection date";

    private Inspection mInspection;

    ArrayList<String> violations = new ArrayList<String>();

    public static Intent makeIntent(Context context, Inspection inspection) {
        Intent intent = new Intent(context, InspectionActivity.class);
        intent.putExtra(EXTRA_TRACKING_NUMBER, inspection.getTrackingNum());
        intent.putExtra(EXTRA_INSPECTION_DATE, inspection.getInspectionDate());
        return intent;
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
        final String trackingNum = intent.getStringExtra(EXTRA_TRACKING_NUMBER);
        final String inspectionDate = intent.getStringExtra(EXTRA_INSPECTION_DATE);

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
        txtInspectionDate.setText(DateHelper.getFullDate(mInspection.getInspectionDate()));
        txtInspectionType.setText(mInspection.getInspectionType());
        txtNumOfCritical.setText(Integer.toString(mInspection.getNumOfCritical()));
        txtNumOfNonCritical.setText(Integer.toString(mInspection.getNumOfNonCritical()));
        txtHazardRating.setText(mInspection.getHazardRating());

        ImageView hazardImage = findViewById(R.id.hazardLogo);
        if(mInspection.getHazardRating().equalsIgnoreCase("Low")){
            txtHazardRating.setTextColor(ContextCompat.getColor(this, R.color.lowHazardLevel));
            hazardImage.setImageResource(R.drawable.green_exclamation_circle);
        } else if (mInspection.getHazardRating().equalsIgnoreCase("Moderate")) {
            txtHazardRating.setTextColor(ContextCompat.getColor(this, R.color.moderateHazardLevel));
            hazardImage.setImageResource(R.drawable.yellow_exclamation_circle);
        } else if (mInspection.getHazardRating().equalsIgnoreCase("High")) {
            txtHazardRating.setTextColor(ContextCompat.getColor(this, R.color.highHazardLevel));
            hazardImage.setImageResource(R.drawable.red_exclamation_circle);
        }
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
                TextView txtDetailViolation = (TextView) violation.findViewById(R.id.txtToast);
                String toast = txtDetailViolation.getText().toString();
//                Integer index = Integer.valueOf(toast);
//                String toastStr = "";
//                for (BriefViolation briefViolation : BriefViolationManager.getInstance(view.getContext()).getListBriefViolation()) {
//                    if(briefViolation.getIndex() == index){
//                        toastStr = briefViolation.getViolationBriefDesc();
//                        break;
//                    }
//                }
                showToast(toast);
            }
        });
    }

    private void showToast(String text) {
        Toast.makeText(InspectionActivity.this, text, Toast.LENGTH_LONG).show();
    }
}
