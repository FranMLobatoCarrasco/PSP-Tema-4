package main;

import java.util.Scanner;

public class Cifrado {
    public static void main(String[] args) {
        // Una clase que contenga un método main que se encargue de generar la clave, cifrar un texto y almacenarlo en un fichero.
        // Para ello hará uso de los métodos definidos en la clase anterior. El texto a cifrar se pedirá por teclado al usuario.

        Scanner sc = new Scanner(System.in);
        String constrasena, mensaje;

        System.out.println("=========================================================");
        System.out.print("Introduzca la contraseña que va a usar ➜ ");
        constrasena = sc.nextLine();
        System.out.print("Introduzca el mensaje ➜ ");
        mensaje = sc.nextLine();
        System.out.println("=========================================================");

        Utilidades utilidades = new Utilidades(constrasena);

        utilidades.encriptar(mensaje);
    }

    //TODO: crear metodo que compruebe la contraseña si es de 16 caracteres
}
