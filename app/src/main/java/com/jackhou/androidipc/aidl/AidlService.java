/**
 * Created with JackHou
 * Date: 2020/8/28
 * Time: 15:39
 * Description:
 */

package com.jackhou.androidipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jackhou.androidipc.IReporter;

import androidx.annotation.Nullable;

/**
 * Author: JackHou
 * Date: 2020/8/28.
 */
public class AidlService extends Service {

    private static final String TAG = "AidlService";

    public static final class Reporter extends IReporter.Stub {

        @Override
        public int report(String value, int type) throws RemoteException {
            Log.i(TAG, "report, value = " + value);
            return type;
        }
    }

    private Reporter mReporter;

    public AidlService() {
        mReporter = new Reporter();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mReporter;
    }
}
