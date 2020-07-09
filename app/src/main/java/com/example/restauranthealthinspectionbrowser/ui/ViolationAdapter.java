package com.example.restauranthealthinspectionbrowser.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restauranthealthinspectionbrowser.R;
import com.example.restauranthealthinspectionbrowser.model.BriefViolation;
import com.example.restauranthealthinspectionbrowser.model.BriefViolationManager;
import com.example.restauranthealthinspectionbrowser.model.Inspection;

import java.util.ArrayList;

public class ViolationAdapter extends ArrayAdapter {
    private int rid;

    public ViolationAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<String> objects) {
        super(context, resource, textViewResourceId, objects);
        this.rid = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String violations = (String) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(this.rid, null);

        ImageView imageViolation = (ImageView) view.findViewById(R.id.imageViolation);
        ImageView imageSeverity = (ImageView) view.findViewById(R.id.imageSeverity);

        if(violations.contains("Foods") || violations.contains("Food") || violations.contains("foods") || violations.contains("food")){
            imageViolation.setImageResource(R.drawable.food_icon);
        }
        else if(violations.contains("pest") ||violations.contains("pests")){
            imageViolation.setImageResource(R.drawable.pest_icon);
        }
        else if(violations.contains("Equipment") || violations.contains("equipment")){
            imageViolation.setImageResource(R.drawable.equipment_icon);
        } else if (violations.contains("handwashing") || violations.contains("clean") || violations.contains("hygiene")) {
            imageViolation.setImageResource(R.drawable.hygene_icon);
        } else if (violations.contains("certificate") || violations.contains("animal") || violations.contains("manner") || violations.contains("FOODSAFE")) {
            imageViolation.setImageResource(R.drawable.certificate_icon);
        } else {
            imageViolation.setImageResource(R.drawable.blank_icon);
        }

        if (violations.contains("Not Critical")) {
            imageSeverity.setImageResource(R.drawable.green_shield);
        } else if (violations.contains("Critical")) {
            imageSeverity.setImageResource(R.drawable.red_shield);
        } else {
            imageSeverity.setImageResource(R.drawable.blank_icon);
        }

        TextView textViewViolation = (TextView) view.findViewById(R.id.txtViolationDetail);
        TextView textToast = (TextView) view.findViewById(R.id.txtToast);
        String[] s = violations.split(",");

        Integer index = Integer.valueOf(s[0]);
        String toastStr = "";
        for (BriefViolation briefViolation : BriefViolationManager.getInstance(view.getContext()).getListBriefViolation()) {
            if(briefViolation.getIndex() == index){
                toastStr = briefViolation.getViolationBriefDesc();
                break;
            }
        }

        textToast.setText(violations);

        textViewViolation.setText(toastStr);
        return view;
    }

    private String getViolationString(String[] violations) {
        if (violations == null || violations.length == 0) {
            return "";
        }

        StringBuilder vios = new StringBuilder();
        for (String violation : violations) {
            vios.append(violation);
        }

        return vios.toString();
    }
}