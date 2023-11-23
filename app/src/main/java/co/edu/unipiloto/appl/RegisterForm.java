package co.edu.unipiloto.appl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class RegisterForm extends AppCompatActivity implements View.OnClickListener {

    EditText etName, etUser, etPassword;
    Spinner sTypeUser;
    Button btnRegister;

    RequestQueue requestQueue;

    private static final String URL1 = "http://" + MainActivityUsuario.ip_server +"/app_db/save.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
        getSupportActionBar().setTitle("Registro");

        requestQueue = Volley.newRequestQueue(this);
        initUI();

        btnRegister.setOnClickListener(this);
    }

    public void btn_login(View view){
        startActivity(new Intent(getApplicationContext(),LoginForm.class));
    }

    private void initUI(){
        //EditText
        etName = findViewById(R.id.editTextFullNameR);
        etUser = findViewById(R.id.editTextUsernameR);
        etPassword = findViewById(R.id.editTextPasswordR);
        sTypeUser = findViewById(R.id.spinnerRoles);
        //Buttons
        btnRegister = findViewById(R.id.btnRegister);
    };

    private boolean isValidInput(String input) {
        // Utiliza una expresión regular para verificar si el input contiene solo letras y números
        return input.matches("^[a-zA-Z0-9@.+_-]+$");
    }
    private boolean isAlpha(String input) {
        return input.matches("^[a-zA-Z ]+$");
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnRegister) {
            String name = etName.getText().toString().trim();
            String user = etUser.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String user_type = String.valueOf(sTypeUser.getSelectedItem());

            if (name.isEmpty()) {
                Toast.makeText(this, "¡Campos vacios!", Toast.LENGTH_SHORT).show();
            } else if (!isAlpha(name)) {
                Toast.makeText(this, "Nombre no válido. Digite unicamente caracteres.", Toast.LENGTH_SHORT).show();
            } else if (user.isEmpty()) {
                Toast.makeText(this, "¡Campos vacios!", Toast.LENGTH_SHORT).show();
            } else if (!isValidInput(user)) {
                Toast.makeText(this, "Usuario no válido. No puede contener espacios", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(this, "¡Campos vacios!", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 5) {
                Toast.makeText(this, "La contraseña debe tener al menos " + 5 + " caracteres.", Toast.LENGTH_SHORT).show();
            } else if (!isValidInput(password)) {
                Toast.makeText(this, "Contraseña no válida. No puede contener espacios", Toast.LENGTH_SHORT).show();
            } else {
                createUser(name, user, password, user_type);
            }
        }
    }
    private void createUser(String name, String user, String password, String user_type) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("El usuario ya existe")) {
                            Toast.makeText(RegisterForm.this, "El usuario " +user +" ya se encuentra registrado, pruebe con otro usuario", Toast.LENGTH_SHORT).show();
                        } else {
                            if(response.equals("El usuario se ha creado exitosamente como Administrador")){
                                startActivity(new Intent(getApplicationContext(), MainActivityAdmin.class));
                            }else {
                                startActivity(new Intent(getApplicationContext(), MainActivityUsuario.class));
                            }
                            Toast.makeText(RegisterForm.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterForm.this, "User created incorrectly", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("user", user);
                params.put("password", password);
                params.put("user_type", user_type);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}