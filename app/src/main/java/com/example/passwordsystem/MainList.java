package com.example.passwordsystem;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Currency;

public class MainList extends AppCompatActivity {
    static SQLiteDatabase database;
    static ArrayList<String> list = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    ListView listView;
    String username;
    ImageView settingImage;

    public void change() {
        Cursor cursor = database.rawQuery("SELECT  * from DATA", null);
        int nameIndex = cursor.getColumnIndex("name");
        if (cursor.moveToFirst()) {
            list.clear();
            do {
                list.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(arrayAdapter);
        }
    }

    public void addNew(View view) {
        Intent intent = new Intent(getApplicationContext(), Editor.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        Intent intent = getIntent();
        listView = findViewById(R.id.listView);
        username = intent.getStringExtra("username");
        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("com.example.passwordsystem", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("userName","");
        Log.i("hereMe",username);
        sharedPreferences= getApplicationContext().getSharedPreferences("com.example.passwordsystem", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("fingerPrinterror",false).apply();



        //setting call
        settingImage=findViewById(R.id.setting_Image);
        settingImage.setClickable(true);
        settingImage.bringToFront();
        settingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("Msg","Setting Image clicked");
                Intent intent=new Intent(getApplicationContext(),Setting_page.class);
                startActivity(intent);
            }
        });





        Log.i("here","Mainlist1");
        try{
        database = openOrCreateDatabase(username,MODE_PRIVATE, null);}
        catch (Exception e){
        Log.i("here","Mainlist");}
        database.execSQL("CREATE TABLE IF NOT EXISTS DATA(id INTEGER PRIMARY KEY,name VARCHAR2, username VARCHAR2,password VARCHAR2)");
        change();
        //editing
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = list.get(position);
                Log.i("Got", name);
                Intent intent = new Intent(getApplicationContext(), viewer.class);
                intent.putExtra("username", username);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        //deleting
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainList.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are Sure?").setMessage("Do you want to Delte This")
                        .setNegativeButton("NO", null)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String seq = "DELETE from DATA where name=" + "'" + list.get(position) + "'";
                                database.execSQL(seq);
                                change();
                            }
                        }).show();

                return true;
            }
        });



    }
}
