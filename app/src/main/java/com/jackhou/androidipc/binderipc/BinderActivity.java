/**
 * Created with JackHou
 * Date: 2020/8/28
 * Time: 15:30
 * Description:
 */

package com.jackhou.androidipc.binderipc;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jackhou.androidipc.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Author: JackHou
 * Date: 2020/8/28.
 */
public class BinderActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private IBinder mReporterBind;
    private Button mButtonSend;

    private class BindConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "onServiceConnected");
            mReporterBind = iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "onServiceDisconnected");
            mReporterBind = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);
        mButtonSend = findViewById(R.id.btn);
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInterfaceToken("reporter");
                data.writeString("this is a test string.");
                data.writeInt(0);

                try {
                    mReporterBind.transact(BinderService.REPORT_CODE, data, reply, 0);
                    reply.enforceInterface("reporter");
                    int result = reply.readInt();
                    Log.i(TAG, "result: " + result);
                    data.recycle();
                    reply.recycle();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        Intent intent = new Intent(this, BinderService.class);
        bindService(intent, new BindConnection(), BIND_AUTO_CREATE);
    }
}
