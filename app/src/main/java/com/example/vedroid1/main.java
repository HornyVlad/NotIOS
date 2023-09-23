package com.example.vedroid1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;


public class main extends Activity {

    private int i = 0, j = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloact);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = i + 1;
                TextView mainText = findViewById(R.id.text1);
                mainText.setText("Левая:" + i);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j = j + 1;
                TextView mainText = findViewById(R.id.text2);
                mainText.setText("Правая:" + j);
            }
        });
    }

}
