package com.example.vedroids2;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.util.List;

public class MyThread {
    Handler handler;

    DatabaseHandler db;
    final Message message = Message.obtain();

    MyThread(Handler main_handler, DatabaseHandler db){
        this.db = db;
        this.handler = main_handler;
    }

    public void authorization(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List <User> userList = db.getAllUsers();
                message.obj = userList;
                handler.sendMessage(message);
            }
        }).start();
    }
    public void initialization(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> userList  = db.getAllUsers();
                message.sendingUid = 1;
                message.obj = userList;
                handler.sendMessage(message);
            }
        }).start();
    }

    public void initializationLite(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> userList  = db.getAllUsers();
                message.sendingUid = 2;
                message.obj = userList;
                handler.sendMessage(message);
            }
        }).start();
    }
    public void addElement(String string1, String string2){
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.addUser(new User(string1, string2));
            }
        }).start();
    }

    public void deleteElement(User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.deleteUser(user);
            }
        }).start();
    }
}
