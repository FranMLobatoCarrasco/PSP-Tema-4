package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class SingIn {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String nombre, contrasena;
        byte[] contrasenaBytes, resumen;
        Encriptador hash = new Encriptador();

        System.out.println("=========================================================");
        System.out.print("Introduzca el nombre de usuario ➜ ");
        nombre = sc.nextLine();
        System.out.print("Introduzca su contraseña ➜ ");
        contrasena = sc.nextLine();
        System.out.println("=========================================================");

        try {
            contrasenaBytes = contrasena.getBytes(StandardCharsets.UTF_8);
            resumen = hash.getDigest(contrasenaBytes);
            contrasena = String.format("%064x", new BigInteger(1, resumen));
            escribir(nombre, contrasena);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("ERROR. En el algoritmo de encriptación");
        }

    }

    public static void escribir(String nombre, String contrasena) {
        try {
            File archivo = new File("usuarios.usu");
            validarFichero(archivo);
            BufferedWriter bufferedWriter;
            if (archivo.exists()) {
                bufferedWriter = new BufferedWriter(new FileWriter(archivo, true));
                bufferedWriter.write(nombre + " " + contrasena);
            } else {
                bufferedWriter = new BufferedWriter(new FileWriter(archivo));
                bufferedWriter.write(nombre + " " + contrasena);
            }
            bufferedWriter.newLine();
            System.out.println("Te has registrado correctamente como nuevo usuario.");
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("ERROR. En la entrada/salida");
        }
    }

    // metodo que valide si un fichero existe, y si no existe lo cree
    public static void validarFichero(File archivo) {
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                System.err.println("ERROR. En la entrada/salida");
            }
        }
    }
}
