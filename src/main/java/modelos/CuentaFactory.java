package modelos;

import java.util.List;

public class CuentaFactory {

    /** Valores posibles para el parámetro tipo de crearCuenta. */
    public static final List<String> TIPOS = List.of("AHORRO", "CORRIENTE");

    /**
     * Crea una cuenta según el tipo indicado.
     *
     * @param tipo   uno de los valores de TIPOS (no distingue mayúsculas).
     * @param limite saldo máximo para una caja de ahorro, descubierto permitido para una cuenta corriente.
     * @throws IllegalArgumentException si el tipo no es válido.
     */
    public CuentaBancaria crearCuenta(String tipo, String titular, double limite) {
        switch (tipo.toUpperCase()) {
            case "AHORRO":
                return new CajaDeAhorro(titular, limite);
            case "CORRIENTE":
                return new CuentaCorriente(titular, limite);
            default:
                throw new IllegalArgumentException(
                        "Tipo de cuenta desconocido: " + tipo + ". Valores posibles: " + TIPOS);
        }
    }
}
