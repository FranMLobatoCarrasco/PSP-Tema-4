package main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encriptador {

    public byte[] getDigest(byte[] contrasena) throws NoSuchAlgorithmException {
        byte[] resumen;
        MessageDigest encriptador = MessageDigest.getInstance("SHA-256");
        encriptador.reset();
        encriptador.update(contrasena);
        resumen = encriptador.digest();
        return resumen;
    }

    public boolean compararResumenes(String resumen1, String resumen2) {
        return resumen1.equals(resumen2);
    }
}
