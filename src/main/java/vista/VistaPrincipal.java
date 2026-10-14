package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

public class VistaPrincipal extends JFrame implements IVistaPrincipal {

    // Norte: crear cuenta
    private final JComboBox<String> comboTipoCuenta = new JComboBox<>();
    private final JTextField campoTitular = new JTextField(15);
    private final JTextField campoTope = new JTextField(8);
    private final JButton botonCrear = new JButton("Crear");

    // Centro: transacciones
    private final DefaultListModel<String> modeloCuentas = new DefaultListModel<>();
    private final JList<String> listaCuentas = new JList<>(modeloCuentas);
    private final JLabel labelSaldo = new JLabel();
    private final JTextField campoMonto = new JTextField(10);
    private final JButton botonDepositar = new JButton("Depositar");
    private final JButton botonRetirar = new JButton("Retirar");

    // Sur: errores
    private final JLabel labelError = new JLabel(" ");

    private ListSelectionListener seleccionCuentaListener;

    public VistaPrincipal() {
        super("Cuentas bancarias");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(crearPanelNorte(), BorderLayout.NORTH);
        add(crearPanelCentro(), BorderLayout.CENTER);
        add(crearPanelSur(), BorderLayout.SOUTH);

        botonCrear.setActionCommand(CREAR);
        botonDepositar.setActionCommand(DEPOSITAR);
        botonRetirar.setActionCommand(RETIRAR);

        setSaldo(0);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelNorte() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Nueva cuenta"));

        panel.add(new JLabel("Tipo:"));
        panel.add(comboTipoCuenta);
        panel.add(new JLabel("Titular:"));
        panel.add(campoTitular);
        panel.add(new JLabel("Tope:"));
        panel.add(campoTope);
        panel.add(botonCrear);

        return panel;
    }

    private JPanel crearPanelCentro() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Transacciones"));

        listaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollCuentas = new JScrollPane(listaCuentas);
        scrollCuentas.setPreferredSize(new Dimension(220, 200));
        scrollCuentas.setBorder(BorderFactory.createTitledBorder("Cuentas"));
        panel.add(scrollCuentas, BorderLayout.WEST);

        JPanel panelOperaciones = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panelOperaciones.add(labelSaldo, c);

        c.gridy = 1;
        JPanel panelMonto = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelMonto.add(new JLabel("Monto:"));
        panelMonto.add(campoMonto);
        panelOperaciones.add(panelMonto, c);

        c.gridy = 2;
        c.gridwidth = 1;
        panelOperaciones.add(botonDepositar, c);
        c.gridx = 1;
        panelOperaciones.add(botonRetirar, c);

        panel.add(panelOperaciones, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelSur() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelError.setForeground(Color.RED);
        panel.add(labelError);
        return panel;
    }

    // --- Sección crear cuenta ---

    @Override
    public void setTiposCuenta(List<String> tipos) {
        comboTipoCuenta.setModel(new DefaultComboBoxModel<>(tipos.toArray(new String[0])));
    }

    @Override
    public String getTipoCuenta() {
        return (String) comboTipoCuenta.getSelectedItem();
    }

    @Override
    public String getTitular() {
        return campoTitular.getText().trim();
    }

    @Override
    public String getTope() {
        return campoTope.getText().trim();
    }

    @Override
    public void limpiarFormularioCuenta() {
        campoTitular.setText("");
        campoTope.setText("");
    }

    // --- Sección transacciones ---

    @Override
    public void setCuentas(List<String> cuentas) {
        int seleccionada = listaCuentas.getSelectedIndex();

        // Se quita el listener para que repoblar la lista no dispare eventos de selección.
        if (seleccionCuentaListener != null) {
            listaCuentas.removeListSelectionListener(seleccionCuentaListener);
        }
        modeloCuentas.clear();
        modeloCuentas.addAll(cuentas);
        if (seleccionada >= 0 && seleccionada < cuentas.size()) {
            listaCuentas.setSelectedIndex(seleccionada);
        }
        if (seleccionCuentaListener != null) {
            listaCuentas.addListSelectionListener(seleccionCuentaListener);
        }
    }

    @Override
    public int getCuentaSeleccionada() {
        return listaCuentas.getSelectedIndex();
    }

    @Override
    public void setCuentaSeleccionada(int indice) {
        listaCuentas.setSelectedIndex(indice);
    }

    @Override
    public void setSaldo(double saldo) {
        labelSaldo.setText(String.format("Saldo: $%.2f", saldo));
    }

    @Override
    public String getMonto() {
        return campoMonto.getText().trim();
    }

    @Override
    public void limpiarMonto() {
        campoMonto.setText("");
    }

    // --- Sección errores ---

    @Override
    public void setError(String mensaje) {
        labelError.setText(mensaje);
    }

    @Override
    public void clearError() {
        // Un espacio en lugar de "" para que el panel sur no colapse.
        labelError.setText(" ");
    }

    // --- Eventos y ciclo de vida ---

    @Override
    public void setActionListener(ActionListener listener) {
        botonCrear.addActionListener(listener);
        botonDepositar.addActionListener(listener);
        botonRetirar.addActionListener(listener);
    }

    @Override
    public void setSeleccionCuentaListener(ListSelectionListener listener) {
        if (seleccionCuentaListener != null) {
            listaCuentas.removeListSelectionListener(seleccionCuentaListener);
        }
        seleccionCuentaListener = listener;
        listaCuentas.addListSelectionListener(listener);
    }

    @Override
    public void mostrar() {
        setVisible(true);
    }
}
