package com.example.ejemplosqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLite (context: Context, bdName:String, version:Int): SQLiteOpenHelper(context, bdName, null, version){
    //esta se ejecuta cuando el archivo se crea
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table alumnos (matricula TEXT, nombre TEXT, carrera TEXT)")
        db?.execSQL("create table materia (codigoM TEXT, nombre TEXT, creditos TEXT)")
        db?.execSQL("create table cursa (matricula TEXT, codigoM TEXT)")
    }

    //esto cuando se
    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {

    }

}