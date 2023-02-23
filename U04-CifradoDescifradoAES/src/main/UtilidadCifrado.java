package main;

import javax.crypto.IllegalBlockSizeException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.security.Key;
import java.util.Base64;
import java.io.*;

public class UtilidadCifrado {
    private final static File ARCHIVO = new File("cifrado.cif");
    private final String ALGORITMO = "AES/ECB/PKCS5Padding";
    private final Key clave;

    public UtilidadCifrado(String contrasena, int longitudBloque) {
        clave = new SecretKeySpec(contrasena.getBytes(), 0, longitudBloque, "AES");
        if (!ARCHIVO.exists()) {
            try {
                ARCHIVO.createNewFile();
            } catch (IOException e) {
                System.err.println("ERROR. En la entrada/salida");
            }
        }
    }

    /**
     * Crea la instancia de Cipher e inicializa el cifrado.
     * Muestra por pantalla el contenido descifrado.
     */
    public void encriptar(String textoAEncriptar) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITMO);

            cipher.init(Cipher.ENCRYPT_MODE, clave);

            byte[] cipherText = cipher.doFinal(textoAEncriptar.getBytes());

            escribirFichero(Base64.getEncoder().encodeToString(cipherText));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No existe el algoritmo especificado");
            System.err.println("Mensaje de error: " + e.getMessage());
        } catch (NoSuchPaddingException e) {
            System.err.println("El padding seleccionado no existe");
            System.err.println("Mensaje de error: " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println("La clave utilizada no es válida");
            System.err.println("Mensaje de error: " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            System.err.println("El tamaño del bloque elegido no es correcto");
            System.err.println("Mensaje de error: " + e.getMessage());
        } catch (BadPaddingException e) {
            System.err.println("El padding seleccionado no es correcto");
            System.err.println("Mensaje de error: " + e.getMessage());
        }
    }

    /**
     * Escribe en un fichero el mensaje pasado por parámetro.
     */
    private static void escribirFichero(String texto) {
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(ARCHIVO));
            bufferedWriter.write(texto);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("ERROR: No se ha podido escribir en el fichero");
        }
    }

    /**
     * Crea la instancia de Cipher e inicializa el descifrado.
     * Muestra por pantalla el contenido descifrado.
     */
    public void desencriptar() {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITMO);

            cipher.init(Cipher.DECRYPT_MODE, clave);

            String textoCifrado = leerYDevolverMensaje();
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(textoCifrado));

            System.out.println(new String(plainText));
            System.out.println("=========================================================");
        } catch (NoSuchPaddingException e) {
            System.err.println("El padding seleccionado no existe");
            System.err.println("Mensaje de error: " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            System.err.println("El tamaño del bloque elegido no es correcto");
            System.err.println("Mensaje de error: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No existe el algoritmo especificado");
            System.err.println("Mensaje de error: " + e.getMessage());
        } catch (BadPaddingException e) {
            System.err.println("El padding seleccionado no es correcto");
            System.err.println("Mensaje de error: " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println("La clave utilizada no es válida");
            System.err.println("Mensaje de error: " + e.getMessage());
        }
    }

    /**
     * Lee el fichero y devuelve una cadena con el contenido.
     */
    private static String leerYDevolverMensaje() {
        String linea = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(ARCHIVO));
            linea = bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: No se ha encontrado el fichero");
        } catch (IOException e) {
            System.err.println("ERROR: No se ha podido escribir en el fichero");
        }
        return linea;
    }
}
