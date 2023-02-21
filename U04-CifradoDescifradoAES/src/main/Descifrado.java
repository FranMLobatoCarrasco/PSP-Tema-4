package main;

import java.util.Scanner;

import static main.Utilidad.pedirContrasena;

public class Descifrado {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String constrasena;
        int longitudBloque;

        System.out.println("=========================================================");
        constrasena = pedirContrasena();
        System.out.println("Introduzca la longitud de bloque. ");
        System.out.print("Debe ser 16, 24 o 32 ➜ ");
        longitudBloque = sc.nextInt();
        System.out.println("=========================================================");

        UtilidadCifrado utilidadCifrado = new UtilidadCifrado(constrasena, longitudBloque);

        utilidadCifrado.desencriptar();
    }
}
