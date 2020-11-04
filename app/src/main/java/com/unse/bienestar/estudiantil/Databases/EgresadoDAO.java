package com.unse.bienestar.estudiantil.Databases;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.Egresado;

import java.util.ArrayList;

public class EgresadoDAO {

    static String createTable() {
        return String.format("create table %s(%s %s %s,%s %s %s,%s %s %s" +
                        ", primary key(%s))",
                Egresado.TABLE,
                Egresado.KEY_ID_EGR, Utils.INT_TYPE, Utils.NULL_TYPE,
                Egresado.KEY_PROFESION, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Egresado.KEY_FECHA_EGRESO, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Egresado.KEY_ID_EGR);
    }

    private Egresado mEgresado;

    public EgresadoDAO(Context context) {
        Utils.initBD(context);
        mEgresado = new Egresado();
    }

    private ContentValues loadValues(Egresado Egresado, int tipo) {
        ContentValues values = new ContentValues();
        if (tipo == 1)
            values.put(Egresado.KEY_ID_EGR, Egresado.getIdEgresado());
        values.put(Egresado.KEY_PROFESION, Egresado.getProfesion());
        values.put(Egresado.KEY_FECHA_EGRESO, Egresado.getFechaEgreso());

        return values;
    }

    private Egresado loadDataFromCursor(Cursor cursor) {
        mEgresado = new Egresado();
        mEgresado.setIdEgresado(cursor.getInt(0));
        mEgresado.setProfesion(cursor.getString(1));
        mEgresado.setFechaEgreso(cursor.getString(2));
        return mEgresado;
    }

    public int insert(Egresado Egresado) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        int x = (int) db.insert(Egresado.TABLE, null, loadValues(Egresado, 1));
        //Egresado.getHorarios().setId(x);
        //mHorariosRepo.insertHorario(Egresado.getHorarios());
        DBManager.getInstance().closeDatabase();
        return (int) x;
    }

    public void update(Egresado Egresado) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        String id = String.valueOf(Egresado.getIdEgresado());
        String selection = Egresado.KEY_ID_EGR + " = " + id;
        db.update(Egresado.TABLE, loadValues(Egresado, 1), selection, null);
        DBManager.getInstance().closeDatabase();
    }

    public Egresado get(int id) {
        mEgresado = new Egresado();
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("select * from " + Egresado.TABLE + " where " +
                Egresado.KEY_ID_EGR + " = " + id, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mEgresado = loadDataFromCursor(cursor);
            cursor.close();
        }
        DBManager.getInstance().closeDatabase();
        return mEgresado;
    }

    public ArrayList<Egresado> getAll() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<Egresado> list = new ArrayList<Egresado>();
        String query = String.format("select * from %s", Egresado.TABLE);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                mEgresado = loadDataFromCursor(cursor);
                list.add(mEgresado);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;

    }

    public void deleteAll() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        db.delete(Egresado.TABLE, null, null);
        DBManager.getInstance().closeDatabase();
    }
}


