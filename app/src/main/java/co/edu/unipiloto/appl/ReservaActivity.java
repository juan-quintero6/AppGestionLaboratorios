package co.edu.unipiloto.appl;

import static co.edu.unipiloto.appl.R.layout.activity_reserva;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ReservaActivity extends ArrayAdapter <Reserva> {
    private Context context;
    public ReservaActivity(Context context, List<Reserva> reservas) {
        super(context, 0, reservas);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reserva reserva = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(activity_reserva, parent, false);
        }

        TextView labTextView = convertView.findViewById(R.id.lab);
        TextView dateReservaTextView = convertView.findViewById(R.id.date);

        labTextView.setText(reserva.getLab());
        dateReservaTextView.setText(reserva.getDate());

        return convertView;
    }

}
