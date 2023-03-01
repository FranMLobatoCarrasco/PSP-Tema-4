package main;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;

public class EncriptacionRSA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String mensaje;

        System.out.println("=========================================================");
        System.out.print("Introduzca el mensaje ➜ ");
        mensaje = sc.nextLine();
        System.out.println("=========================================================");

        try {
            System.out.println("Generando clave pública...");
            PublicKey clavePublica = GestorLlaves.obtenerClavePublica();

            System.out.println("Cifrando mensaje...");
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, clavePublica);
            byte[] mensajeBytes = mensaje.getBytes(StandardCharsets.UTF_8);
            byte[] mensajeCifrado = cipher.doFinal(mensajeBytes);

            System.out.println("Escribiendo mensaje cifrado en el fichero...");
            escribirFichero(Base64.getEncoder().encodeToString(mensajeCifrado));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("ERROR. El algoritmo seleccionado no existe. " + e.getMessage());
        } catch (NoSuchPaddingException e) {
            System.err.println("ERROR. No existe el padding seleccionado. " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println("ERROR. La clave introducida no es válida. " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            System.err.println("ERROR. El tamaño del bloque utilizado no es correcto. " + e.getMessage());
        } catch (BadPaddingException e) {
            System.err.println("ERROR. El padding utilizado es erróneo. " + e.getMessage());
        }
    }

    /**
     * Escribe el mensaje cifrado en el fichero mensajeCifrado.cif
     *
     * @param texto Mensaje cifrado a escribir en el fichero
     */
    private static void escribirFichero(String texto) {
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter("mensajeCifrado.cif"));
            bufferedWriter.write(texto);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("ERROR: No se ha podido escribir en el fichero");
        }
    }
}
