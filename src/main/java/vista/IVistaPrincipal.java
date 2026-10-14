package vista;

import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.event.ListSelectionListener;

public interface IVistaPrincipal {

    /** Action commands de los botones, para que el controlador sepa cuál se presionó. */
    String CREAR = "CREAR";
    String DEPOSITAR = "DEPOSITAR";
    String RETIRAR = "RETIRAR";

    // --- Sección crear cuenta ---

    /** Carga las opciones del selector de tipo de cuenta. */
    void setTiposCuenta(List<String> tipos);

    String getTipoCuenta();

    String getTitular();

    /** Devuelve el texto ingresado tal cual; el controlador se encarga de validarlo. */
    String getTope();

    /** Vacía los campos de titular y tope. */
    void limpiarFormularioCuenta();

    // --- Sección transacciones ---

    /** Reemplaza la lista de cuentas mostradas, manteniendo la selección si es posible. */
    void setCuentas(List<String> cuentas);

    /** Índice de la cuenta seleccionada en la lista, o -1 si no hay ninguna. */
    int getCuentaSeleccionada();

    void setCuentaSeleccionada(int indice);

    void setSaldo(double saldo);

    /** Devuelve el texto ingresado tal cual; el controlador se encarga de validarlo. */
    String getMonto();

    void limpiarMonto();

    // --- Sección errores ---

    void setError(String mensaje);

    void clearError();

    // --- Eventos y ciclo de vida ---

    /** Registra el listener para los botones Crear, Depositar y Retirar. */
    void setActionListener(ActionListener listener);

    /** Registra el listener que se dispara al cambiar la cuenta seleccionada. */
    void setSeleccionCuentaListener(ListSelectionListener listener);

    void mostrar();
}
