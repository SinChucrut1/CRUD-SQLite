package com.example.sqlcrud_semana13;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Instanciamos variables para su uso

    // Campos de texto de donde extraemos info
    EditText ID_Trabajador, NombreTrabajador, CargoTrabajador;
    // Botones para utilizar
    // Button btnRegistro, btnEliminar, btnBuscar, btnActualizar;
    // Lista donde se veran los trabajadores
    ListView Lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Identificar las variables de texto en el formulario
        ID_Trabajador = findViewById(R.id.txtID);
        NombreTrabajador = findViewById(R.id.txtNombre);
        CargoTrabajador = findViewById(R.id.txtCargo);
        Lista = findViewById(R.id.ListaTrabajadores);

        // Funcion para cargar trabajadores por defecto
        cargarTrabajadores();
    }

    // Crear funciones para CRUD de trabajadores
    // Nota: Se añade (View view) como parametro para
    //       asignar la funcion directamente al boton desde su propiedad "onClick"

    // Funcion para registrar trabajadores
    public void CrearTrabajador(View view){

        // llamamos a la clase sql (this, Nombre de la bd, null, la version 1)
        SQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion",null,1);

        // desde admin extraemos la base de datos para poder añadirle datos
        SQLiteDatabase bd = admin.getWritableDatabase();

        // creamos variables (opcional)
        // es mas comodo definir variable oficial y luego solo llamarla
        String id = ID_Trabajador.getText().toString();
        String nombre = NombreTrabajador.getText().toString();
        String cargo = CargoTrabajador.getText().toString();

        // validamos que los campos no esten vacios
        if(id.isEmpty() || nombre.isEmpty() || cargo.isEmpty()){
            Toast.makeText(this, "No se permiten campos vacios", Toast.LENGTH_SHORT).show();
        } else {
            // Se hace una "bolsita de datos" donde mandamos las variables
            ContentValues datosUser = new ContentValues();

            // A la bolsita de datos le hacemos un put, uno por cada columna que tenga la tabla
            // OJO: revisar que los nombres de las columnas coincidan con la de la clase
            datosUser.put("ID_Usuario", id);
            datosUser.put("NombreTrabajador", nombre);
            datosUser.put("CargoTrabajador", cargo);

            // ahora se realiza el insert
            bd.insert("Trabajadores",null, datosUser);

            // se cierra la base de datos
            bd.close();

            // limpiar campos de los EditText
            ID_Trabajador.setText("");
            NombreTrabajador.setText("");
            CargoTrabajador.setText("");

            // Funcion para cargar trabajadores una vez ingresado
            cargarTrabajadores();
        }
    }

    // Crear Funcion para cargar datos en el listview
    public void cargarTrabajadores(){

        // llamamos a la clase sql (this, Nombre de la bd, null, la version 1)
        SQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion",null,1);

        // desde admin extraemos la base de datos para poder añadirle datos
        SQLiteDatabase bd = admin.getWritableDatabase();
        // recorrer cada fila de la bd
        Cursor fila = bd.rawQuery("Select * from Trabajadores", null);
        // crear arraylist
        ArrayList<String> listaTrabajadores = new ArrayList<>();

        // consulta if para mostrar datos
        // se va a mover hasta el primer registro que encuentre
        if(fila.moveToFirst()){
            do {
                // extraemos los datos de las filas sql
                String ID = fila.getString(0);
                String Nombre = fila.getString(1);
                String Cargo = fila.getString(2);

                // creamos un string anidado de estas 3 variables
                String UserInfo = "ID: " + ID + " , Nombre: " + Nombre + ", Cargo: " + Cargo;

                // añadimos este super string al arraylist
                listaTrabajadores.add(UserInfo);

                // luego pasamos al sgte elemento de la consulta sql
                // y asi funcionara hasta que ya no haya elementos

            } while (fila.moveToNext());

        }
        // Cerramos la bd
        bd.close();
        // creamos un arrayadapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaTrabajadores);
        Lista.setAdapter(adapter);
    }


    // 05-11-2024
    // Funcion para buscar trabajador
    public void BuscarTrabajador(View view){
        // llamamos a la clase sql (this, Nombre de la bd, null, la version 1)
        SQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion",null,1);

        // desde admin extraemos la base de datos para poder añadirle datos
        SQLiteDatabase bd = admin.getWritableDatabase();

        // String del ID del trabajador a buscar
        String ID = ID_Trabajador.getText().toString();

        // Validar que el campo no este vacio
        if(!ID.isEmpty()){

            // Almacenamos la respuesta de la consulta sql en un CURSOR
            // El cursor va a encontrar algo en base al parametro que le pongamos, en este caso, la consulta sql
            Cursor fila = bd.rawQuery("Select NombreTrabajador, CargoTrabajador from Trabajadores WHERE ID_Usuario = " + ID, null);

            // si se encontro algo en la fila
            // El cursor se moverá al primer dato que encuentra
            if(fila.moveToFirst()){

                // definimos el texto que va a modificarse en los editText
                // Para eso, obtenemos los datos de las columnas en base a su numeracion
                NombreTrabajador.setText(fila.getString(0));
                CargoTrabajador.setText(fila.getString(1));

                // Opcional: desabilitar el campo de texto del ID, ya que BUSCAR se usa en conjunto con ACTUALIZAR
                ID_Trabajador.setEnabled(false);


            } else {
                // Si no se encontro nada, mostrar mensaje
                Toast.makeText(this, "El ID " + ID + " no se encuentra en la lista", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Si no hay nada en el editText del ID, lanzar alerta
            Toast.makeText(this, "El campo ID no puede estar vacio", Toast.LENGTH_SHORT).show();
        }
    }

    // Funcion para actualizar trabajadores
    // Se mezclan los metodos para agregar y buscar
    public void ActualizarTrabajador(View view){
        // llamamos a la clase sql (this, Nombre de la bd, null, la version 1)
        SQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion",null,1);

        // desde admin extraemos la base de datos para poder añadirle datos
        SQLiteDatabase bd = admin.getWritableDatabase();

        // String del ID del trabajador a buscar
        String ID = ID_Trabajador.getText().toString();
        String nombre = NombreTrabajador.getText().toString();
        String cargo = CargoTrabajador.getText().toString();

        // validar que los campos de nombre y de cargo no esten vacios
        if(!nombre.isEmpty() && !cargo.isEmpty()){
            // creamos una nueva bolsita de datos
            ContentValues nuevosDatos = new ContentValues();

            // le hacemos un put con los nuevos datos
            nuevosDatos.put("NombreTrabajador", nombre);
            nuevosDatos.put("CargoTrabajador", cargo);

            // se realiza un update en la BD
            // la almacenamos en un INT debido a que el update regresa un 0 o 1
            int cantidad = bd.update("Trabajadores", nuevosDatos, "ID_Usuario="+ID, null);

            // si devolvio 1, es porque se realizo el update correctamente
            if(cantidad == 1){

                // Notificamos en caso de exito
                Toast.makeText(this, "Los datos del trabajador se han actualizado correctamente", Toast.LENGTH_SHORT).show();

                // poner campos vacios
                ID_Trabajador.setText("");
                NombreTrabajador.setText("");
                CargoTrabajador.setText("");

                // Opcional: habilitar el campo de texto del ID, ya que BUSCAR se usa en conjunto con ACTUALIZAR
                ID_Trabajador.setEnabled(true);

                // refrescar la tabla
                cargarTrabajadores();

            } else {
                // En caso de devolver 0, es porque no existe el ID ingresado
                // NOTA: tambien puede dar el caso de que existan trabajadores con ID repetido
                // FIREBASE: crear BD en la nube (BUSCAR)
                Toast.makeText(this, "No se encontro el ID ingresado", Toast.LENGTH_SHORT).show();
            }
        } else {
            // notificar en caso de que los campos esten vacios
            Toast.makeText(this, "No pueden haber campos vacios", Toast.LENGTH_SHORT).show();
        }


    }
}