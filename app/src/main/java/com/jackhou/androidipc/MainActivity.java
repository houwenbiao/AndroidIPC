/**
 * IPC的client端
 */

package com.jackhou.androidipc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jackhou.androidipc.aidl.BookActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButton = findViewById(R.id.btn_jump);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        BookActivity.class);
                startActivity(intent);
            }
        });

//        Intent intent = new Intent(this, TestService.class);
//        startService(intent);
    }
}