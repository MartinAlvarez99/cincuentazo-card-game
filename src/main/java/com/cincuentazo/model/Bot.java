package com.cincuentazo.model;

import java.util.List;

public class Bot {

    private String nombre;
    private List<Carta> mano;

    public Bot(String nombre) {
        this.nombre = nombre;
    }

    public void setMano(List<Carta> mano) {
        this.mano = mano;
    }

    public List<Carta> getMano() {
        return mano;
    }

    public String getNombre() {
        return nombre;
    }

    public void agregarCarta(Carta c) {
        mano.add(c);
    }

    // Lógica para elegir qué carta tirar
    public Carta elegirCarta(int sumaMesa) {

        for (Carta c : mano) {
            int valor = c.valorJuego(sumaMesa, c.getValor().equals("A") ? 1 : 0);

            if (sumaMesa + valor <= 50) {
                mano.remove(c);  // 🔥 EL BOT YA NO TIENE ESTA CARTA
                return c;
            }
        }

        return null; // no puede jugar
    }

}

