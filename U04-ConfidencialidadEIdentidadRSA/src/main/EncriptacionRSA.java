package main;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Objects;

public class EncriptacionRSA {

    private static final String ALGORITMO = "RSA/ECB/PKCS1Padding";
    private static final String FICHERO = "src/ficheroCifrado.cif";

    /**
     * Método que cifra un fichero con la clave pública del receptor y la clave privada del emisor.
     *
     * @param fichero              Fichero a cifrar.
     * @param clavePrivadaEmisor   Clave privada del emisor.
     * @param clavePublicaReceptor Clave pública del receptor.
     */
    public static void cifrarFichero(File fichero, PrivateKey clavePrivadaEmisor, PublicKey clavePublicaReceptor) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fichero));
            FileWriter fileWriter = new FileWriter(FICHERO);
            Cipher cipherEmisor = Cipher.getInstance(ALGORITMO); // Creamos el cifrador del emisor
            Cipher cipherReceptor = Cipher.getInstance(ALGORITMO); // Creamos el cifrador del receptor
            cipherEmisor.init(Cipher.ENCRYPT_MODE, clavePrivadaEmisor); // Inicializamos el cifrado del emisor
            cipherReceptor.init(Cipher.ENCRYPT_MODE, clavePublicaReceptor); // Inicializamos el cifrado del receptor
            StringBuilder textoFichero = new StringBuilder();
            String linea = bufferedReader.readLine();
            while (!Objects.equals(linea, "") && linea != null) { // Leemos el fichero y lo guardamos en un StringBuilder
                textoFichero.append(linea).append("\n");
                linea = bufferedReader.readLine();
            }
            byte[] textoACifrar = textoFichero.toString().getBytes();
            byte[] mensajeCifrado1 = cipherEmisor.doFinal(textoACifrar); // Ciframos el mensaje con la clave privada del emisor
            int blockSize = (((RSAPublicKey) clavePublicaReceptor).getModulus().bitLength() + 7) / 8 - 11; // Tamaño del bloque
            ByteArrayOutputStream bufferSalida = new ByteArrayOutputStream();
            int offset = 0;
            while (offset < mensajeCifrado1.length) { // Ciframos el mensaje con el tamaño del bloque
                int tamanoBloqueActual = Math.min(blockSize, mensajeCifrado1.length - offset);
                byte[] mensajeCifrado2 = cipherReceptor.doFinal(mensajeCifrado1, offset, tamanoBloqueActual); // Ciframos el mensaje con la clave pública del receptor
                bufferSalida.write(mensajeCifrado2);
                offset += tamanoBloqueActual;
            }
            byte[] cifrado = bufferSalida.toByteArray();
            String cifradoString = Base64.getEncoder().encodeToString(cifrado);
            fileWriter.write(cifradoString);

            bufferSalida.close();
            bufferedReader.close();
            fileWriter.close();
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
}
