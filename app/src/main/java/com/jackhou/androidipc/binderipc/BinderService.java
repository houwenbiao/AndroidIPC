/**
 * Created with JackHou
 * Date: 2020/8/28
 * Time: 14:22
 * Description:IPC的server端
 * 运行在另一个线程中，所以log要在另一个线程中查看
 */

package com.jackhou.androidipc.binderipc;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BinderService extends Service {

    private static final String TAG = "BinderService";
    public static final int REPORT_CODE = 1;

    public final class Reporter extends Binder implements IReporter {

        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            Log.i(TAG, "onTransact, code = " + code);
            switch (code) {
                case REPORT_CODE:
                    data.enforceInterface("reporter");
                    String values = data.readString();
                    Log.i("IReporter", "data is '" + values + "'");
                    int type = data.readInt();
                    int result = report(values, type);
                    reply.writeInterfaceToken("reporter");
                    reply.writeInt(result);
                    return true;

                default:
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public int report(String value, int key) {
            return key;
        }
    }

    private Reporter mReporter;

    public BinderService() {
        mReporter = new Reporter();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mReporter;
    }
}
