package co.edu.unipiloto.appl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivityUsuario extends AppCompatActivity {
    Button GenerarReserva;
    Button Disponibilidad;
    Button ConsularMaterial;
    Button ConsultarReserva;
    Button Salir;

    //static String ip_server = "192.168.80.21";
    static String ip_server = "192.168.80.21";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_usuario);

        GenerarReserva = findViewById(R.id.btnGenerarReserva);
        Disponibilidad = findViewById(R.id.btnDisponibilidad);
        ConsultarReserva = findViewById(R.id.btnConsultarReserva);
        ConsularMaterial = findViewById(R.id.btnMaterial);
        Salir = findViewById(R.id.btnSalir);

        GenerarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityUsuario.this, CrearReservaForm.class);
                startActivity(intent);
            }
        });
        ConsultarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityUsuario.this, ReservaCategoryActivity.class);
                startActivity(intent);
            }
        });
        Disponibilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityUsuario.this, DispLabForm.class);
                startActivity(intent);
            }
        });
        ConsularMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityUsuario.this, ConsultarMaterialActivity.class);
                startActivity(intent);
            }
        });
        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityUsuario.this, LoginForm.class);
                startActivity(intent);
            }
        });

    }
}