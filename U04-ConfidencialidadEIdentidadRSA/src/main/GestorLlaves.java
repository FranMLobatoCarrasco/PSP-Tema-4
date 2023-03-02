package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class GestorLlaves {

    private static final String CLAVE_PUBLICA_EMISOR = "src/keys/emisor/clavePublica.key";
    private static final String CLAVE_PRIVADA_EMISOR = "src/keys/emisor/clavePrivada.key";
    private static final String CLAVE_PUBLICA_RECEPTOR = "src/keys/receptor/clavePublica.key";
    private static final String CLAVE_PRIVADA_RECEPTOR = "src/keys/receptor/clavePrivada.key";

    /**
     * Constructor de la clase.
     */
    public GestorLlaves() {
        comprobarYborrarFicheros();
        KeyPair clavesEmisor = generadorClaves();
        KeyPair clavesReceptor = generadorClaves();
        guardarClaves(clavesEmisor, clavesReceptor);
    }

    /**
     * Obtiene la clave privada del fichero.
     *
     * @return La clave privada.
     */
    public void comprobarYborrarFicheros() {
        File file = new File(CLAVE_PUBLICA_EMISOR);
        if (file.exists()) {
            file.delete();
        }
        file = new File(CLAVE_PRIVADA_EMISOR);
        if (file.exists()) {
            file.delete();
        }
        file = new File(CLAVE_PUBLICA_RECEPTOR);
        if (file.exists()) {
            file.delete();
        }
        file = new File(CLAVE_PRIVADA_RECEPTOR);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Guarda las claves en los ficheros.
     *
     * @param clavesEmisor   Claves del emisor.
     * @param clavesReceptor Claves del receptor.
     */
    public void guardarClaves(KeyPair clavesEmisor, KeyPair clavesReceptor) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(CLAVE_PUBLICA_EMISOR);
            fileOutputStream.write(clavesEmisor.getPublic().getEncoded());
            fileOutputStream.close();
            fileOutputStream = new FileOutputStream(CLAVE_PRIVADA_EMISOR);
            fileOutputStream.write(clavesEmisor.getPrivate().getEncoded());
            fileOutputStream.close();
            fileOutputStream = new FileOutputStream(CLAVE_PUBLICA_RECEPTOR);
            fileOutputStream.write(clavesReceptor.getPublic().getEncoded());
            fileOutputStream.close();
            fileOutputStream = new FileOutputStream(CLAVE_PRIVADA_RECEPTOR);
            fileOutputStream.write(clavesReceptor.getPrivate().getEncoded());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR. No se ha encontrado el fichero. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("ERROR. No se ha podido escribir en el fichero. " + e.getMessage());
        }
    }

    /**
     * Genera las claves pública y privada y la devuelve.
     *
     * @return Clave pública
     */
    public KeyPair generadorClaves() {
        try {
            KeyPair claves;
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            claves = keyPairGenerator.generateKeyPair();
            return claves;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No existe ese algoritmo");
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene la clave pública del fichero.
     *
     * @return La clave pública.
     */
    public PublicKey obtenerClavePublica(String clave) {
        File ficheroClavePublica = new File(clave);
        PublicKey clavePublica = null;
        try {
            byte[] bytesClavePublica = Files.readAllBytes(ficheroClavePublica.toPath());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytesClavePublica);
            clavePublica = keyFactory.generatePublic(publicKeySpec);
        } catch (IOException e) {
            System.err.println("ERROR. Se ha producido en la lectura del fichero. " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("ERROR. No existe el algoritmo especificado. " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.err.println("ERROR. La clave indicada no es válida. " + e.getMessage());
        }
        return clavePublica;
    }

    /**
     * Obtiene la clave privada del fichero.
     *
     * @return La clave privada.
     */
    public PrivateKey obtenerClavePrivada(String clave) {
        File ficheroClavePrivada = new File(clave);
        PrivateKey clavePrivada = null;
        try {
            byte[] bytesClavePrivada = Files.readAllBytes(ficheroClavePrivada.toPath());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytesClavePrivada);
            clavePrivada = keyFactory.generatePrivate(privateKeySpec);
        } catch (IOException e) {
            System.err.println("ERROR. Se ha producido en la lectura del fichero. " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("ERROR. No existe el algoritmo especificado. " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.err.println("ERROR. La clave indicada no es válida. " + e.getMessage());
        }
        return clavePrivada;
    }
}
