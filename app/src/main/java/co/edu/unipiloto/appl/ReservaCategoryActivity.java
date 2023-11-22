package co.edu.unipiloto.appl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ReservaCategoryActivity extends AppCompatActivity {
    private ReservaActivity reservaAdapter;
    RequestQueue requestQueue;
    private static final String URL1 = "http://" + MainActivityUsuario.ip_server +"/app_db/reservas.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_category);
        requestQueue = Volley.newRequestQueue(this);

        ListView listaReservas = findViewById(R.id.lista_reservas);
        reservaAdapter = new ReservaActivity(this, new ArrayList<>());
        listaReservas.setAdapter(reservaAdapter);

        listaReservas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reserva selectedReserva = (Reserva) parent.getItemAtPosition(position);

                Intent intent = new Intent(ReservaCategoryActivity.this, ReservaDetailActivity.class);

                intent.putExtra("lab", selectedReserva.getLab());
                intent.putExtra("date_reserva",selectedReserva.getDate());

                startActivity(intent);
            }
        });

        obtenerReservas(LoginForm.userId);
    }

    private void obtenerReservas(int id_usuario) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta en formato de cadena y llenar la lista
                        procesarRespuesta(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores
                        Toast.makeText(getApplicationContext(), "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(id_usuario));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void procesarRespuesta(String response) {
        String[] reservaItems = response.split("&");
        List<Reserva> reservas = new ArrayList<>();

        for (int i = 0; i < reservaItems.length; i += 2) {
            String lab = "";
            String dateReserva = "";
            if (i < reservaItems.length) {
                lab = reservaItems[i].replace("lab=", "");
            }
            if (i + 1 < reservaItems.length) {
                dateReserva = reservaItems[i + 1].replace("date_reserva=", "");
            }

            Reserva reserva = new Reserva(lab, dateReserva);
            reservas.add(reserva);
        }
        reservaAdapter.clear();
        reservaAdapter.addAll(reservas);
    }
}
