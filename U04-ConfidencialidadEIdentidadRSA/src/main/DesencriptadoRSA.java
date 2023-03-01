package main;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;

public class DesencriptadoRSA {

    public static void main(String[] args) {
        try {
            System.out.println("Generando clave privada...");
            PrivateKey clavePrivada = GestorLlaves.obtenerClavePrivada();

            System.out.println("Descifrando mensaje...");
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, clavePrivada);
            byte[] mensajeCifrado = Base64.getDecoder().decode(leerYDevolverMensaje());
            byte[] mensaje = cipher.doFinal(mensajeCifrado);

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
}
