package com.example.sqlcrud_semana13;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

// Esta clase va a obtener sus metodos a partir de otra clase

// Hacemos extends a la clase  SQLiteOpenHelper
// Para implementar sus metodos se hace ALT+Enter > Implements methods
// Tambien se debe crear un constructor de la misma forma ALT+Enter > Create Constructor (La primera opcion)
// De esa forma dejan de haber errores

// De esta forma creamos una clase de apoyo
// Es una maqueta basica, la que podemos modificar para nuestro trabajo
public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    // Constructor
    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Metodos especiales

    // En onCreate se genera la base de datos (SQLiteDatabase [ingrese nombre])
    @Override
    public void onCreate(SQLiteDatabase Produccion) {
        // Ahora dentro le crearemos una tabla Trabajadores
        Produccion.execSQL("CREATE TABLE Trabajadores(ID_Usuario int primary key NOT NULL, NombreTrabajador text not null, CargoTrabajador text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
