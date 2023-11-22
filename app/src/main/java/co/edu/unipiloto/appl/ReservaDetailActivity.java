package co.edu.unipiloto.appl;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ReservaDetailActivity extends AppCompatActivity implements View.OnClickListener{

    TextView labTextView, dateReservaTextView;
    Button btnEliminar;

    private static final String URL1 = "http://" + MainActivityUsuario.ip_server +"/app_db/eliminarReservas.php";
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_detail);
        // Recupera los datos de la reserva seleccionada
        Intent intent = getIntent();
        initUI();
        String lab = intent.getStringExtra("lab");
        String dateReserva = intent.getStringExtra("date_reserva");

        // Muestra los datos en la actividad de detalles
        labTextView.setText(lab);
        dateReservaTextView.setText(dateReserva);

        requestQueue = Volley.newRequestQueue(this);
        btnEliminar.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnEliminarReserva){
            mostrarDialogoConfirmacion();
        }
    }
    // Dentro de tu actividad ReservaDetailActivity
    private void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que deseas eliminar esta reserva?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Llamar al método eliminarReserva si el usuario confirma
                eliminarReserva(labTextView.getText().toString().trim(), dateReservaTextView.getText().toString().trim());
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Cerrar el cuadro de diálogo
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initUI(){
        labTextView = findViewById(R.id.lab_detail);
        dateReservaTextView = findViewById(R.id.date_reserva_detail);
        btnEliminar = findViewById(R.id.btnEliminarReserva);
    };

    private void eliminarReserva(String name_lab, String date_reserva) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("Error al eliminar la reserva.")){
                            Intent intent = new Intent(getApplicationContext(), MainActivityUsuario.class);
                            startActivity(intent);
                            Toast.makeText(ReservaDetailActivity.this,"Reserva cancelada exitosamente", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ReservaDetailActivity.this,"Error al cancelar la reserva", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Maneja errores aquí
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name_lab", name_lab);
                params.put("date_reserva", date_reserva);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
