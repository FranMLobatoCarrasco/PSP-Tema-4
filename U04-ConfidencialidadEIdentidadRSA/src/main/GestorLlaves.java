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

    private static final String CLAVE_PUBLICA = "clavePublica.key";
    private static final String CLAVE_PRIVADA = "clavePrivada.key";

    public static void main(String[] args) {
        KeyPair claves = generarClaves();
        guardarClaves(claves);
    }

    /**
     * Genera las claves pública y privada y la devuelve.
     *
     * @return Clave pública
     */
    public static KeyPair generarClaves() {
        KeyPairGenerator generador;
        KeyPair claves = null;
        try {
            generador = KeyPairGenerator.getInstance("RSA");
            generador.initialize(2048);
            claves = generador.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("ERROR. No existe el algoritmo especificado. " + e.getMessage());
        }
        return claves;
    }

    /**
     * Guarda las claves en los ficheros correspondientes.
     *
     * @param claves Claves a guardar.
     */
    public static void guardarClaves(KeyPair claves) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(CLAVE_PUBLICA);
            fileOutputStream.write(claves.getPublic().getEncoded());
            fileOutputStream.close();
            fileOutputStream = new FileOutputStream(CLAVE_PRIVADA);
            fileOutputStream.write(claves.getPrivate().getEncoded());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR. No se encuentra el fichero. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("ERROR. Se ha producido un error durante la escritura en el fichero. " + e.getMessage());
        }
    }

    /**
     * Obtiene la clave pública del fichero.
     *
     * @return La clave pública.
     */
    public static PublicKey obtenerClavePublica() {
        File ficheroClavePublica = new File(CLAVE_PUBLICA);
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
    public static PrivateKey obtenerClavePrivada() {
        File ficheroClavePrivada = new File(CLAVE_PRIVADA);
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
