package main;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Utilidades {
    // Una clase que implemente la funcionalidad relativa a la generación de la clave a partir de la contraseña del usuario (debe tener 16 bytes) y los métodos de cifrado y descifrado.
    // Debe contener los siguientes métodos:
    //   - obtenerClave. Genera la clave de cifrado y descifrado a partir de la contraseña del usuario.
    //   - cifrar. Cifra un String.
    //   - descifrar. Descifra un String.

    private final String ALGORITMO = "AES/ECB/PKCS5Padding";
    private final static File ARCHIVO = new File("usuarios.usu");
    static final int LONGITUD_BLOQUE = 16;
    private final Key clave;

    public Utilidades(String contrasena) {
        clave = new SecretKeySpec(contrasena.getBytes(), 0, LONGITUD_BLOQUE, "AES");
        if (!ARCHIVO.exists()) {
            try {
                ARCHIVO.createNewFile();
            } catch (IOException e) {
                System.err.println("ERROR. En la entrada/salida");
            }
        }
    }

    public void encriptar(String textoAEncriptar) {
        // 1 - Crear la clave. Al ser el algoritmo AES tenemos que indicarle la longitud del bloque
        // La longitud puede ser 16, 24 o 32 bytes
        // La longitud de la contraseña tiene que coincidir con la longitud indicada

        try {
            // 2 - Crear un Cipher
            Cipher cipher = Cipher.getInstance(ALGORITMO);

            // 3 - Iniciar el cifrado con la clave
            cipher.init(Cipher.ENCRYPT_MODE, clave);

            // 4 - Llevar a cabo el cifrado
            byte[] cipherText = cipher.doFinal(textoAEncriptar.getBytes());

            // Imprimimos el mensaje cifrado en Base 64:
            escribirFichero(Base64.getEncoder().encodeToString(cipherText));

        } catch (NoSuchAlgorithmException e) {
            System.err.println("No existe el algoritmo especificado");
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            System.err.println("El padding seleccionado no existe");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.err.println("La clave utilizada no es válida");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.err.println("El tamaño del bloque elegido no es correcto");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.err.println("El padding seleccionado no es correcto");
            e.printStackTrace();
        }
    }

    private static void escribirFichero(String texto) {
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(ARCHIVO, true));
            bufferedWriter.write(texto);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("ERROR: No se ha podido escribir en el fichero");
        }
    }

    public void desencriptar() {
        // 1 - Crear la clave. Al ser el algoritmo AES tenemos que indicarle la longitud del bloque
        // La longitud puede ser 16, 24 o 32 bytes
        // La longitud de la contraseña tiene que coincidir con la longitud indicada

        try {
            // 2 - Crear un Cipher
            Cipher cipher = Cipher.getInstance(ALGORITMO);

            // 3 - Iniciar el descifrado con la clave
            cipher.init(Cipher.DECRYPT_MODE, clave);

            // 4 - Llevar a cabo el descifrado
            String textoCifrado = leerYDevolverMensaje();
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(textoCifrado));

            // Imprimimos el mensaje descifrado:
            System.out.println("=========================================================");
            System.out.println(new String(plainText));
            System.out.println("=========================================================");

        } catch (NoSuchAlgorithmException e) {
            System.err.println("No existe el algoritmo especificado");
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            System.err.println("El padding seleccionado no existe");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.err.println("La clave utilizada no es válida");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.err.println("El tamaño del bloque elegido no es correcto");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.err.println("El padding seleccionado no es correcto");
            e.printStackTrace();
        }
    }

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
