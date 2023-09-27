package com.example.vedroids2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        //Вывод имени
        Bundle arguments = getIntent().getExtras();
        String str = arguments.get("hello").toString();
        Toast.makeText(MyList.this, str, Toast.LENGTH_SHORT).show();

        //Создание списка
        myStringArray = new ArrayList<String>();
        List<User> userList  = db.getAllUsers();
        ArrayAdapter<String> TextAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myStringArray);
        ListView textList = findViewById(R.id.textList);
        for(int i = 0; i < userList.size(); i++)
        {
            myStringArray.add(userList.get(i)._login + "\t" + userList.get(i)._pass);
        }
        textList.setAdapter(TextAdapter);
        TextAdapter.notifyDataSetChanged();

        //Диалоговое окно
        AlertDialog.Builder builder = new AlertDialog.Builder(MyList.this);
        final EditText et = new EditText(MyList.this);
        et.setHint("Пароль");
        String login = arguments.get("account").toString();
        builder.setView(et).setTitle("Укажите новый пароль пользователя").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0; i < userList.size(); i++)
                        {
                            if(login.equals(userList.get(i)._login)){
                                db.deleteUser(userList.get(i));
                                userList.get(i).setPass(et.getText().toString());
                                db.addUser(new User(userList.get(i)._login, userList.get(i)._pass));
                                myStringArray.remove(i);
                                myStringArray.add(i, userList.get(i)._login + "\t" + userList.get(i)._pass);
                                textList.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textList.setAdapter(TextAdapter);
                                        TextAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                        dialog.cancel();
                    }
                }).start();
            }
        });
        AlertDialog alertDialog = builder.create();

        //Функционал кнопки "добавить"
        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EditText dataLog = findViewById(R.id.editLogin);
                        EditText dataPass = findViewById(R.id.editPassword);
                        userList.add(new User(dataLog.getText().toString(), dataPass.getText().toString()));
                        db.addUser(new User(dataLog.getText().toString(), dataPass.getText().toString()));
                        myStringArray.add(dataLog.getText().toString() + "\t" + dataPass.getText().toString());
                        textList.post(new Runnable() {
                            @Override
                            public void run() {
                                textList.setAdapter(TextAdapter);
                                TextAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            }
        });

        //Функционал кнопки "удалить"
        Button buttonDel = findViewById(R.id.buttonDel);
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EditText dataLog = findViewById(R.id.editLogin);
                        for(int i = 0; i < userList.size(); i++)
                        {
                            if(dataLog.getText().toString().equals(userList.get(i)._login)){
                                db.deleteUser(userList.get(i));
                                myStringArray.remove(i);
                                userList.remove(i);
                                i--;
                                textList.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textList.setAdapter(TextAdapter);
                                        TextAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }
                }).start();
            }
        });

        //Функицонал кнопки "изменить"
        Button buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show it
                alertDialog.show();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(MyList.this, main.class);

        finish();
        MyList.this.startActivity(intent);
    }
}
