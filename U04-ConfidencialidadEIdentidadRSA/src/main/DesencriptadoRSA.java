package main;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;

public class DesencriptadoRSA {

    private static final String ALGORITMO = "RSA/ECB/PKCS1Padding";
    private static final String FICHERO = "src/ficheroCifrado.cif";

    /**
     * Método que descifra un fichero con la clave pública del emisor y la clave privada del receptor.
     *
     * @param publicaEmisor   Clave pública del emisor.
     * @param privadaReceptor Clave privada del receptor.
     */
    public static void descifrarFichero(PublicKey publicaEmisor, PrivateKey privadaReceptor) {
        try {
            String textoCifrado = leerFichero(FICHERO); // Leemos el fichero cifrado y lo guardamos en una variable
            byte[] bytesCifrado = Base64.getDecoder().decode(textoCifrado);
            Cipher cipherEmisor = Cipher.getInstance(ALGORITMO); // Creamos el cifrador del emisor
            Cipher cipherReceptor = Cipher.getInstance(ALGORITMO); // Creamos el cifrador del receptor
            cipherEmisor.init(Cipher.DECRYPT_MODE, publicaEmisor); // Inicializamos el descifrado del emisor
            cipherReceptor.init(Cipher.DECRYPT_MODE, privadaReceptor); // Inicializamos el descifrado del receptor
            int blockSize = (((RSAPublicKey) publicaEmisor).getModulus().bitLength() + 7) / 8; // Calculamos el tamaño del bloque
            ByteArrayOutputStream bufferSalida = new ByteArrayOutputStream();
            int offset = 0;
            descifrar(cipherReceptor, blockSize, bufferSalida, offset, bytesCifrado); // Desciframos el fichero con la clave privada del receptor
            byte[] archivoDescrifrado = bufferSalida.toByteArray();
            bufferSalida = new ByteArrayOutputStream();
            descifrar(cipherEmisor, blockSize, bufferSalida, offset, archivoDescrifrado); // Desciframos el fichero con la clave pública del emisor
            mostrarCadena(bufferSalida.toString());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("ERROR. No existe el algoritmo seleccionado. " + e.getMessage());
        } catch (NoSuchPaddingException e) {
            System.err.println("ERROR. No existe el padding seleccionado. " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println("ERROR. La clave privada no es válida. " + e.getMessage());
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
     * Método que lee un fichero y lo devuelve en forma de cadena.
     *
     * @param nombreDelFichero Nombre del fichero a leer.
     * @return Cadena con el contenido del fichero.
     * @throws IOException Excepción de entrada o salida.
     */
    public static String leerFichero(String nombreDelFichero) throws IOException {
        String cadena;
        cadena = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(nombreDelFichero)));
        return cadena;
    }

    /**
     * Método que descifra un bloque de bytes.
     *
     * @param cipherEmisor       Cipher del emisor.
     * @param blockSize          Tamaño del bloque.
     * @param bufferSalida       Buffer de salida.
     * @param offset             Offset.
     * @param archivoDescrifrado Archivo descifrado.
     * @throws IllegalBlockSizeException Excepción de tamaño de bloque ilegal.
     * @throws BadPaddingException       Excepción de padding erróneo.
     * @throws IOException               Excepción de entrada o salida.
     */
    private static void descifrar(Cipher cipherEmisor, int blockSize, ByteArrayOutputStream bufferSalida, int offset, byte[] archivoDescrifrado) throws IllegalBlockSizeException, BadPaddingException, IOException {
        while (offset < archivoDescrifrado.length) {
            int tamanoBloqueActual = Math.min(blockSize, archivoDescrifrado.length - offset);
            byte[] bloqueCifrado = Arrays.copyOfRange(archivoDescrifrado, offset, offset + tamanoBloqueActual);
            byte[] mensajeDescrifrado = cipherEmisor.doFinal(bloqueCifrado);
            bufferSalida.write(mensajeDescrifrado);
            offset += tamanoBloqueActual;
        }
    }

    public static void mostrarCadena(String cadena) {
        System.out.println("=================================");
        System.out.println("=            Mensaje            =");
        System.out.println("=================================");
        System.out.println(cadena);
        System.out.println("=================================");
    }
}
