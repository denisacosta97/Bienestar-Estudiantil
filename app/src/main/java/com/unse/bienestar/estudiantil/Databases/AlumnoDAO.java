package com.unse.bienestar.estudiantil.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unse.bienestar.estudiantil.Herramientas.Utils;
import com.unse.bienestar.estudiantil.Modelos.Alumno;

import java.util.ArrayList;


public class AlumnoDAO {

    static String createTable() {
        return String.format("create table %s(%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s" +
                        ", primary key(%s))",
                Alumno.TABLE,
                Alumno.KEY_ID_ALU, Utils.INT_TYPE, Utils.NULL_TYPE,
                Alumno.KEY_CARRERA, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Alumno.KEY_FACULTAD, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Alumno.KEY_ANIO, Utils.INT_TYPE, Utils.NULL_TYPE,
                Alumno.KEY_REGULARIDAD, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Alumno.KEY_LEGAJO, Utils.STRING_TYPE, "null",
                Alumno.KEY_ID_ALU);
        /*return String.format("create table %s(%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s" +
                        ", primary key(%s))",
                Alumno.TABLE,
                Alumno.KEY_ID_ALU, Utils.INT_TYPE, Utils.NULL_TYPE,
                Alumno.KEY_CARRERA, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Alumno.KEY_FACULTAD, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Alumno.KEY_ANIO, Utils.INT_TYPE, Utils.NULL_TYPE,
                Alumno.KEY_REGULARIDAD, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Alumno.KEY_ID_ALU);*/
    }

    private Alumno mAlumno;

    public AlumnoDAO(Context context) {
        Utils.initBD(context);
        mAlumno = new Alumno();
    }

    private ContentValues loadValues(Alumno alumno, int tipo) {
        ContentValues values = new ContentValues();
        if (tipo == 1)
            values.put(Alumno.KEY_ID_ALU, alumno.getIdAlumno());
        values.put(Alumno.KEY_FACULTAD, alumno.getFacultad());
        values.put(Alumno.KEY_CARRERA, alumno.getCarrera());
        values.put(Alumno.KEY_ANIO, alumno.getAnio());
        values.put(Alumno.KEY_REGULARIDAD, alumno.getIdRegularidad());
        values.put(Alumno.KEY_LEGAJO, alumno.getLegajo());
        return values;
    }

    private Alumno loadDataFromCursor(Cursor cursor) {
        mAlumno = new Alumno();
        mAlumno.setIdAlumno(cursor.getInt(0));
        mAlumno.setCarrera(cursor.getString(1));
        mAlumno.setFacultad(cursor.getString(2));
        mAlumno.setAnio(cursor.getString(3));
        mAlumno.setIdRegularidad(cursor.getInt(4));
        mAlumno.setLegajo(cursor.getString(5));
        return mAlumno;
    }

    public int insert(Alumno alumno) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        int x = (int) db.insert(Alumno.TABLE, null, loadValues(alumno, 1));
        //alumno.getHorarios().setId(x);
        //mHorariosRepo.insertHorario(alumno.getHorarios());
        DBManager.getInstance().closeDatabase();
        return (int) x;
    }

    public void update(Alumno alumno) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        String id = String.valueOf(alumno.getIdAlumno());
        String selection = Alumno.KEY_ID_ALU + " = " + id;
        db.update(Alumno.TABLE, loadValues(alumno, 1), selection, null);
        DBManager.getInstance().closeDatabase();
    }

    public Alumno get(int id) {
        mAlumno = new Alumno();
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("select * from " + Alumno.TABLE + " where " +
                Alumno.KEY_ID_ALU + " = " + id, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mAlumno = loadDataFromCursor(cursor);
            cursor.close();
        }
        DBManager.getInstance().closeDatabase();
        return mAlumno;
    }

    public ArrayList<Alumno> getAll() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<Alumno> list = new ArrayList<Alumno>();
        String query = String.format("select * from %s", Alumno.TABLE);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                mAlumno = loadDataFromCursor(cursor);
                list.add(mAlumno);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;

    }

    public void deleteAll() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        db.delete(Alumno.TABLE, null, null);
        DBManager.getInstance().closeDatabase();
    }
}

