package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelos.CuentaBancaria;
import modelos.CuentaFactory;
import modelos.DepositoNoPermitidoException;
import modelos.SaldoInsuficienteException;
import vista.IVistaPrincipal;

public class ControladorPrincipal implements ActionListener, ListSelectionListener {
    private final IVistaPrincipal vista;
    private final CuentaFactory cuentaFactory = new CuentaFactory();
    private final List<CuentaBancaria> cuentas = new ArrayList<>();

    public ControladorPrincipal(IVistaPrincipal vista) {
        this.vista = vista;
        this.vista.setTiposCuenta(CuentaFactory.TIPOS);
        this.vista.setActionListener(this);
        this.vista.setSeleccionCuentaListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        vista.clearError();

        switch (e.getActionCommand()) {
            case IVistaPrincipal.CREAR:
                crearCuenta();
                break;
            case IVistaPrincipal.DEPOSITAR:
                depositar();
                break;
            case IVistaPrincipal.RETIRAR:
                retirar();
                break;
        }
    }

    /** Al cambiar la cuenta seleccionada se muestra su saldo. */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        vista.clearError();
        actualizarSaldo();
    }

    private void crearCuenta() {
        String titular = vista.getTitular();
        if (titular.isEmpty()) {
            vista.setError("Ingresá el titular de la cuenta");
            return;
        }

        double tope;
        try {
            tope = Double.parseDouble(vista.getTope());
        } catch (NumberFormatException ex) {
            vista.setError("El tope tiene que ser un número");
            return;
        }
        if (tope < 0) {
            vista.setError("El tope no puede ser negativo");
            return;
        }

        cuentas.add(cuentaFactory.crearCuenta(vista.getTipoCuenta(), titular, tope));
        actualizarListaCuentas();
        vista.setCuentaSeleccionada(cuentas.size() - 1);
        vista.limpiarFormularioCuenta();
    }

    private void depositar() {
        CuentaBancaria cuenta = getCuentaSeleccionada();
        if (cuenta == null) {
            return;
        }
        Double monto = leerMonto();
        if (monto == null) {
            return;
        }

        try {
            cuenta.depositar(monto);
            actualizarTrasOperacion();
        } catch (DepositoNoPermitidoException ex) {
            vista.setError(ex.getMessage());
        }
    }

    private void retirar() {
        CuentaBancaria cuenta = getCuentaSeleccionada();
        if (cuenta == null) {
            return;
        }
        Double monto = leerMonto();
        if (monto == null) {
            return;
        }

        try {
            cuenta.retirar(monto);
            actualizarTrasOperacion();
        } catch (SaldoInsuficienteException ex) {
            vista.setError(ex.getMessage());
        }
    }

    /** Devuelve la cuenta seleccionada, o null (mostrando el error) si no hay ninguna. */
    private CuentaBancaria getCuentaSeleccionada() {
        int indice = vista.getCuentaSeleccionada();
        if (indice < 0) {
            vista.setError("Seleccioná una cuenta");
            return null;
        }
        return cuentas.get(indice);
    }

    /** Devuelve el monto ingresado, o null (mostrando el error) si no es un número positivo. */
    private Double leerMonto() {
        double monto;
        try {
            monto = Double.parseDouble(vista.getMonto());
        } catch (NumberFormatException ex) {
            vista.setError("El monto tiene que ser un número");
            return null;
        }
        if (monto <= 0) {
            vista.setError("El monto tiene que ser mayor a cero");
            return null;
        }
        return monto;
    }

    private void actualizarTrasOperacion() {
        actualizarListaCuentas();
        actualizarSaldo();
        vista.limpiarMonto();
    }

    private void actualizarListaCuentas() {
        List<String> descripciones = new ArrayList<>();
        for (CuentaBancaria cuenta : cuentas) {
            descripciones.add(cuenta.toString());
        }
        vista.setCuentas(descripciones);
    }

    private void actualizarSaldo() {
        int indice = vista.getCuentaSeleccionada();
        vista.setSaldo(indice < 0 ? 0 : cuentas.get(indice).getSaldo());
    }
}
