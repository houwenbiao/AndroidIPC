// IBookManager.aidl
package com.jackhou.androidipc;
import com.jackhou.androidipc.bean.Book;
import com.jackhou.androidipc.IBookChangeListener;

// Declare any non-default types here with import statements

interface IBookManager {

    int addBook(in Book book);

    List<Book> getBookList();

    void registerListener(IBookChangeListener listener);

    void unregisterListener(IBookChangeListener listener);

}