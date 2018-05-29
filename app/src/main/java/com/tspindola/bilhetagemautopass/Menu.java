package com.tspindola.bilhetagemautopass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btSettings = findViewById(R.id.btSettings);
        Button btReader = findViewById(R.id.btStartNFC);
        Button btRegister = findViewById(R.id.btRegister);
        Button btLogs = findViewById(R.id.btLogs);

        btSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Settings.class);
                startActivity(intent);
            }
        });

        btReader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Reader.class);
                startActivity(intent);
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Register.class);
                startActivity(intent);
            }
        });

        btLogs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Writer.class);
                startActivity(intent);
            }
        });
    }
}
