package com.ibm.shwetank.atrisk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushException;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationButton;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationCategory;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationOptions;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPSimplePushNotification;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
    private static final int channelID = 844;
    MFPPush push;

    //Handles the notification when it arrives
    MFPPushNotificationListener notificationListener = new MFPPushNotificationListener() {

        @Override
        public void onReceive (final MFPSimplePushNotification message){
            // Handle Push Notification
            //non UI thread
            if (message.actionName.equals("View")){
                System.out.print("Clicked View Action");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeSDK();
        createNotificationChannel();
        findIds();
        setListeners();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(push != null) {
            push.listen(notificationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (push != null) {
            push.hold();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(push != null){
            push.registerDevice(new MFPPushResponseListener<String>() {

                @Override
                public void onSuccess(final String response) {
                    //handle successful device registration here
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "successful registration", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(MFPPushException ex) {
                    //handle failure in device registration here
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "registration failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(push != null){
            push.unregister(new MFPPushResponseListener<String>() {

                @Override
                public void onSuccess(String s) {
                    // Handle success
                }

                @Override
                public void onFailure(MFPPushException e) {
                    // Handle Failure
                }
            });
        }
    }

    private void initializeSDK() {
        MFPPushNotificationOptions options = new MFPPushNotificationOptions();
        MFPPushNotificationButton viewButton = new MFPPushNotificationButton.Builder("View")
                .setIcon("extension_circle_icon")
                .setLabel("view")
                .build();
        List<MFPPushNotificationButton> buttonGroup =  new ArrayList<MFPPushNotificationButton>();
        ArrayList list = new ArrayList();
        list.add(viewButton);
        MFPPushNotificationCategory category = new MFPPushNotificationCategory.Builder("First_Button_Group").setButtons(buttonGroup).build();
        List<MFPPushNotificationCategory> categoryList =  new ArrayList<MFPPushNotificationCategory>();
        categoryList.add(category);
        options.setInteractiveNotificationCategories(categoryList);

        // Initialize the SDK
        BMSClient.getInstance().initialize(this, BMSClient.REGION_US_SOUTH);
        //Initialize client Push SDK
        push = MFPPush.getInstance();
        push.initialize(getApplicationContext(), "8b0c8b16-5d54-4ae6-9c70-ee5fd6008bbf", "099d8b1a-2e0d-4130-910a-48856dd95e87", options);
    }

    private void setListeners() {
        continueBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSaveable = true;
                String name = String.valueOf(nameET.getText());
                if(name.equals("null") || name.length() == 0){
                    isSaveable = false;
                    showToast("Please enter your name");
                }
                String age = String.valueOf(ageET.getText());
                if(age.equals("null") || age.length() == 0){
                    isSaveable = false;
                    showToast("Please enter your age");
                }

                String gender = String.valueOf(genderSP.getSelectedItem());

                String street = String.valueOf(streetET.getText());
                if(street.equals("null") || street.length() == 0){
                    isSaveable = false;
                    showToast("Please enter your street street");
                }

                String apt = String.valueOf(aptET.getText());
                if(apt.equals("null") || apt.length() == 0){
                    isSaveable = false;
                    showToast("Please enter your apartment number");
                }

                String city = String.valueOf(cityET.getText());
                if(city.equals("null") || city.length() == 0){
                    isSaveable = false;
                    showToast("Please enter your city");
                }

                String state = String.valueOf(stateET.getText());
                if(state.equals("null") || state.length() == 0){
                    isSaveable = false;
                    showToast("Please enter your state");
                }

                String zipcode = String.valueOf(zipcodeET.getText());
                if(zipcode.equals("null") || zipcode.length() == 0){
                    isSaveable = false;
                    showToast("Please enter your zipcode");
                }
                String phoneNumber = String.valueOf(phoneNumberET.getText());
                if(phoneNumber.equals("null") || phoneNumber.length() == 0){
                    isSaveable = false;
                    showToast("Please enter your phone number");
                }

                if(isSaveable){
                    showToast("everything is good");
                    showNotification();
                }

            }
        });
    }

    private void showNotification() {

        Intent intent = new Intent(this, NotificationReceiverActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "shwetank")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Tornado Alert!!")
                .setContentText("Do you need to be evacuated?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(channelID, builder.build());
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("shwetank", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void findIds() {

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
    }

}
