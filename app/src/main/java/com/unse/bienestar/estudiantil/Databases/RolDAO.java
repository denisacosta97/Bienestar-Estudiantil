package com.unse.bienestar.estudiantil.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.Rol;

import java.util.ArrayList;

public class RolDAO {

    static String createTable() {
        return String.format("create table %s(%s %s %s,%s %s %s,%s %s %s,%s %s %s" +
                        ", primary key(%s, %s))",
                Rol.TABLE,
                Rol.KEY_ROL, Utils.INT_TYPE, Utils.NULL_TYPE,
                Rol.KEY_USER, Utils.INT_TYPE, Utils.NULL_TYPE,
                Rol.KEY_DESCRIPCON, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Rol.KEY_ID_PADRE, Utils.INT_TYPE, Utils.NULL_TYPE,
                Rol.KEY_ROL, Rol.KEY_USER);
    }

    private Rol mRol;

    public RolDAO(Context context) {
        Utils.initBD(context);
        mRol = new Rol();
    }

    private ContentValues loadValues(Rol Rol, int tipo) {
        ContentValues values = new ContentValues();
        if (tipo == 1)
            values.put(Rol.KEY_ROL, Rol.getIdRol());
        values.put(Rol.KEY_USER, Rol.getIdUsuario());
        values.put(Rol.KEY_DESCRIPCON, Rol.getDescripcion());
        values.put(Rol.KEY_ID_PADRE, Rol.getIdRolPadre());

        return values;
    }

    private Rol loadDataFromCursor(Cursor cursor) {
        mRol = new Rol();
        mRol.setIdRol(cursor.getInt(0));
        mRol.setIdUsuario(cursor.getInt(1));
        mRol.setDescripcion(cursor.getString(2));
        mRol.setIdRolPadre(cursor.getInt(2));
        return mRol;
    }

    public int insert(Rol Rol) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        int x = (int) db.insert(Rol.TABLE, null, loadValues(Rol, 1));
        DBManager.getInstance().closeDatabase();
        return (int) x;
    }

    public void update(Rol Rol) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        String id = String.valueOf(Rol.getIdRol());
        String selection = Rol.KEY_ROL + " = " + id;
        db.update(Rol.TABLE, loadValues(Rol, 1), selection, null);
        DBManager.getInstance().closeDatabase();
    }

    public Rol get(int id) {
        mRol = new Rol();
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("select * from " + Rol.TABLE + " where " +
                Rol.KEY_ROL + " = " + id, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mRol = loadDataFromCursor(cursor);
            cursor.close();
        }
        DBManager.getInstance().closeDatabase();
        return mRol;
    }

    public ArrayList<Rol> getAll() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<Rol> list = new ArrayList<Rol>();
        String query = String.format("select * from %s", Rol.TABLE);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                mRol = loadDataFromCursor(cursor);
                list.add(mRol);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;

    }

    public ArrayList<Rol> getAllByUser(int user) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<Rol> list = new ArrayList<Rol>();
        String query = String.format("select * from %s where %s = %s", Rol.TABLE, Rol.KEY_USER, user);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                mRol = loadDataFromCursor(cursor);
                list.add(mRol);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;

    }

    public void deleteAll() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        db.delete(Rol.TABLE, null, null);
        DBManager.getInstance().closeDatabase();
    }
}



