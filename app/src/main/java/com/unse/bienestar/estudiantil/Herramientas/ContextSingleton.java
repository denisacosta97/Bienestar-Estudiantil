package com.unse.bienestar.estudiantil.Herramientas;

import android.content.Context;

public class ContextSingleton {

    private static ContextSingleton INSTANCE;
    private Context mContext;

    public ContextSingleton(Context context) {
        mContext = context;
    }

    public synchronized static ContextSingleton getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ContextSingleton(context);
        }
        return INSTANCE;
    }

    public Context getContext() {
        return mContext;
    }
}
