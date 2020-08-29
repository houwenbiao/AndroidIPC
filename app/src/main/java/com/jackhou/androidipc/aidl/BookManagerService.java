/**
 * Created with JackHou
 * Date: 2020/8/28
 * Time: 16:47
 * Description:
 */

package com.jackhou.androidipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.jackhou.androidipc.IBookChangeListener;
import com.jackhou.androidipc.IBookManager;
import com.jackhou.androidipc.bean.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.Nullable;

/**
 * Author: JackHou
 * Date: 2020/8/28.
 */
public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";
    private CopyOnWriteArrayList mBookList = new CopyOnWriteArrayList();
    private RemoteCallbackList<IBookChangeListener> mCallbackList = new RemoteCallbackList<>();

    private class BookManager extends IBookManager.Stub {

        @Override
        public int addBook(Book book) throws RemoteException {
            onBookAdd(book);
            Log.d(TAG, "Add book success!");
            return book.getId();
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void registerListener(IBookChangeListener listener) throws RemoteException {
            mCallbackList.register(listener);
        }

        @Override
        public void unregisterListener(IBookChangeListener listener) throws RemoteException {
            mCallbackList.unregister(listener);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    private BookManager mBookManager;

    public BookManagerService() {
        mBookManager = new BookManager();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBookManager;
    }

    /**
     * 一本书被添加进来的处理逻辑
     */
    private void onBookAdd(Book book) {
        mBookList.add(book);
        int n = mCallbackList.beginBroadcast();
        for (int i = 0; i < n; i++) {
            IBookChangeListener listener = mCallbackList.getBroadcastItem(i);
            try {
                listener.onBookAdd(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallbackList.finishBroadcast();
    }
}
