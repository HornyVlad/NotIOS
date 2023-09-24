package com.example.vedroids2;

import android.app.Activity;
import android.content.Context;
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
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class List extends Activity {

    ArrayList<String> myStringArray;
    SharedPreferences sharedPref;
    RelativeLayout myLayout;
    int backColor;
    int index = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        //Смена цвета фона
        Bundle arguments = getIntent().getExtras();
        backColor = arguments.getInt("color");
        myLayout = findViewById(R.id.listLayout);
        myLayout.setBackgroundColor(backColor);

        //Вывод имени
        String str = arguments.get("hello").toString();
        Toast.makeText(List.this, str, Toast.LENGTH_SHORT).show();

        //Создание списка
        myStringArray = new ArrayList<String>();
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        for(int i = 0; i < sharedPref.getInt("strNum", 0); i++) {
            myStringArray.add(sharedPref.getString("stroka" + i, ""));
        }

        //Создание динамического списка
        ArrayAdapter<String> TextAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myStringArray);
        ListView textList = findViewById(R.id.textList);
        textList.setAdapter(TextAdapter);

        //Обновление списка
        TextAdapter.notifyDataSetChanged();

        //Получение индекса строки, на которую нажали
        textList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
            }
        });

        //Функционал кнопка "добавить"
        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText data = findViewById(R.id.editTextData);
                myStringArray.add(data.getText().toString());
                TextAdapter.notifyDataSetChanged();
            }
        });

        //Функционал кнопки "удалить"
        Button buttonDel = findViewById(R.id.buttonDel);
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStringArray.remove(index);
                TextAdapter.notifyDataSetChanged();
                index = 0;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Intent intent = new Intent(List.this, main.class);

        editor.putInt("strNum", myStringArray.size());
        for (int i = 0; i < myStringArray.size(); i++) {
            editor.putString("stroka"+ i, "" + myStringArray.get(i).toString());
            editor.apply();
        }

        finish();
        List.this.startActivity(intent);
    }
}
