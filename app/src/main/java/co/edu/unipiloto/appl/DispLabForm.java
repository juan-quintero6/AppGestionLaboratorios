package co.edu.unipiloto.appl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DispLabForm extends AppCompatActivity implements View.OnClickListener {

    TextView dateTimeTextView;
    Calendar calendar;
    Button btnDisp;

    private static final String URL1 = "http://" +MainActivity.ip_server +"/app_db/disponibilidad.php";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_lab_form);

        requestQueue = Volley.newRequestQueue(this);
        initUI();

        btnDisp.setOnClickListener(this);
    }
    private void initUI(){
        //Componentes de fecha y hora
        dateTimeTextView = findViewById(R.id.dateTimeTextView);
        calendar = Calendar.getInstance();
        //Button
        btnDisp = findViewById(R.id.btnDisp);
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
        String fecha_hora_reserva = dateTimeTextView.getText().toString().trim();

        if (view.getId() == R.id.btnDisp) {
            consulteDisp(fecha_hora_reserva);
        }
    }

    private void consulteDisp(String fecha_hora_reserva) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if("No disponible".equals(response)){
                            Toast.makeText(DispLabForm.this, "No se encuentra disponible", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DispLabForm.this,"Se encuentra disponible", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fecha_hora_reserva", fecha_hora_reserva);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}