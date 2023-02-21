package main;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Login {

    private static int contador = 0;
    private static int lineaUsuario = 0;
    private static int lineaContrasena = 0;

    public static void main(String[] args) {
        boolean correcto = false;
        Scanner sc = new Scanner(System.in);
        String nombre, contrasena;

        System.out.println("=========================================================");
        System.out.print("Introduzca el nombre de usuario ➜ ");
        nombre = sc.nextLine();
        System.out.print("Introduzca su contraseña ➜ ");
        contrasena = sc.nextLine();
        System.out.println("=========================================================");

        if (comprobarUsuario(nombre))
            if (comprobarContrasena(contrasena)) {
                System.out.println("Bienvenido al sistema, " + nombre);
            } else {
                System.out.println("Contraseña incorrecta para el usuario \"" + nombre + "\".");
                lineaContrasena = 0;
            }
        else
            System.out.println("El usuario \"" + nombre + "\", no existe");
    }

    public static boolean comprobarUsuario(String nombre) {
        boolean existe = false;
        String[] nombreArchivo;
        String linea;
        try {
            File archivo = new File("usuarios.usu");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo));
            validarFichero(archivo);
            linea = bufferedReader.readLine();
            while (linea != null && !existe) {
                contador++;
                nombreArchivo = linea.split(" ");
                if (nombreArchivo[0].equals(nombre)) {
                    existe = true;
                    lineaUsuario = contador;
                }
                linea = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR. Fichero no encontrado");
        } catch (IOException e) {
            System.err.println("ERROR. En la entrada/salida");
        }
        return existe;
    }

    private static boolean comprobarContrasena(String contrasena) {
        Encriptador encriptador = new Encriptador();
        String linea, resumenHexadecimal;
        byte[] contrasenaBytes, resumen2;
        String[] contrasenaArchivo;
        boolean iguales = false;
        contrasenaBytes = contrasena.getBytes(StandardCharsets.UTF_8);
        try {
            resumen2 = encriptador.getDigest(contrasenaBytes);
            resumenHexadecimal = String.format("%064x", new BigInteger(1, resumen2));
            File archivo = new File("usuarios.usu");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo));
            validarFichero(archivo);
            linea = bufferedReader.readLine();
            while (linea != null && !iguales) {
                lineaContrasena++;
                contrasenaArchivo = linea.split(" ");
                if (lineaContrasena == lineaUsuario)
                    iguales = encriptador.compararResumenes(contrasenaArchivo[1], resumenHexadecimal);
                linea = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR. Fichero no encontrado");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("ERROR. En el algoritmo de encriptación");
        } catch (IOException e) {
            System.err.println("ERROR. En la entrada/salida");
        }
        return iguales;
    }

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
