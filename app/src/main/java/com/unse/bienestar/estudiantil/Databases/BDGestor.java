package com.unse.bienestar.estudiantil.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unse.bienestar.estudiantil.Herramientas.Utils;


public class BDGestor extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Bienestar.db";
    private static final int DATABASE_VERSION = 2;


    public BDGestor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(AlumnoDAO.createTable());
        sqLiteDatabase.execSQL(EgresadoDAO.createTable());
        sqLiteDatabase.execSQL(ProfesorDAO.createTable());
        sqLiteDatabase.execSQL(RolDAO.createTable());
        sqLiteDatabase.execSQL(UsuarioDAO.createTable());


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            sqLiteDatabase.execSQL("ALTER TABLE alumno ADD legajo text");
        } else {
            Utils.showLog("SQL", "Actualizo BD " + oldVersion);
        }


    }
}
