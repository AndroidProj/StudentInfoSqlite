package com.example.vasundhara.studentinfosqlite;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    public static final String TAG = MainActivity.class.getSimpleName();
    EditText first_et, last_et, mobile_et, email_et;
    Button insert_btn;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Boolean first_bool = false, last_bool = false, mobile_bool = false, email_bool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        first_et = (EditText) findViewById(R.id.et_first_name);
        last_et = (EditText) findViewById(R.id.et_last_name);
        mobile_et = (EditText) findViewById(R.id.et_mobile_name);
        email_et = (EditText) findViewById(R.id.et_email_name);
        insert_btn = (Button) findViewById(R.id.btn_insert);
        if (DisplayActivity.status) {
            insert_btn.setText("update");
        }
        databaseHelper = new DatabaseHelper(MainActivity.this);
        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_et.getText().toString().trim().length() < 1) {
                    first_et.setError("Please Fill The Field");
                } else if (first_et.getText().toString().trim().length() >= 1) {
                    first_bool = true;
                }
                if (last_et.getText().toString().trim().length() < 1) {
                    last_et.setError("Please Fill The Field");
                } else if (last_et.getText().toString().trim().length() >= 1) {
                    last_bool = true;
                }
                if (mobile_et.getText().toString().trim().length() < 10) {
                    mobile_et.setError("Please Enter 10 Digit Of Number");
                } else if (mobile_et.getText().toString().trim().length() == 10) {
                    mobile_bool = true;
                }
                if (!email_et.getText().toString().matches(emailPattern)) {
                    email_et.setError("Please Enter Valid EmailAddress");
                } else if (email_et.getText().toString().matches(emailPattern)) {
                    email_bool = true;
                }

                if (first_bool && last_bool && mobile_bool && email_bool) {
                    if (insert_btn.getText().toString().equalsIgnoreCase("insert")) {
                        String mobile = mobile_et.getText().toString();
                        boolean result = databaseHelper.insertData(first_et.getText().toString(),
                                last_et.getText().toString(),
                                Long.parseLong(mobile),
                                email_et.getText().toString());
                        if (result) {
                            Log.e(TAG, "Data Are Successfully Inserted...");
                            Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                            startActivity(intent);

                        } else {
                            Log.e(TAG, "Data Are Not Inserted...");
                        }
                    }
                    if (insert_btn.getText().toString().equalsIgnoreCase("update")) {
                        String mobile = mobile_et.getText().toString();
                        boolean result = databaseHelper.updateData(DisplayActivity.dbId,
                                first_et.getText().toString(),
                                last_et.getText().toString(),
                                Long.parseLong(mobile),
                                email_et.getText().toString());
                        if (result) {
                            Log.e(TAG, "Data Are Successfully Updated...");
                            Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.e(TAG, "Data Are Not Updated...");
                        }
                    }
                }
            }
        });
    }
}
