package com.unse.bienestar.estudiantil.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.Profesor;

import java.util.ArrayList;

public class ProfesorDAO {

    static String createTable() {
        return String.format("create table %s(%s %s %s,%s %s %s,%s %s %s" +
                        ", primary key(%s))",
                Profesor.TABLE,
                Profesor.KEY_ID_PRO, Utils.INT_TYPE, Utils.NULL_TYPE,
                Profesor.KEY_PROFESION, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Profesor.KEY_FECHA_INGRESO, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Profesor.KEY_ID_PRO);
    }

    private Profesor mProfesor;

    public ProfesorDAO(Context context) {
        Utils.initBD(context);
        mProfesor = new Profesor();
    }

    private ContentValues loadValues(Profesor Profesor, int tipo) {
        ContentValues values = new ContentValues();
        if (tipo == 1)
            values.put(Profesor.KEY_ID_PRO, Profesor.getIdProfesor());
        values.put(Profesor.KEY_PROFESION, Profesor.getProfesion());
        values.put(Profesor.KEY_FECHA_INGRESO, Profesor.getFechaIngreso());

        return values;
    }

    private Profesor loadDataFromCursor(Cursor cursor) {
        mProfesor = new Profesor();
        mProfesor.setIdProfesor(cursor.getInt(0));
        mProfesor.setProfesion(cursor.getString(1));
        mProfesor.setFechaIngreso(cursor.getString(2));
        return mProfesor;
    }

    public int insert(Profesor Profesor) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        int x = (int) db.insert(Profesor.TABLE, null, loadValues(Profesor, 1));
        //Profesor.getHorarios().setId(x);
        //mHorariosRepo.insertHorario(Profesor.getHorarios());
        DBManager.getInstance().closeDatabase();
        return (int) x;
    }

    public void update(Profesor Profesor) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        String id = String.valueOf(Profesor.getIdProfesor());
        String selection = Profesor.KEY_ID_PRO + " = " + id;
        db.update(Profesor.TABLE, loadValues(Profesor, 1), selection, null);
        DBManager.getInstance().closeDatabase();
    }

    public Profesor get(int id) {
        mProfesor = new Profesor();
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("select * from " + Profesor.TABLE + " where " +
                Profesor.KEY_ID_PRO + " = " + id, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mProfesor = loadDataFromCursor(cursor);
            cursor.close();
        }
        DBManager.getInstance().closeDatabase();
        return mProfesor;
    }

    public ArrayList<Profesor> getAll() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<Profesor> list = new ArrayList<Profesor>();
        String query = String.format("select * from %s", Profesor.TABLE);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                mProfesor = loadDataFromCursor(cursor);
                list.add(mProfesor);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;

    }

    public void deleteAll() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        db.delete(Profesor.TABLE, null, null);
        DBManager.getInstance().closeDatabase();
    }
}



