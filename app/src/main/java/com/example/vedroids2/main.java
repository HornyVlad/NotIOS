package com.example.vedroids2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;


public class main extends Activity {
    SharedPreferences sharedPref;

    DatabaseHandler db = new DatabaseHandler(this);
    List<User> userList;
    EditText loginField;
    EditText passwordField;
    TextView attentionText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloact);

        loginField = findViewById(R.id.editTextName);
        passwordField = findViewById(R.id.editTextTextPassword);

        attentionText = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyThread(handler, db).authorization();
            }
        });
    }

    final Looper looper = Looper.getMainLooper();
    final Message message = Message.obtain();
    final Handler handler = new Handler(looper){
        public void handleMessage(Message msg){

            userList = (List<User>)msg.obj;
            boolean access = false;

            for(int i = 0; i < userList.size(); i++) {
                if (loginField.getText().toString().equals(userList.get(i)._login) &&
                        passwordField.getText().toString().equals(userList.get(i)._pass)) {
                    Intent intent = new Intent(main.this, MyList.class);
                    String name = loginField.getText().toString();
                    intent.putExtra("hello", "Привет " + name);
                    intent.putExtra("account", userList.get(i)._login);
                    finish();
                    main.this.startActivity(intent);
                    access = true;
                }
            }
            if(!access)
                attentionText.setText("Неверный логин или пароль");
        }
    };

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

        Log.i("AppLogger", "Приложение закрыто");
        Toast mytoast = new Toast(this);
        mytoast.makeText(main.this, "Приложение закрыто", Toast.LENGTH_SHORT).show();
    }

}
