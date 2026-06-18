package com.ejemplo.pedidos;

import java.util.ArrayList;
import java.util.List;

public class Practica {
    public static void main(String[] args) {
        Producto p1 = new Producto(103L, "Placa Universal Audio", 900, 19 );


        Producto p2 = new Producto(104L, "Consola DDJ400 Pioneer", 600, 1);


        Producto p3 = new Producto(105L, "Auriculares Audio Technica 30X", 150, 5);

        List<Producto> productos = new ArrayList<>();

        productos.add(p1);
        productos.add(p2);
        productos.add(p3);

        int stockTotal = 0;

        for (Producto p : productos){
            System.out.println(p);
            stockTotal += p.getStock();


        }
        System.out.println("Stock Total= " + stockTotal);

        int resultado = productos.stream()
                .filter(p -> p.getPrecio() > 500)
                .mapToInt(Producto::getStock)
                .sum();
        System.out.println("Stock de productos caros: " + resultado);


    }
}
