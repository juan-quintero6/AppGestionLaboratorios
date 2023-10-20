package co.edu.unipiloto.appl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GenerateReservationForm extends AppCompatActivity implements View.OnClickListener  {

    Spinner sLab;
    TextView dateTimeTextView;
    Calendar calendar;
    Button btnGene;

    static String ip_server = "192.168.80.23";
    private static final String URL1 = "http://" +ip_server+ "/app_db/generate_reservation.php";
    private static final String URL2 = "http://" + ip_server + "/app_db/get_lab_id.php";

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_form);

        requestQueue = Volley.newRequestQueue(this);
        initUI();

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
        String nombreLaboratorio = String.valueOf(sLab.getSelectedItem());
        String fecha_hora_reserva = dateTimeTextView.getText().toString().trim();

        // Define una expresión regular para el formato "yyyy-MM-dd HH:mm".
        String regexPattern = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";

        if (view.getId() == R.id.btnGene) {
            if (fecha_hora_reserva.matches(regexPattern)) {
                // Obtén la fecha actual.
                Calendar calendarHoy = Calendar.getInstance();
                Date fechaHoy = calendarHoy.getTime();

                // Convierte la fecha de reserva a un objeto Date.
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date fechaReserva = null;
                try {
                    fechaReserva = sdf.parse(fecha_hora_reserva);
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
                        Date selectedTime = timeFormat.parse(fecha_hora_reserva.split(" ")[1]);

                        if (dayOfWeek != Calendar.SUNDAY && selectedTime.after(minTime) && selectedTime.before(maxTime)) {
                            obtenerIdLaboratorio(nombreLaboratorio, fecha_hora_reserva);
                        } else {
                            Toast.makeText(GenerateReservationForm.this, "La hora no es válida o es domingo", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(GenerateReservationForm.this, "La fecha no es válida o es anterior a la fecha de hoy", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(GenerateReservationForm.this, "Formato de fecha y hora no válido. Debe ser yyyy-MM-dd HH:mm", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void crearReserva(int id_usuario, int id_lab, String fecha_hora_reserva) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(GenerateReservationForm.this, "Reserva creada correctamente", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GenerateReservationForm.this, "Reserva incorrecta", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(id_usuario));
                params.put("id_lab", String.valueOf(id_lab));
                params.put("fecha_hora_reserva", fecha_hora_reserva);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void obtenerIdLaboratorio(String nombreLaboratorio, String fecha_hora_reserva) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Analiza la respuesta para obtener el ID del laboratorio.
                        int id_lab = Integer.parseInt(response);

                        // Luego, puedes llamar a tu método crearReserva con el ID del laboratorio obtenido.
                        crearReserva(1, id_lab, fecha_hora_reserva);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GenerateReservationForm.this, "Error al obtener el ID del laboratorio", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name_lab", nombreLaboratorio);
                params.put("fecha_hora_reserva", fecha_hora_reserva);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}