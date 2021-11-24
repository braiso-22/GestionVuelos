package com.example.gestionvuelos.vo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class Vuelo {
    FlightType tipo;
    String from,to;
    LocalDate depart,returno;
    int passengers;
    Stops paradas;

    public Vuelo(){

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Vuelo(FlightType tipo, String from, String to, String depart, String returno, String passengers, Stops paradas) {
        this.tipo = tipo;
        this.from = from;
        this.to = to;
        this.depart = LocalDate.parse(depart);
        this.returno = LocalDate.parse(returno);
        this.passengers = Integer.parseInt(passengers);
        this.paradas = paradas;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Vuelo(FlightType tipo, String from, String to, String depart, String passengers, Stops paradas) {
        this.tipo = tipo;
        this.from = from;
        this.to = to;
        this.depart = LocalDate.parse(depart);
        this.returno=null;
        this.passengers = Integer.parseInt(passengers);
        this.paradas = paradas;
    }

    public FlightType getTipo() {
        return tipo;
    }

    public void setTipo(FlightType tipo) {
        this.tipo = tipo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDate getDepart() {
        return depart;
    }

    public void setDepart(LocalDate depart) {
        this.depart = depart;
    }

    public LocalDate getReturno() {
        return returno;
    }

    public void setReturno(LocalDate returno) {
        this.returno = returno;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public Stops getParadas() {
        return paradas;
    }

    public void setParadas(Stops paradas) {
        this.paradas = paradas;
    }
}

