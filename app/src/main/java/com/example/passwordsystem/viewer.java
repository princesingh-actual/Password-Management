package com.example.passwordsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class viewer extends AppCompatActivity {
    EditText name;
    EditText username;
    EditText password;
    String username1;
    ImageView write;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        name = findViewById(R.id.nameText_view);
        write=findViewById(R.id.imageView2);
        username = findViewById(R.id.usernameText_view);
        password = findViewById(R.id.passwordText_view);
        Intent intent = getIntent();
        username1 = intent.getStringExtra("username");
        final String nameEdit = intent.getStringExtra("name");
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

        write.bringToFront();
        write.setClickable(true);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Editor.class);
                intent.putExtra("username", username1);
                intent.putExtra("name", nameEdit);
                startActivity(intent);
            }
        });
    }
}
