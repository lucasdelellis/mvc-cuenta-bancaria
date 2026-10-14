package modelos;

public class CuentaCorriente extends CuentaBancaria {
    private double descubiertoPermitido;

    public CuentaCorriente(String titular, double descubiertoPermitido) {
        super(titular);
        this.descubiertoPermitido = descubiertoPermitido;
    }

    /** No tiene tope de saldo: nunca rechaza un depósito, por eso no declara la excepción. */
    @Override
    public void depositar(double monto) {
        assert monto > 0 : "El monto a depositar debe ser positivo";

        double saldoAnterior = getSaldo();
        setSaldo(getSaldo() + monto);

        assert getSaldo() == saldoAnterior + monto : "El saldo no se incrementó en el monto";
    }

    /** Se puede quedar en negativo hasta el descubierto permitido. */
    @Override
    public void retirar(double monto) throws SaldoInsuficienteException {
        assert monto > 0 : "El monto a retirar debe ser positivo";

        if (monto > getSaldo() + descubiertoPermitido) {
            throw new SaldoInsuficienteException(
                    "No se pueden retirar $" + monto + " de la cuenta de " + getTitular()
                            + " (saldo: $" + getSaldo() + ", descubierto permitido: $"
                            + descubiertoPermitido + ")");
        }

        double saldoAnterior = getSaldo();
        setSaldo(getSaldo() - monto);

        assert getSaldo() == saldoAnterior - monto : "El saldo no se decrementó en el monto";
    }

    public double getDescubiertoPermitido() {
        return descubiertoPermitido;
    }

    public void setDescubiertoPermitido(double descubiertoPermitido) {
        this.descubiertoPermitido = descubiertoPermitido;
    }
}
