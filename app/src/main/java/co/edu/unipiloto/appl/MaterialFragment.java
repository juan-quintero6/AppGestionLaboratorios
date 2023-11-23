package co.edu.unipiloto.appl;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MaterialFragment extends Fragment {

    private List<String> listaMateriales;
    private MaterialAdapter adapter;
    private String nombreLaboratorio;

    public static MaterialFragment newInstance(String nombreLaboratorio) {
        MaterialFragment fragment = new MaterialFragment();
        Bundle args = new Bundle();
        args.putString("nombreLaboratorio", nombreLaboratorio);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Obtener el nombre del laboratorio del argumento
            nombreLaboratorio = getArguments().getString("nombreLaboratorio");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material, container, false);

        // Inicializar la lista de materiales
        listaMateriales = new ArrayList<>();

        // Configurar el RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMateriales);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Crear y establecer el adaptador
        adapter = new MaterialAdapter(listaMateriales);
        recyclerView.setAdapter(adapter);

        // Cargar datos desde la base de datos utilizando Volley
        cargarDatosDesdeBD(nombreLaboratorio);

        return view;
    }
    private void cargarDatosDesdeBD(String name_lab) {
        // URL del servidor que contiene los datos de la base de datos
        String URL = "http://" + MainActivityUsuario.ip_server +"/app_db/inventories.php";

        // Crear la cola de solicitudes Volley
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        // Realizar una solicitud de tipo String
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta de texto
                        procesarRespuesta(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        Toast.makeText(requireContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    }
                }
                )
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name_lab", name_lab);
                return params;
                }
            };

        // Agregar la solicitud a la cola
        requestQueue.add(stringRequest);
    }
    private void procesarRespuesta(String response) {
        listaMateriales.clear();

        String[] lineas = response.split("\n");

        for (String linea : lineas) {
            listaMateriales.add(linea.trim());
        }

        adapter.notifyDataSetChanged();
    }
}