package com.example.eva3_aplicaciones;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class admin extends AppCompatActivity {
    private EditText txtCodigo, txtNombre, txtPrecio;
    private Button btnBuscar, btnAgregar, btnModificar, btnEliminar;
    private ListView listmodelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Vincular con los IDs del archivo XML
        txtCodigo = findViewById(R.id.edCodigo);
        txtNombre = findViewById(R.id.edNombre);
        txtPrecio = findViewById(R.id.edPrecio);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);
        listmodelo = findViewById(R.id.listmodelo);

        // Inicializar los métodos para funcionalidad de Firebase
        Buscar();
        Agregar();
        Modificar();
        Eliminar();
        ListarModelos();
    }

    private void Buscar() {
        btnBuscar.setOnClickListener(view -> {
            if (txtCodigo.getText().toString().trim().isEmpty()) {
                Toast.makeText(admin.this, "Ingrese el Código a buscar", Toast.LENGTH_LONG).show();
            } else {
                int codigo = Integer.parseInt(txtCodigo.getText().toString());
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference dbref = db.getReference(modelo.class.getSimpleName());

                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String strCodigo = Integer.toString(codigo);
                        boolean res = false;
                        for (DataSnapshot x : snapshot.getChildren()) {
                            if (strCodigo.equalsIgnoreCase(x.child("id").getValue().toString())) {
                                res = true;
                                txtNombre.setText(x.child("nombre").getValue().toString());
                                txtPrecio.setText(x.child("precio").getValue().toString());
                                break;
                            }
                        }
                        if (!res) {
                            Toast.makeText(admin.this, "Código (" + strCodigo + ") no encontrado", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejo de error
                    }
                });
            }
        });
    }

    private void Agregar() {
        btnAgregar.setOnClickListener(view -> {
            if (txtCodigo.getText().toString().trim().isEmpty()
                    || txtNombre.getText().toString().trim().isEmpty()
                    || txtPrecio.getText().toString().trim().isEmpty()) {
                Toast.makeText(admin.this, "Complete los campos faltantes", Toast.LENGTH_LONG).show();
            } else {
                int codigo = Integer.parseInt(txtCodigo.getText().toString());
                String nombre = txtNombre.getText().toString();
                double precio = Double.parseDouble(txtPrecio.getText().toString());

                // Crear la fecha y hora actual
                String fechaCreacion = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference dbref = db.getReference(modelo.class.getSimpleName());

                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean nombreExiste = false;
                        for (DataSnapshot x : snapshot.getChildren()) {
                            if (x.child("nombre").getValue().toString().equalsIgnoreCase(nombre)) {
                                nombreExiste = true;
                                break;
                            }
                        }
                        if (nombreExiste) {
                            Toast.makeText(admin.this, "El nombre " + nombre + " ya existe!", Toast.LENGTH_LONG).show();
                        } else {
                            modelo nuevoModelo = new modelo(codigo, nombre, precio, fechaCreacion);
                            dbref.push().setValue(nuevoModelo);
                            Toast.makeText(admin.this, "Modelo agregado!", Toast.LENGTH_LONG).show();
                            txtCodigo.setText("");
                            txtNombre.setText("");
                            txtPrecio.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejo de error
                    }
                });
            }
        });
    }

    private void Modificar() {
        btnModificar.setOnClickListener(view -> {
            if (txtCodigo.getText().toString().trim().isEmpty() || txtNombre.getText().toString().trim().isEmpty() ||
                    txtPrecio.getText().toString().trim().isEmpty()) {
                Toast.makeText(admin.this, "Complete los campos faltantes", Toast.LENGTH_LONG).show();
            } else {
                int codigo = Integer.parseInt(txtCodigo.getText().toString());
                String nombre = txtNombre.getText().toString();
                double precio = Double.parseDouble(txtPrecio.getText().toString());

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference dbref = db.getReference(modelo.class.getSimpleName());

                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean res = false;
                        for (DataSnapshot x : snapshot.getChildren()) {
                            if (x.child("id").getValue(Integer.class) == codigo) {
                                res = true;
                                x.getRef().child("nombre").setValue(nombre);
                                x.getRef().child("precio").setValue(precio);
                                txtCodigo.setText("");
                                txtNombre.setText("");
                                txtPrecio.setText("");
                                ListarModelos();
                                Toast.makeText(admin.this, "Modelo modificado correctamente!", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                        if (!res) {
                            Toast.makeText(admin.this, "Código (" + codigo + ") no encontrado,\n no se puede modificar !!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejo de error
                    }
                });
            }
        });
    }

    private void Eliminar() {
        btnEliminar.setOnClickListener(view -> {
            if (txtCodigo.getText().toString().trim().isEmpty()) {
                Toast.makeText(admin.this, "Ingrese el Código a eliminar!!", Toast.LENGTH_LONG).show();
            } else {
                int codigo = Integer.parseInt(txtCodigo.getText().toString());
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference dbref = db.getReference(modelo.class.getSimpleName());

                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean encontrado = false;
                        for (DataSnapshot x : snapshot.getChildren()) {
                            if (x.child("id").getValue(Integer.class) == codigo) {
                                encontrado = true;
                                AlertDialog.Builder a = new AlertDialog.Builder(admin.this);
                                a.setCancelable(false);
                                a.setTitle("Consulta");
                                a.setMessage("¿Está seguro de eliminar?");
                                a.setNegativeButton("Cancelar", null);
                                a.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                                    x.getRef().removeValue();
                                    ListarModelos();
                                    Toast.makeText(admin.this, "Modelo eliminado correctamente!", Toast.LENGTH_LONG).show();
                                });
                                a.show();
                                break;
                            }
                        }
                        if (!encontrado) {
                            Toast.makeText(admin.this, "Código (" + codigo + ") no encontrado", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    private void ListarModelos() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(modelo.class.getSimpleName());
        ArrayList<modelo> listModelos = new ArrayList<>();
        ArrayAdapter<modelo> adaptador = new ArrayAdapter<>(admin.this, R.layout.list_item, listModelos);
        listmodelo.setAdapter(adaptador);

        // Configuración del ChildEventListener para la base de datos
        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                modelo modelo = snapshot.getValue(modelo.class);
                listModelos.add(modelo);
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                modelo modelo = snapshot.getValue(modelo.class);
                for (int i = 0; i < listModelos.size(); i++) {
                    if (listModelos.get(i).getId() == modelo.getId()) {
                        listModelos.set(i, modelo);
                        break;
                    }
                }
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                modelo modelo = snapshot.getValue(modelo.class);
                listModelos.removeIf(m -> m.getId() == modelo.getId());
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        listmodelo.setOnItemClickListener((adapterView, view, i, l) -> {
            modelo modeloSeleccionado = listModelos.get(i);

            // Mostrar información detallada en un AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(admin.this);
            builder.setTitle("Detalles del modelo");
            builder.setMessage("ID: " + modeloSeleccionado.getId() +
                    "\nNombre: " + modeloSeleccionado.getNombre() +
                    "\nPrecio: " + modeloSeleccionado.getPrecio() +
                    "\nFecha de creación: " + modeloSeleccionado.getFechaCreacion());
            builder.setPositiveButton("Aceptar", null);
            builder.show();
        });
    }
}
