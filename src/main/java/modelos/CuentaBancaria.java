package modelos;

public abstract class CuentaBancaria {
    private final String titular;
    private double saldo;

    public CuentaBancaria(String titular) {
        this.titular = titular;
        this.saldo = 0;
    }

    /**
     * Precondición: monto > 0.
     * Postcondición: el saldo se incrementó en monto.
     *
     * @throws DepositoNoPermitidoException si el tipo de cuenta no admite el depósito.
     */
    public abstract void depositar(double monto) throws DepositoNoPermitidoException;

    /**
     * Precondición: monto > 0.
     * Postcondición: el saldo se decrementó en monto.
     *
     * @throws SaldoInsuficienteException si el tipo de cuenta no permite retirar ese monto.
     */
    public abstract void retirar(double monto) throws SaldoInsuficienteException;

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    protected void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " de " + titular + " - saldo: $" + saldo;
    }
}
