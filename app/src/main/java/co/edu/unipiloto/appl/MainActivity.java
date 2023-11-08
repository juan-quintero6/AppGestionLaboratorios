package co.edu.unipiloto.appl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button GenerarReserva;
    Button Disponibilidad;
    Button CancelarReserva;
    Button ConsultarReserva;
    Button salir;

    static String ip_server = "192.168.80.10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GenerarReserva = findViewById(R.id.btnGenerarReserva);
        Disponibilidad = findViewById(R.id.btnDisponibilidad);
        ConsultarReserva = findViewById(R.id.btnConsultarReserva);
        salir = findViewById(R.id.btnSalir);

        GenerarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CrearReservaForm.class);
                startActivity(intent);
            }
        });

        Disponibilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DispLabForm.class);
                startActivity(intent);
            }
        });

        ConsultarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReservaCategoryActivity.class);
                startActivity(intent);
            }
        });

    }
}