package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;

public class VistaPrincipal extends JFrame implements IVistaPrincipal {

    // Tamaños pensados para que se lea bien en un proyector.
    private static final Font FUENTE = new Font(Font.SANS_SERIF, Font.PLAIN, 26);
    private static final Font FUENTE_TITULO = FUENTE.deriveFont(Font.BOLD);
    private static final Font FUENTE_SALDO = FUENTE.deriveFont(Font.BOLD, 44f);
    private static final int ESPACIO = 15;

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

        aplicarFuente(getContentPane());
        labelSaldo.setFont(FUENTE_SALDO);

        // Sin esto, GridBagLayout colapsa los campos de texto si la ventana queda angosta.
        for (JComponent campo : List.of(campoTitular, campoTope, campoMonto)) {
            campo.setMinimumSize(campo.getPreferredSize());
        }

        setSaldo(0);
        pack();
        actualizarTamanioMinimo();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelNorte() {
        // GridBagLayout en vez de FlowLayout para que los campos nunca pasen a una segunda fila.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(borde("Nueva cuenta"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(ESPACIO / 2, ESPACIO / 2, ESPACIO / 2, ESPACIO / 2);
        panel.add(new JLabel("Tipo:"), c);
        panel.add(comboTipoCuenta, c);
        panel.add(new JLabel("Titular:"), c);
        panel.add(campoTitular, c);
        panel.add(new JLabel("Tope:"), c);
        panel.add(campoTope, c);
        panel.add(botonCrear, c);

        // Relleno que ocupa el espacio sobrante, así los campos quedan alineados a la izquierda.
        c.weightx = 1;
        panel.add(new JPanel(), c);

        return panel;
    }

    private JPanel crearPanelCentro() {
        JPanel panel = new JPanel(new BorderLayout(ESPACIO, ESPACIO));
        panel.setBorder(borde("Transacciones"));

        listaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollCuentas = new JScrollPane(listaCuentas);
        scrollCuentas.setPreferredSize(new Dimension(450, 300));
        scrollCuentas.setBorder(borde("Cuentas"));
        panel.add(scrollCuentas, BorderLayout.WEST);

        JPanel panelOperaciones = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(ESPACIO, ESPACIO, ESPACIO, ESPACIO);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        labelSaldo.setHorizontalAlignment(JLabel.CENTER);
        panelOperaciones.add(labelSaldo, c);

        c.gridy = 1;
        JPanel panelMonto = new JPanel(new FlowLayout(FlowLayout.CENTER, ESPACIO, 0));
        panelMonto.add(new JLabel("Monto:"));
        panelMonto.add(campoMonto);
        panelOperaciones.add(panelMonto, c);

        c.gridy = 2;
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, ESPACIO, 0));
        panelBotones.add(botonDepositar);
        panelBotones.add(botonRetirar);
        panelOperaciones.add(panelBotones, c);

        panel.add(panelOperaciones, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelSur() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, ESPACIO, ESPACIO));
        labelError.setForeground(Color.RED);
        panel.add(labelError);
        return panel;
    }

    /** Evita que la ventana se pueda achicar al punto de cortar componentes. */
    private void actualizarTamanioMinimo() {
        setMinimumSize(null);
        setMinimumSize(getPreferredSize());
    }

    /** Borde con título en la fuente grande y un margen interno. */
    private Border borde(String titulo) {
        TitledBorder conTitulo = BorderFactory.createTitledBorder(titulo);
        conTitulo.setTitleFont(FUENTE_TITULO);
        return BorderFactory.createCompoundBorder(
                conTitulo, BorderFactory.createEmptyBorder(ESPACIO / 2, ESPACIO / 2, ESPACIO / 2, ESPACIO / 2));
    }

    /** Aplica la fuente grande a un componente y a todos los que contiene. */
    private void aplicarFuente(Component componente) {
        componente.setFont(FUENTE);
        if (componente instanceof Container contenedor) {
            for (Component hijo : contenedor.getComponents()) {
                aplicarFuente(hijo);
            }
        }
    }

    // --- Sección crear cuenta ---

    @Override
    public void setTiposCuenta(List<String> tipos) {
        comboTipoCuenta.setModel(new DefaultComboBoxModel<>(tipos.toArray(new String[0])));
        // El tamaño del combo depende de sus opciones, así que se recalcula acá.
        comboTipoCuenta.setMinimumSize(null);
        comboTipoCuenta.setMinimumSize(comboTipoCuenta.getPreferredSize());
        actualizarTamanioMinimo();
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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }
}
