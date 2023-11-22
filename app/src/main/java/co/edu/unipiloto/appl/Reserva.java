package co.edu.unipiloto.appl;

public class Reserva {
    private String lab;
    private String date;

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

}
