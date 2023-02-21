package main;

import java.util.Scanner;

public class Utilidad {
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Pide una contraseña al usuario y comprueba que tenga 16 caracteres.
     * @return La contraseña introducida por el usuario.
     */
    public static String pedirContrasena() {
        String contrasena;
        do {
            System.out.print("Introduzca la contraseña que va a usar ➜ ");
            contrasena = sc.next();
            if (contrasena.length() != 16) {
                System.out.println("⚠️ La contraseña debe tener 16 caracteres. ⚠️");
            }
        } while (contrasena.length() != 16);
        return contrasena;
    }
}
