package com.ibm.shwetank.atrisk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int channelID = 844;
    MFPPush push;
    Button makeProfile;
    Button showProfile;
    List<String> tagsAvailable = new ArrayList<>();

    //Handles the notification when it arrives
    MFPPushNotificationListener notificationListener = new MFPPushNotificationListener() {

        @Override
        public void onReceive (final MFPSimplePushNotification message){
            // Handle Push Notification
            //non UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject j = new JSONObject(message.getPayload());
                            showNotification((String)j.get("disaster"), (String)j.get("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                    getTags();

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

    private void getTags() {
        push.getTags(new MFPPushResponseListener<List<String>>(){

            @Override
            public void onSuccess(List<String> tags){
                tagsAvailable = tags;
                subscribeToTags();
            }

            @Override
            public void onFailure(MFPPushException ex){
                System.out.println("Error getting available tags.. " + ex.getMessage());
            }
        });
    }

    private void subscribeToTags() {
        push.subscribe("user", new MFPPushResponseListener<String>() {

            @Override
            public void onSuccess(String arg) {
                System.out.println("Succesfully Subscribed to: "+ arg);
            }

            @Override
            public void onFailure(MFPPushException ex) {
                System.out.println("Error subscribing to Tag1.." + ex.getMessage());
            }
        });
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
        push.initialize(getApplicationContext(), "a6c30504-d212-4bff-8d20-ba51ee26d51c", "1d39e2bf-adc5-4f63-84a4-0196eab5b2f2", options);
    }

    private void setListeners() {

        makeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        showProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showNotification(String disaster, String message) {

        Intent intent = new Intent(this, NotificationReceiverActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "shwetank")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(disaster)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(channelID, builder.build());
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
        makeProfile = findViewById(R.id.make_profile);
        showProfile = findViewById(R.id.show_profile);
    }

}
