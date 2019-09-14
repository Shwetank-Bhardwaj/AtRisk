package com.ibm.shwetank.atrisk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    String[] gender = {"Male", "Female", "Other"};
    EditText nameET;
    EditText ageET;
    EditText phoneNumberET;
    EditText streetET;
    EditText aptET;
    EditText cityET;
    EditText stateET;
    EditText zipcodeET;
    Spinner genderSP;
    Switch disableSW;
    Button continueBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameET = findViewById(R.id.et_name);
        ageET = findViewById(R.id.et_age);
        phoneNumberET = findViewById(R.id.et_phone);
        streetET = findViewById(R.id.et_street);
        aptET = findViewById(R.id.et_apt);
        cityET = findViewById(R.id.et_city);
        stateET = findViewById(R.id.et_state);
        zipcodeET = findViewById(R.id.et_zipcode);
        genderSP = findViewById(R.id.spinner_gender);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        gender);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        genderSP.setAdapter(spinnerArrayAdapter);
        disableSW = findViewById(R.id.sw_disable);
        continueBT = findViewById(R.id.bt_continue);

        continueBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref instance = SharedPref.getInstance(getApplicationContext());
                String name = String.valueOf(nameET.getText());
                if(name.equals("null") || name.length() == 0){
                    showToast("Please enter your name");
                    return;
                }
                instance.putString("name", name);

                String age = String.valueOf(ageET.getText());
                if(age.equals("null") || age.length() == 0){
                    showToast("Please enter your age");
                    return;
                }
                instance.putString("age", age);

                String gender = String.valueOf(genderSP.getSelectedItem());
                instance.putString("gender", gender);

                String street = String.valueOf(streetET.getText());
                if(street.equals("null") || street.length() == 0){
                    showToast("Please enter your street");
                    return;
                }
                instance.putString("street", street);

                String apt = String.valueOf(aptET.getText());
                if(apt.equals("null") || apt.length() == 0){
                    showToast("Please enter your apartment number");
                    return;
                }
                instance.putString("apt", apt);

                String city = String.valueOf(cityET.getText());
                if(city.equals("null") || city.length() == 0){
                    showToast("Please enter your city");
                    return;
                }
                instance.putString("city", city);

                String state = String.valueOf(stateET.getText());
                if(state.equals("null") || state.length() == 0){
                    showToast("Please enter your state");
                    return;
                }
                instance.putString("state", state);

                String zipcode = String.valueOf(zipcodeET.getText());
                if(zipcode.equals("null") || zipcode.length() == 0){
                    showToast("Please enter your zipcode");
                    return;
                }
                instance.putString("zipcode", zipcode);

                String phoneNumber = String.valueOf(phoneNumberET.getText());
                if(phoneNumber.equals("null") || phoneNumber.length() == 0){
                    showToast("Please enter your phone number");
                    return;
                }
                instance.putString("phone", phoneNumber);
                boolean isDisable = disableSW.isChecked();
                instance.putBoolean("disable", isDisable);

                showToast("everything is good");
                    //save to database and close activity
                    finish();
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

}
