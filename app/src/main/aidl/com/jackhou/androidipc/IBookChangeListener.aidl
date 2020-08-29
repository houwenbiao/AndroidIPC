// IBookChangeListener.aidl
package com.jackhou.androidipc;
import com.jackhou.androidipc.bean.Book;

// Declare any non-default types here with import statements

interface IBookChangeListener {
    void onBookAdd(in Book book);
}
