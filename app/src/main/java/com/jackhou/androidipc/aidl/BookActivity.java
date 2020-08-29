/**
 * Created with JackHou
 * Date: 2020/8/28
 * Time: 17:12
 * Description:
 */

package com.jackhou.androidipc.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jackhou.androidipc.IBookChangeListener;
import com.jackhou.androidipc.IBookManager;
import com.jackhou.androidipc.R;
import com.jackhou.androidipc.bean.Book;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BookActivity extends AppCompatActivity {
    private static final String TAG = "BookActivity";
    private TextView mTxtBooks, mTxtAllBooks;
    private int mIndex = 0;
    private IBookManager mIBookManager;
    private BookServiceConnection mBookServiceConnection;
    private IBookChangeListener mIBookChangeListener;

    private class BookServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mIBookManager = IBookManager.Stub.asInterface(iBinder);
            try {
                mIBookManager.registerListener(mIBookChangeListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIBookManager = null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        mTxtBooks = findViewById(R.id.txt_books);
        mTxtAllBooks = findViewById(R.id.txt_book_list);
        Button mAddBookBtn = findViewById(R.id.btn_add_book),
                mGetBookBtn = findViewById(R.id.btn_get_books);
        mIBookChangeListener = new IBookChangeListener.Stub() {
            @Override
            public void onBookAdd(final Book book) throws RemoteException {
                Log.d(TAG, "add book id = " + book.getId() + ",  name = " + book.getName());
            }
        };
        mAddBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIndex++;
                try {
                    final int bookId = mIBookManager.addBook(new Book(mIndex, "book" + mIndex));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTxtBooks.setText("新添加的bookId = " + bookId);
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        mGetBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final List<Book> allBooks = mIBookManager.getBookList();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTxtAllBooks.setText("All book names: " + Arrays.toString(allBooks.toArray()));
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        mBookServiceConnection = new BookServiceConnection();
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mBookServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIBookManager != null) {
            try {
                mIBookManager.unregisterListener(mIBookChangeListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mBookServiceConnection);
    }
}
