package com.ibm.shwetank.atrisk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NotificationReceiverActivity extends AppCompatActivity {

    Button yes, no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_receiver);

        yes = findViewById(R.id.bt_yes);
        no = findViewById(R.id.bt_no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NotificationReceiverActivity.this, "Yes clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
