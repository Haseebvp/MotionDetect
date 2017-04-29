package com.motioncheck.teztapplication;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.motioncheck.wake.EventCommunication;
import com.motioncheck.wake.MovementManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.R.attr.max;

public class MainActivity extends AppCompatActivity {

    MovementManager movementManager;
    TextView status;
    ImageView statusPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = (TextView) findViewById(R.id.status);
        statusPic = (ImageView) findViewById(R.id.statuspic);
        movementManager = MovementManager.getInstance(this);
        movementManager.setOnDataChanged(new EventCommunication() {
            @Override
            public void getValues(String data, String statustext) {
                status.setText(data);
                switch (data) {
                    case "Jumping":
                        statusPic.setImageResource(R.drawable.jump);
                        break;

                    case "Running":
                        statusPic.setImageResource(R.drawable.run);
                        break;

                    case "Walking":

                        statusPic.setImageResource(R.drawable.walk);
                        break;

//                    case "Moving":
//                        statusPic.setImageResource(R.drawable.move);
//                        break;

                    case "Idle":

                        statusPic.setImageResource(R.drawable.idle);
                        break;

                    case "Phone Detached":

                        statusPic.setImageResource(R.drawable.detach);
                        break;

                    default :
                        statusPic.setImageResource(R.drawable.idle);
                        break;



                }
            }

        });
    }


}
