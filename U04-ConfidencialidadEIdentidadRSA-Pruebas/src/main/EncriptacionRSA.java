package main;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
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
            PrivateKey clavePrivada = GestorLlaves.obtenerClavePrivadaER("emisor");

            System.out.println("Cifrando mensaje...");
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, clavePrivada);
            byte[] mensajeBytes = mensaje.getBytes(StandardCharsets.UTF_8);
            byte[] mensajeCifrado = cipher.doFinal(mensajeBytes);

            PublicKey clavePublica = GestorLlaves.obtenerClavePublicaER("receptor");

            mensajeCifrado = cifrarContenido(mensajeCifrado, clavePublica);

            System.out.println("Escribiendo mensaje cifrado en el fichero...");
            escribirFichero(Base64.getEncoder().encodeToString(mensajeCifrado));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("ERROR. El algoritmo seleccionado no existe. " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            System.err.println("ERROR. No existe el padding seleccionado. " + e.getMessage());
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.err.println("ERROR. La clave introducida no es válida. " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.err.println("ERROR. El tamaño del bloque utilizado no es correcto. " + e.getMessage());
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.err.println("ERROR. El padding utilizado es erróneo. " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("ERROR. En entrada o salida. " + e.getMessage());
            e.printStackTrace();
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

    public static byte[] cifrarContenido(byte[] contenido, Key clave) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        // Crear objeto Cipher
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        // Inicializar cifrador en modo cifrado con la clave proporcionada
        cipher.init(Cipher.ENCRYPT_MODE, clave);

        // Calcular tamaño del bloque
        int tamanoBloque = (((RSAPublicKey)clave).getModulus().bitLength() + 7) / 8 - 11;

        // Inicializar buffer de salida
        ByteArrayOutputStream bufferSalida = new ByteArrayOutputStream();

        // Cifrar el contenido en bloques
        int offset = 0;
        while (offset < contenido.length) {
            int tamanoBloqueActual = Math.min(tamanoBloque, contenido.length - offset);
            byte[] bloqueCifrado = cipher.doFinal(contenido, offset, tamanoBloqueActual);
            bufferSalida.write(bloqueCifrado);
            offset += tamanoBloqueActual;
        }

        // Devolver contenido cifrado completo
        return bufferSalida.toByteArray();
    }
}
