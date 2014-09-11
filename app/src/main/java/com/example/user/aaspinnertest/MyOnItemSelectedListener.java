package com.example.user.aaspinnertest;

/**
 * Created by user on 1/9/2014.
 */

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class MyOnItemSelectedListener implements OnItemSelectedListener{
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch(parent.getId()) {
            case R.id.ageSpinner:
                if(parent.getItemAtPosition(pos).toString()!="Please select your age") {
                    Toast.makeText(parent.getContext(), "Selected age: " +
                            parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.weightSpinner:
                if(parent.getItemAtPosition(pos).toString()!="Please select your weight") {
                    Toast.makeText(parent.getContext(), "Selected weight: " +
                            parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
