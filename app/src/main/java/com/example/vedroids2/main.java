package com.example.vedroids2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


public class main extends Activity {
    int backColor = Color.WHITE;
    SharedPreferences sharedPref;
    Switch blackThemeSwitch;
    boolean switchStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloact);

        ConstraintLayout myLayout = findViewById(R.id.mainLayout);
        blackThemeSwitch = findViewById(R.id.blackTheme);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        switchStatus = sharedPref.getBoolean("switchStatus", false);
        if(switchStatus){
            backColor = Color.GRAY;
            myLayout.setBackgroundColor(backColor);
        }
        blackThemeSwitch.setChecked(switchStatus);
        blackThemeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blackThemeSwitch.isChecked()) {
                    switchStatus = true;
                    backColor = Color.GRAY;
                    myLayout.setBackgroundColor(backColor);
                }
                else {
                    switchStatus = false;
                    backColor = Color.WHITE;
                    myLayout.setBackgroundColor(backColor);
                }
            }
        });

        String name = new String("Влад");
        String password = new String("123");

        EditText nameField = findViewById(R.id.editTextName);
        EditText passwordField = findViewById(R.id.editTextTextPassword);

        TextView attentionText = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(true)
                {
                    Intent intent = new Intent(main.this, List.class);
                    String name = nameField.getText().toString();
                    intent.putExtra("hello", "Привет " + name);
                    intent.putExtra("color", backColor);
                    finish();
                    main.this.startActivity(intent);
                }
                else attentionText.setText("Неверный номер или пароль");
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("AppLogger", "Сработал старт");
        Toast mytoast = new Toast(this);
        mytoast.makeText(main.this, "Сработал старт", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("AppLogger", "Сработало возобновление");
        Toast mytoast = new Toast(this);
        mytoast.makeText(main.this, "Сработало возобновление", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("AppLogger", "Сработала пауза");
        Toast mytoast = new Toast(this);
        mytoast.makeText(main.this, "Сработала пауза", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("AppLogger", "Работа прекращена");
        Toast mytoast = new Toast(this);
        mytoast.makeText(main.this, "Работа прекращена", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("switchStatus",switchStatus);
        editor.apply();
        Log.i("AppLogger", "Приложение закрыто");
        Toast mytoast = new Toast(this);
        mytoast.makeText(main.this, "Приложение закрыто", Toast.LENGTH_SHORT).show();
    }

}
