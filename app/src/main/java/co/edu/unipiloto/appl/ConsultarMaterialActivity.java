package co.edu.unipiloto.appl;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class ConsultarMaterialActivity extends AppCompatActivity {

    Spinner sLab;
    Button btnActualizarFragmento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_material);

        sLab = findViewById(R.id.salas_laboratorio);
        btnActualizarFragmento = findViewById(R.id.btnConsultarMaterial);

        btnActualizarFragmento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el nombre del laboratorio
                String name_lab = String.valueOf(sLab.getSelectedItem());

                // Crear una instancia del fragmento con el nombre del laboratorio como parámetro
                MaterialFragment fragment = MaterialFragment.newInstance(name_lab);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit();
            }
        });


    }
}