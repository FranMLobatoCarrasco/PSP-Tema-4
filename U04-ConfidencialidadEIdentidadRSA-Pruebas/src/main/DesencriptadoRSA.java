package main;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Scanner;

public class DesencriptadoRSA {

    public static void main(String[] args) {
        try {
            System.out.println("Generando clave privada...");
            PrivateKey clavePrivada = GestorLlaves.obtenerClavePrivadaER("receptor");

            System.out.println("Descifrando mensaje...");
            Cipher cipher = Cipher.getInstance("RSA");

            byte[] mensajeCifrado = Base64.getDecoder().decode(leerYDevolverMensaje());

            byte[] mensaje = cifrarContenido(mensajeCifrado, clavePrivada);

            PublicKey clavePublica = GestorLlaves.obtenerClavePublicaER("emisor");

            cipher.init(Cipher.DECRYPT_MODE, clavePublica);
            mensaje = cipher.doFinal(mensaje);

            System.out.println("=========================================================");
            System.out.println("Mensaje descifrado: " + new String(mensaje));
            System.out.println("=========================================================");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("ERROR. No existe el algoritmo seleccionado. " + e.getMessage());
        } catch (NoSuchPaddingException e) {
            System.err.println("ERROR. No existe el padding seleccionado. " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println("ERROR. La clave privada no es válida. " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            System.err.println("ERROR. El tamaño del bloque utilizado no es correcto. " + e.getMessage());
        } catch (BadPaddingException e) {
            System.err.println("ERROR. El padding utilizado es erróneo. " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("ERROR. En entrada o salida. " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lee el fichero mensajeCifrado.cif y devuelve el mensaje cifrado
     *
     * @return Mensaje cifrado leído del fichero
     */
    private static String leerYDevolverMensaje() {
        String linea = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("mensajeCifrado.cif"));
            linea = bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: No se ha encontrado el fichero");
        } catch (IOException e) {
            System.err.println("ERROR: No se ha podido escribir en el fichero");
        }
        return linea;
    }
    public static byte[] cifrarContenido(byte[] contenido, Key clave) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        // Crear objeto Cipher
        Cipher cipher = Cipher.getInstance("RSA");

        // Inicializar cifrador en modo cifrado con la clave proporcionada
        cipher.init(Cipher.DECRYPT_MODE, clave);

        // Calcular tamaño del bloque
        int tamanoBloque = (((RSAPrivateKey)clave).getModulus().bitLength() + 7) / 8 - 11;

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
