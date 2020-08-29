/**
 * Created with JackHou
 * Date: 2020/8/28
 * Time: 18:55
 * Description:
 */

package com.jackhou.androidipc;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.jackhou.androidipc.aidl.BookManagerService;
import com.jackhou.androidipc.bean.Book;

import androidx.annotation.Nullable;

/**
 * Author: JackHou
 * Date: 2020/8/28.
 */
public class TestService extends Service {

    private IBookManager mIBookManager;

    private class TestConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mIBookManager = IBookManager.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIBookManager = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final int[] i = {1000};
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, new TestConnection(), BIND_AUTO_CREATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        mIBookManager.addBook(new Book(i[0], "book " + i[0]));
                        i[0]++;
                    } catch (InterruptedException | RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
