package com.example.restauranthealthinspectionbrowser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restauranthealthinspectionbrowser.R;
import com.example.restauranthealthinspectionbrowser.model.Inspection;

import java.util.ArrayList;

public class violationAdapter extends ArrayAdapter {
    private int rid;
    public violationAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<String> objects){
        super(context, resource, textViewResourceId, objects);
        this.rid = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Inspection inspection = (Inspection) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(this.rid, null);

        TextView textViewViolation = (TextView) view.findViewById(R.id.txtViolationDetail);
        textViewViolation.setText(getViolationString(inspection.getViolation()));
        return view;
    }

    private String getViolationString(String[] violations) {
        if(violations == null || violations.length == 0){
            return "";
        }

        StringBuilder vios = new StringBuilder();
        for (String violation : violations) {
            vios.append(violation);
        }

        return vios.toString();
    }
}
