package com.example.practica6calificaciones

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLite(context: Context, bdName: String, version: Int) :
    SQLiteOpenHelper(context, bdName, null, version) {

    // Se ejecuta cuando la base de datos se crea por primera vez
    override fun onCreate(db: SQLiteDatabase?) {

        // 1. Tabla Alumnos (Basada en tu foto)
        db?.execSQL("""
            CREATE TABLE alumnos (
                matricula TEXT PRIMARY KEY,
                nombre TEXT,
                carrera TEXT,
                email TEXT,
                avatar TEXT
            )
        """)

        // 2. Tabla Materias (Basada en tu foto)
        db?.execSQL("""
            CREATE TABLE materias (
                codigo TEXT PRIMARY KEY,
                nombre TEXT,
                creditos INTEGER
            )
        """)

        // 3. Tabla Calificaciones (Basada en tu foto)
        // Esta tabla relaciona Alumnos y Materias, y guarda las calificaciones
        db?.execSQL("""
            CREATE TABLE calificaciones (
                matricula TEXT,
                codigo TEXT,
                unidad1 REAL,
                unidad2 REAL,
                unidad3 REAL,
                unidad4 REAL,
                PRIMARY KEY (matricula, codigo),
                FOREIGN KEY (matricula) REFERENCES alumnos(matricula),
                FOREIGN KEY (codigo) REFERENCES materias(codigo)
            )
        """)
    }

    // Se ejecuta cuando cambias la VERSIÓN de la base de datos
    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        // La forma más simple de actualizar es borrar todo y volver a crearlo
        db?.execSQL("DROP TABLE IF EXISTS calificaciones")
        db?.execSQL("DROP TABLE IF EXISTS materias")
        db?.execSQL("DROP TABLE IF EXISTS alumnos")
        // Vuelve a llamar a onCreate para recrear las tablas con el nuevo esquema
        onCreate(db)
    }
}