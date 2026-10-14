package modelos;

public class CajaDeAhorro extends CuentaBancaria {
    private final double saldoMaximo;

    public CajaDeAhorro(String titular, double saldoMaximo) {
        super(titular);
        this.saldoMaximo = saldoMaximo;
    }

    /** No se puede superar el saldo máximo. */
    @Override
    public void depositar(double monto) throws DepositoNoPermitidoException {
        assert monto > 0 : "El monto a depositar debe ser positivo";

        if (getSaldo() + monto > saldoMaximo) {
            throw new DepositoNoPermitidoException(
                    "No se pueden depositar $" + monto + " en la cuenta de " + getTitular()
                            + " (saldo máximo: $" + saldoMaximo + ")");
        }

        double saldoAnterior = getSaldo();
        setSaldo(getSaldo() + monto);

        assert getSaldo() == saldoAnterior + monto : "El saldo no se incrementó en el monto";
    }

    /** Solo se puede retirar lo que se tiene. */
    @Override
    public void retirar(double monto) throws SaldoInsuficienteException {
        assert monto > 0 : "El monto a retirar debe ser positivo";

        if (monto > getSaldo()) {
            throw new SaldoInsuficienteException(
                    "No se pueden retirar $" + monto + " de la cuenta de " + getTitular()
                            + " (saldo: $" + getSaldo() + ")");
        }

        double saldoAnterior = getSaldo();
        setSaldo(getSaldo() - monto);

        assert getSaldo() == saldoAnterior - monto : "El saldo no se decrementó en el monto";
    }

    public double getSaldoMaximo() {
        return saldoMaximo;
    }
}
