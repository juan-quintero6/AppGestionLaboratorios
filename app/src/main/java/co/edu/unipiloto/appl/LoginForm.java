package co.edu.unipiloto.appl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

public class LoginForm extends AppCompatActivity implements View.OnClickListener{

    EditText etUser, etPassword;
    Button btnLogin;

    private static final String URL1 = "http://" + MainActivityUsuario.ip_server +"/app_db/validate.php";
    static int userId;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        getSupportActionBar().setTitle("Ingreso");

        requestQueue = Volley.newRequestQueue(this);
        initUI();

        btnLogin.setOnClickListener(this);
    }

    public void btn_signup(View view){
        startActivity(new Intent(getApplicationContext(), RegisterForm.class));
    }
    private void initUI(){
        //EditText
        etUser = findViewById(R.id.editTextUserL);
        etPassword = findViewById(R.id.editTextPasswordL);
        //Button
        btnLogin = findViewById(R.id.btnLogin);
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            String user = etUser.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (user.isEmpty()) {
                Toast.makeText(this, "Campos vacios.", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Campos vacios.", Toast.LENGTH_SHORT).show();
            } else
                loginUser(user, password);
            }
            etUser.setText("");
            etPassword.setText("");
    }

    private void loginUser(String user, String password) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.isEmpty() && !response.equals("Credenciales incorrectas")){
                            if(response.equals("Administrador.")){
                                Intent intent = new Intent(getApplicationContext(), MainActivityAdmin.class);
                                startActivity(intent);
                            }else {
                                userId = Integer.parseInt(response);
                                Intent intent = new Intent(getApplicationContext(), MainActivityUsuario.class);
                                startActivity(intent);
                            }
                            Toast.makeText(LoginForm.this,"Usuario ingresó correctamente", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginForm.this,"Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginForm.this, "Error en la solicitud al servidor", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user", user);
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}