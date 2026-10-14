package pruebas;

import vista.IVistaPrincipal;
import vista.VistaPrincipal;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        VistaPrincipal vistaPrincipal = new VistaPrincipal();
        vistaPrincipal.setTiposCuenta(List.of("AHORRO", "CORRIENTE"));
        vistaPrincipal.setVisible(true);
    }
}
