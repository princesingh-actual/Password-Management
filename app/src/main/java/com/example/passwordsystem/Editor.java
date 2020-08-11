package com.example.passwordsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Editor extends AppCompatActivity {
    EditText name;
    EditText username;
    EditText password;
    String username1;
    String nameEdit;

    public void addNew(View view) {
        String seq = "Delete from DATA where name=" + "'" + nameEdit + "'";
        MainList.database.compileStatement(seq).execute();
        if (name.getText().toString().equals("") || username.getText().toString().equals("")  || password.getText().toString().equals("")) {
            Toast.makeText(this, "Please Fill valid Details", Toast.LENGTH_SHORT).show();
        } else {
            seq = " SELECT * from DATA where name=" + "'" + name.getText().toString() + "'";
            Cursor cursor = MainList.database.rawQuery(seq, null);
            if(cursor.moveToFirst()){
                Toast.makeText(this, "This name Already Exists", Toast.LENGTH_SHORT).show();

        }
        else{
                String sql = "INSERT INTO DATA(name,username,password) VALUES (?,?,?)";
                SQLiteStatement sqLiteStatement = MainList.database.compileStatement(sql);
                String t = name.getText().toString();
                String u = username.getText().toString();
                String y = password.getText().toString();
                try {
                    sqLiteStatement.bindString(1, t);
                    sqLiteStatement.bindString(2, u);
                    sqLiteStatement.bindString(3, y);
                    sqLiteStatement.execute();
                } catch (Exception e) {
                    Log.i("Eroor", String.valueOf(e));
                }
                cursor = MainList.database.rawQuery("SELECT  * from DATA", null);
                int nameIndex = cursor.getColumnIndex("name");
                if (cursor.moveToFirst()) {
                    MainList.list.clear();
                    do {
                        MainList.list.add(cursor.getString(nameIndex));
                    } while (cursor.moveToNext());
                }
                //MainList.arrayAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getApplicationContext(), MainList.class);
                intent.putExtra("username", username1);
                startActivity(intent);
        }}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        username1 = intent.getStringExtra("username");
        nameEdit = intent.getStringExtra("name");
        name = findViewById(R.id.nameText_view);
        username = findViewById(R.id.usernameText_view);
        password = findViewById(R.id.passwordText_view);
        if (nameEdit != null) {
            Log.i("name","Notnull");
            String seq = " SELECT * from DATA where name=" + "'" + nameEdit + "'";
            Cursor cursor = MainList.database.rawQuery(seq, null);
            int nameIndex = cursor.getColumnIndex("name");
            int usernameIndex = cursor.getColumnIndex("username");
            int passwordIndex = cursor.getColumnIndex("password");
            if (cursor.moveToFirst()) {
                name.setText(cursor.getString(nameIndex));
                username.setText(cursor.getString(usernameIndex));
                password.setText(cursor.getString(passwordIndex));
            }
        }
    }
}
