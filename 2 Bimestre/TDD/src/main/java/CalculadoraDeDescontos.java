public class CalculadoraDeDescontos {

    public double calcular(double valorCompra) {
        if (valorCompra < 0) {
            throw new IllegalArgumentException("Valor negativo");
        }
        if (valorCompra < 100) {
            return 0.0;
        } else if (valorCompra <= 500) {
            return valorCompra * 0.05;
        } else {
            return valorCompra * 0.10;
        }
    } }
