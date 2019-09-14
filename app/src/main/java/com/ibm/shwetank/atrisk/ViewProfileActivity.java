package com.ibm.shwetank.atrisk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class ViewProfileActivity extends AppCompatActivity {
    TextView nameET;
    TextView ageET;
    TextView phoneNumberET;
    TextView streetET;
    TextView aptET;
    TextView cityET;
    TextView stateET;
    TextView zipcodeET;
    TextView genderSP;
    Switch disableSW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        nameET = findViewById(R.id.et_name);
        ageET = findViewById(R.id.et_age);
        phoneNumberET = findViewById(R.id.et_phone);
        streetET = findViewById(R.id.et_street);
        aptET = findViewById(R.id.et_apt);
        cityET = findViewById(R.id.et_city);
        stateET = findViewById(R.id.et_state);
        zipcodeET = findViewById(R.id.et_zipcode);
        genderSP = findViewById(R.id.spinner_gender);
        disableSW = findViewById(R.id.sw_disable);
        SharedPref instance = SharedPref.getInstance(getApplicationContext());
        genderSP.setText(instance.getString("gender"));
        nameET.setText(instance.getString("name"));
        ageET.setText(instance.getString("age"));
        phoneNumberET.setText(instance.getString("phone"));
        streetET.setText(instance.getString("street"));
        cityET.setText(instance.getString("city"));
        aptET.setText(instance.getString("apt"));
        stateET.setText(instance.getString("state"));
        zipcodeET.setText(instance.getString("zipcode"));
        disableSW.setChecked(instance.getBoolean("disable"));
    }
}
