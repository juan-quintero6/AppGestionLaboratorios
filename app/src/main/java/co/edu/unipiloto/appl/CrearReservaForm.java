package co.edu.unipiloto.appl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearReservaForm extends AppCompatActivity implements View.OnClickListener  {

    Spinner sLab;
    TextView dateTimeTextView;
    Calendar calendar;
    Button btnGene;

    private static final String URL1 = "http://" + MainActivityUsuario.ip_server +"/app_db/generate_reservation.php";
    private static final String URL = "http://" + MainActivityUsuario.ip_server +"/app_db/";

    RequestQueue requestQueue;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_r_form);

        requestQueue = Volley.newRequestQueue(this);
        initUI();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        btnGene.setOnClickListener(this);
    }
    private void initUI(){
        //Spinner laboratorios
        sLab = findViewById(R.id.salas_laboratorio);
        //Componentes de fecha y hora
        dateTimeTextView = findViewById(R.id.dateTimeTextView);
        calendar = Calendar.getInstance();
        //Button
        btnGene = findViewById(R.id.btnGene);
    };

    public void openDatePicker(View view) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        updateDateTimeTextView();
                    }
                },
                year, month, dayOfMonth
        );

        datePickerDialog.show();
    }
    public void openTimePicker(View view) {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        updateDateTimeTextView();
                    }
                },
                hourOfDay, minute, true
        );

        timePickerDialog.show();
    }
    private void updateDateTimeTextView() {
        String selectedDateAndTime = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", calendar).toString();
        dateTimeTextView.setText(selectedDateAndTime);
    }

    @Override
    public void onClick(View view) {
        int userId = LoginForm.userId;
        String nombreLaboratorio = String.valueOf(sLab.getSelectedItem());
        String date_reserva = dateTimeTextView.getText().toString().trim();

        // Define una expresión regular para el formato "yyyy-MM-dd HH:mm".
        String regexPattern = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";

        if (view.getId() == R.id.btnGene) {
            if (date_reserva.matches(regexPattern)) {
                // Obtén la fecha actual.
                Calendar calendarHoy = Calendar.getInstance();
                Date fechaHoy = calendarHoy.getTime();

                // Convierte la fecha de reserva a un objeto Date.
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date fechaReserva = null;
                try {
                    fechaReserva = sdf.parse(date_reserva);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Verifica si la fecha de reserva es posterior a la fecha de hoy.
                if (fechaReserva != null && fechaReserva.after(fechaHoy)) {
                    // Verifica si la fecha es un domingo.
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fechaReserva);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                    // Verifica si la hora está dentro de un rango permitido (por ejemplo, de 8:00 a 18:00).
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    Date minTime, maxTime;
                    try {
                        minTime = timeFormat.parse("06:00");
                        maxTime = timeFormat.parse("18:00");
                        Date selectedTime = timeFormat.parse(date_reserva.split(" ")[1]);

                        if (dayOfWeek != Calendar.SUNDAY && selectedTime.after(minTime) && selectedTime.before(maxTime)) {
                            crearReservaRest(nombreLaboratorio, LoginForm.userId, date_reserva);
                        } else {
                            Toast.makeText(CrearReservaForm.this, "La hora no es válida o es domingo", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(CrearReservaForm.this, "La fecha no es válida o es anterior a la fecha de hoy", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CrearReservaForm.this, "Formato de fecha y hora no válido. Debe ser yyyy-MM-dd HH:mm", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void crearReserva(String name_lab, int id_usuario, String date_reserva) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("Ya existe la reserva")) {
                            Toast.makeText(CrearReservaForm.this, "Ya existe una reserva para este laboratorio en la misma fecha.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CrearReservaForm.this, "Reserva creada correctamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CrearReservaForm.this, "Reserva incorrecta", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name_lab", name_lab);
                params.put("id_usuario", String.valueOf(id_usuario));
                params.put("date_reserva", date_reserva);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void crearReservaRest(String name_lab, int id_usuario, String date_reserva) {
        Call<JsonObject> call = apiService.crearReserva(name_lab, id_usuario, date_reserva);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        if (jsonObject.has("message")) {
                            String message = jsonObject.get("message").getAsString();
                            if ("Ya existe la reserva".equals(message)) {
                                Toast.makeText(CrearReservaForm.this, "Ya existe una reserva para este laboratorio en la misma fecha.", Toast.LENGTH_SHORT).show();
                            } else if ("La reserva se creó exitosamente.".equals(message)) {
                                Toast.makeText(CrearReservaForm.this, "Reserva creada correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                // Manejar otros casos según sea necesario
                                Toast.makeText(CrearReservaForm.this, "Respuesta no reconocida: " + message, Toast.LENGTH_SHORT).show();
                                Log.d("ReservaForm", "Respuesta no reconocida: " + message);
                            }
                        } else if (jsonObject.has("error")) {
                            // Manejar el caso de error
                            String error = jsonObject.get("error").getAsString();
                            Log.e("ReservaForm", "Error: " + error);
                            Toast.makeText(CrearReservaForm.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Manejar el caso de respuesta no exitosa
                    Log.e("ReservaForm", "Respuesta no exitosa: " + response.code());
                    Toast.makeText(CrearReservaForm.this, "Respuesta no exitosa: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Manejar el caso de error de red
                Log.e("ReservaForm", "Error de red: " + t.getMessage(), t);
                Toast.makeText(CrearReservaForm.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}