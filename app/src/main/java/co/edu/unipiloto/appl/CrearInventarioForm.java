package co.edu.unipiloto.appl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CrearInventarioForm extends AppCompatActivity implements View.OnClickListener {

    Spinner sLab;
    TextView materialTextView, amountTextView;
    Button btnGene;

    private static final String URL1 = "http://" + MainActivityUsuario.ip_server +"/app_db/createInventory.php";

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        /* Agregar el fragment al contenedor
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new MaterialFragment())
                    .commit();
        }*/
        requestQueue = Volley.newRequestQueue(this);
        initUI();

        btnGene.setOnClickListener(this);
    }


    private void initUI(){
        //Spinner laboratorios
        sLab = findViewById(R.id.salas_laboratorio);
        materialTextView = findViewById(R.id.editTextMaterial);
        amountTextView = findViewById(R.id.editTextNumber);
        //Button
        btnGene = findViewById(R.id.btnRegisterMaterial);
    };
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnRegisterMaterial) {
            String name_lab = String.valueOf(sLab.getSelectedItem());
            String material_name = materialTextView.getText().toString().trim();
            String amount = amountTextView.getText().toString().trim();

            createMaterial(name_lab, material_name, amount);
        }
    }

    private void createMaterial(String name_lab, String material_name, String amount) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("El material ya se encuentra registrado")) {
                            Toast.makeText(CrearInventarioForm.this, "Ya existe un material con este nombre.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CrearInventarioForm.this, "Material creado correctamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CrearInventarioForm.this, "Material incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name_lab", name_lab);
                params.put("material_name", material_name);
                params.put("amount", amount);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}