package main;

import java.util.Scanner;

public class Descifrado {
    public static void main(String[] args) {
        // Una clase que contenga un método main que genera la clave, lee el contenido de un fichero y lo descifra.
        // El texto descifrado debe mostrarse por consola.

        Scanner sc = new Scanner(System.in);
        String constrasena;

        System.out.println("=========================================================");
        System.out.print("Introduzca la contraseña que va a usar ➜ ");
        constrasena = sc.nextLine();
        System.out.println("=========================================================");


        Utilidades utilidades = new Utilidades(constrasena);

        utilidades.desencriptar();
    }

    //TODO: crear metodo que compruebe la contraseña si es de 16 caracteres
}
