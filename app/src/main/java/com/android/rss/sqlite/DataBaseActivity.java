package com.android.rss.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.rss.R;

public class DataBaseActivity extends AppCompatActivity {

    // UI
    private Button btnBuscar, btnGuardar, btnUpdate, btnBorrar;
    private TextInputEditText etCI, etNombre, etCargo;
    private TextInputLayout inputID, inputNombre, inputTelef;
    // BD
    private DBHelper ayudaBD;
    // Valores de los editTexts
    private String ci, nombre, cargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        // Inicializar las varialbles a utilizar
        initView();

        // onClick del botón GUARDAR
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateAll())
                    guardar();
            }
        });

        // onClick del botón BUSCAR
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateID())
                    buscar();
            }
        });

        // onClick del botón BORRAR
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateID()) {
                    showDialogDelete();
                }
            }
        });

        // onClick del botón ACTUALIZAR
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateAll())
                    update();
            }
        });

    }

    private void initView() {
        // UI referencias
        btnBorrar = findViewById(R.id.btnBorrar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnGuardar = findViewById(R.id.btnGuardar);
        etCI = findViewById(R.id.etCI);
        etNombre = findViewById(R.id.etNombre);
        etCargo = findViewById(R.id.etCargo);
        inputID = findViewById(R.id.inputID);
        inputNombre = findViewById(R.id.inputNombre);
        inputTelef = findViewById(R.id.inputTelef);

        // inicializar el SQLite Helper
        ayudaBD = new DBHelper(this);
    }

    private void guardar() {
        try {
            // Instanciar la BD
            SQLiteDatabase db = ayudaBD.getWritableDatabase();

            // ContentValues es como una estructura de datos que nos permitirá guardar
            // los datos en formato clave valor
            ContentValues values = new ContentValues();

            values.put(Constants.COLUMN_CI, ci);
            values.put(Constants.COLUMN_NAME, nombre);
            values.put(Constants.COLUMN_JOB, cargo);

            // Almacenar en la variable IdGuardado el resultado del db.insert
            long IdGuardado = db.insert(Constants.TABLE_NAME, Constants.COLUMN_CI, values);

            // Guardado OK si es != -1
            if (IdGuardado != -1) {
                Toast.makeText(getApplicationContext(), getString(R.string.msg_guardar) + IdGuardado, Toast.LENGTH_SHORT).show();
                clearAll();
                requestFocus(etCI);
            } else {
                // El ID ya existe
                Toast.makeText(getApplicationContext(), R.string.id_existe, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // ERROR
            Toast.makeText(getApplicationContext(), getString(R.string.error) + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void update() {
        try {
            // Instanciar la BD
            SQLiteDatabase db = ayudaBD.getWritableDatabase();

            // ContentValues es como una estructura de datos que nos permitirá guardar
            // los datos en formato clave valor
            ContentValues values = new ContentValues();

            values.put(Constants.COLUMN_NAME, nombre);
            values.put(Constants.COLUMN_JOB, cargo);

            // Parámetros para la consulta
            String[] argSel = {ci};
            String selection = Constants.COLUMN_CI;

            // Almacenar en la variable count el resultado del db.update
            int count = db.update(Constants.TABLE_NAME, values, selection + "=?", argSel);

            // Actualizado OK si es != 0
            if (count != 0) {
                Toast.makeText(getApplicationContext(), R.string.msg_editar, Toast.LENGTH_SHORT).show();
                clearAll();
                requestFocus(etCI);
            } else {
                // ID no existe
                Toast.makeText(getApplicationContext(), R.string.ci_existe, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // ERROR
            Toast.makeText(getApplicationContext(), getString(R.string.error) + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void borrar() {
        try {
            // Instanciar la BD
            SQLiteDatabase db = ayudaBD.getWritableDatabase();

            // Criterio de seleccion
            String selection = Constants.COLUMN_CI;
            String[] argSel = {ci};

            // Sentencia delete
            db.delete(Constants.TABLE_NAME, selection + "=?", argSel);
            Toast.makeText(getApplicationContext(), getString(R.string.delete) + ci, Toast.LENGTH_SHORT).show();
            clearAll();
        } catch (Exception e) {
            // Error
            Toast.makeText(getApplicationContext(), getString(R.string.error) + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void buscar() {
        try {
            // Instanciar la BD
            SQLiteDatabase db = ayudaBD.getReadableDatabase();

            // Criterio de seleccion
            String[] argSel = {ci};
            String[] projection = {Constants.COLUMN_NAME, Constants.COLUMN_JOB};

            // El Cursor es para movernos dentro de los resultados de la consulta con el c.moveToFirst();
            @SuppressLint("Recycle") Cursor c =
                    db.query(Constants.TABLE_NAME,
                            projection,
                            Constants.COLUMN_CI + "=?",
                            argSel,
                            null,
                            null,
                            null);
            c.moveToFirst();

            // Ponemos los valores que retorna la consulta en los editTexts
            etNombre.setText(c.getString(0));
            etCargo.setText(c.getString(1));

        } catch (Exception e) {
            // Error
            Toast.makeText(getApplicationContext(), R.string.msg_buscar, Toast.LENGTH_SHORT).show();
            clear();
        }
    }

    /**
     * Validar que el campo CI no esté vacío
     *
     * @return isValid
     */
    private boolean validateID() {
        boolean isValid = true;
        if (etCI.getText().toString().trim().isEmpty()) {
            inputID.setError(getResources().getString(R.string.validate));
            isValid = false;
        } else {
            ci = etCI.getText().toString().trim();
            inputID.setErrorEnabled(false);
        }
        return isValid;
    }

    /**
     * Validar que ningún campo esté vacío
     *
     * @return isValid
     */
    private boolean validateAll() {
        boolean isValid = true;
        if (etCI.getText().toString().trim().isEmpty()) {
            inputID.setError(getResources().getString(R.string.validate));
            requestFocus(etCI);
            isValid = false;
        } else {
            ci = etCI.getText().toString().trim();
            inputID.setErrorEnabled(false);
        }
        if (etNombre.getText().toString().trim().isEmpty()) {
            inputNombre.setError(getResources().getString(R.string.validate));
            requestFocus(etNombre);
            isValid = false;
        } else {
            nombre = etNombre.getText().toString().trim();
            inputNombre.setErrorEnabled(false);
        }
        if (etCargo.getText().toString().trim().isEmpty()) {
            inputTelef.setError(getResources().getString(R.string.validate));
            requestFocus(etCargo);
            isValid = false;
        } else {
            cargo = etCargo.getText().toString().trim();
            inputTelef.setErrorEnabled(false);
        }
        return isValid;
    }

    /**
     * Borrar los textos del campo Nombre y Cargo
     */
    private void clear() {
        etNombre.setText("");
        etCargo.setText("");
        inputNombre.setErrorEnabled(false);
        inputTelef.setErrorEnabled(false);
    }

    /**
     * Borrar los textos de todos los campos
     */
    private void clearAll() {
        etCI.setText("");
        etNombre.setText("");
        etCargo.setText("");
        inputID.setErrorEnabled(false);
        inputNombre.setErrorEnabled(false);
        inputTelef.setErrorEnabled(false);
    }

    /**
     * requestFocus es para poner el cursor en el TextInputEditText que necesitemos
     *
     * @param view view
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * Dialogo para confirmar que queremos eliminar el registro
     */
    private void showDialogDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle(R.string.dialog_title);
        alertDialogBuilder.setMessage(getString(R.string.msg_dialog) + ci + "?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                borrar();
                            }
                        })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

}