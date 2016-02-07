package com.ujujzk.trycursorloader.app;

import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;

public class CursorLoggerProxy implements InvocationHandler {

    private Cursor cursor;

    private CursorLoggerProxy(Cursor cursor) {
        this.cursor = cursor;
    }

    public static Cursor wrap(Cursor cursor){
        return (Cursor) Proxy.newProxyInstance(cursor.getClass().getClassLoader(),
                new Class[]{Cursor.class},
                new CursorLoggerProxy(cursor));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Date start = new Date();
        try {
            return method.invoke(cursor, args);
        } catch (java.lang.reflect.InvocationTargetException e){
            Log.d("CURSOR PROXY", e.getMessage(), e);
            throw e.getTargetException();
        } finally {
            Log.d("CURSOR PROXY", method.getName() + " -> "  + (new Date().getTime() - start.getTime()) + " ms.");
        }
    }
}
