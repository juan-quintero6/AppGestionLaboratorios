package co.edu.unipiloto.appl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Button btnRegister;

    RequestQueue requestQueue;

    private static final String URL1 = "http://192.168.80.34/app_db/save.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refactor_form);
        getSupportActionBar().setTitle("Signup Form");


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
        //Buttons
        btnRegister = findViewById(R.id.btnRegister);
    };

    private boolean isValidInput(String input) {
        // Utiliza una expresión regular para verificar si el input contiene solo letras y números
        return input.matches("^[a-zA-Z0-9]+$");
    }
    private boolean isAlpha(String input) {
        return input.matches("^[a-zA-Z]+$");
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnRegister) {
            String name = etName.getText().toString().trim();
            String user = etUser.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "El campo del nombre no puede estar vacío.", Toast.LENGTH_SHORT).show();
            } else if (!isAlpha(name)) {
                Toast.makeText(this, "Nombre no válido. Solo se permiten letras.", Toast.LENGTH_SHORT).show();
            } else if (user.isEmpty()) {
                Toast.makeText(this, "El campo de usuario no puede estar vacío.", Toast.LENGTH_SHORT).show();
            } else if (!isValidInput(user)) {
                Toast.makeText(this, "Usuario no válido. Solo se permiten letras y números.", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(this, "El campo de contraseña no puede estar vacío.", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 5) {
                Toast.makeText(this, "La contraseña debe tener al menos " + 5 + " caracteres.", Toast.LENGTH_SHORT).show();
            } else {
                createUser(name, user, password);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }
    }
    private void createUser(String name, String user, String password) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegisterForm.this, "User created correctly", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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