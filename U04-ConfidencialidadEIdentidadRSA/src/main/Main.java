package main;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Generando llaves
        System.out.println("Generando llaves...");
        GestorLlaves gestorLlaves = new GestorLlaves();
        // Encriptando fichero
        System.out.println("Encriptando fichero...");
        EncriptacionRSA.cifrarFichero(new File("src/fichero.txt"), gestorLlaves.obtenerClavePrivada("src/keys/emisor/clavePrivada.key"), gestorLlaves.obtenerClavePublica("src/keys/receptor/clavePublica.key"));
        // Desencriptando fichero
        System.out.println("Desencriptando fichero...");
        DesencriptadoRSA.descifrarFichero(gestorLlaves.obtenerClavePublica("src/keys/emisor/clavePublica.key"), gestorLlaves.obtenerClavePrivada("src/keys/receptor/clavePrivada.key"));
    }
}
