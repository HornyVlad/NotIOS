package com.example.vedroids2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyList extends Activity {

    ArrayList<String> myStringArray;
    int index = 0;
    List<User> userList;
    DatabaseHandler db = new DatabaseHandler(this);
    EditText dataLog;
    ArrayAdapter<String> TextAdapter;
    User currentUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        dataLog = findViewById(R.id.editLogin);

        //Создание списка
        myStringArray = new ArrayList<String>();
        userList  = db.getAllUsers();
        for(int i = 0; i < userList.size(); i++)
        {
            myStringArray.add(userList.get(i)._login + "\t" + userList.get(i)._pass);
        }

        //Определяем текущего пользователя
        Bundle arguments = getIntent().getExtras();
        int userId = arguments.getInt("account");
        currentUser = (User)getIntent().getSerializableExtra("account1");

        //Создание динамического списка
        TextAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myStringArray);
        ListView textList = findViewById(R.id.textList);
        textList.setAdapter(TextAdapter);

        //Обновление списка
        TextAdapter.notifyDataSetChanged();

        //Диалоговое окно
        AlertDialog.Builder builder = new AlertDialog.Builder(MyList.this);
        final EditText et = new EditText(MyList.this);
        et.setHint("Пароль");
        builder.setView(et).setTitle("Укажите новый пароль пользователя").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.updateUser(currentUser, et.getText().toString());
                currentUser.setPass(et.getText().toString());
                myStringArray.remove(userId);
                myStringArray.add(userId, currentUser.getLogin() + "\t" + currentUser.getPass());
                TextAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });

        // create alert dialog
        AlertDialog alertDialog = builder.create();

        //Функционал кнопка "добавить"
        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText dataPass = findViewById(R.id.editPassword);
                userList.add(new User(dataLog.getText().toString(), dataPass.getText().toString()));
                db.addUser(new User(dataLog.getText().toString(), dataPass.getText().toString()));
                myStringArray.add(dataLog.getText().toString() + "\t" + dataPass.getText().toString());
                TextAdapter.notifyDataSetChanged();
            }
        });

        //Функционал кнопки "удалить"
        Button buttonDel = findViewById(R.id.buttonDel);
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < userList.size(); i++)
                {
                    if(dataLog.getText().toString().equals(userList.get(i)._login)){
                        db.deleteUser(userList.get(i));
                        myStringArray.remove(i);
                        userList.remove(i);
                        i--;
                        TextAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        Button buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show it
                alertDialog.show();
            }
        });
    }

    final Looper looper = Looper.getMainLooper();
    final Handler handler = new Handler(looper){
        public void handleMessage(Message msg){
            switch (msg.sendingUid)
            {
                case 1:
                    userList  = (List<User>) msg.obj;
                    for(int i = 0; i < userList.size(); i++)
                    {
                        myStringArray.add(userList.get(i)._login + "\t" + userList.get(i)._pass);
                    }
                    break;
                case 2:
                    userList  = (List<User>) msg.obj;
                    for(int i = 0; i < userList.size(); i++)
                    {
                        if(dataLog.getText().toString().equals(userList.get(i)._login)){
                            new MyThread(handler, db).deleteElement(userList.get(i));
                            myStringArray.remove(i);
                            userList.remove(i);
                            i--;
                            TextAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case 3:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(MyList.this, main.class);

        finish();
        MyList.this.startActivity(intent);
    }
}
