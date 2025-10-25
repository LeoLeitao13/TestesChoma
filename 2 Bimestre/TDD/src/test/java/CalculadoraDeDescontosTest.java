import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraDeDescontosTest {

    @Test
    void SemDesconto() {
        CalculadoraDeDescontos calc = new CalculadoraDeDescontos();
        double resultado = calc.calcular(80.0);
        assertEquals(0.0, resultado);
    }

    @Test
    void CincodeDesconto() {
        CalculadoraDeDescontos calc = new CalculadoraDeDescontos();
        double resultado = calc.calcular(200.0);
        assertEquals(10.0, resultado);
    }

    @Test
    void DezdesDesconto() {
        CalculadoraDeDescontos calc = new CalculadoraDeDescontos();
        double resultado = calc.calcular(1000.0);
        assertEquals(100.0, resultado);
    }

    @Test
    void ValorNegativo() {
        CalculadoraDeDescontos calc = new CalculadoraDeDescontos();
        assertThrows(IllegalArgumentException.class, () -> calc.calcular(-50.0));
    }
}
