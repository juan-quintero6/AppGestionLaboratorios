package co.edu.unipiloto.appl;

public class Reserva {
    private String lab;
    private String date;

    static final Reserva[] reservas = {
            new Reserva("Circuitos y electronica", "20/10/2023 16:00.")};

    public Reserva(String lab, String date) {
        this.lab = lab;
        this.date = date;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {

        return
                "Reserva";
    }
}
