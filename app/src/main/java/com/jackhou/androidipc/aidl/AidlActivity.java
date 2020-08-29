/**
 * Created with JackHou
 * Date: 2020/8/28
 * Time: 15:45
 * Description:
 */

package com.jackhou.androidipc.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jackhou.androidipc.IReporter;
import com.jackhou.androidipc.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AidlActivity extends AppCompatActivity {
    private static final String TAG = "AidlActivity";
    private IReporter mIReporter;

    private class AidlConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mIReporter = IReporter.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIReporter = null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.i(TAG, "---" + mIReporter.report("just a test!", 0));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        Intent intent = new Intent(this, AidlService.class);
        bindService(intent, new AidlConnection(), BIND_AUTO_CREATE);
    }
}
