package pruebas;

import controlador.ControladorPrincipal;
import modelos.CuentaBancaria;
import modelos.CuentaCorriente;
import vista.VistaPrincipal;

public class Main {
    public static void main(String[] args) {
        VistaPrincipal vistaPrincipal = new VistaPrincipal();
        new ControladorPrincipal(vistaPrincipal);
        vistaPrincipal.mostrar();
    }
}
