package com.example.android.appp;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;


import static java.lang.Thread.sleep;


public class ChooseItem extends AppCompatActivity {


    Button PersonalInfoButton,waitForTreat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_item_ui);
        CommonInstance.getInstance().addActivity(this);

        ActivityCompat.requestPermissions(ChooseItem.this, new String[]{android
                .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        PersonalInfoButton=(Button)findViewById(R.id.personalInfo);
        PersonalInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseItem.this,PersonalInfo.class);
                startActivity(intent);
            }
        });

        waitForTreat=(Button)findViewById(R.id.toTreat);
        waitForTreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseItem.this,TreatList.class);
                startActivity(intent);
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ChooseItem.this, Login.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
