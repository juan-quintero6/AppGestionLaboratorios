package co.edu.unipiloto.appl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReservaActivity extends ArrayAdapter <Reserva> {
    public ReservaActivity(Context context, List<Reserva> reservas) {
        super(context, 0, reservas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reserva reserva = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_reserva, parent, false);
        }

        TextView labTextView = convertView.findViewById(R.id.lab);
        TextView dateReservaTextView = convertView.findViewById(R.id.date);

        labTextView.setText(reserva.getLab());
        dateReservaTextView.setText(reserva.getDate());

        return convertView;

    }
}
