import org.example.checkout.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FullTeste {

    private CouponService couponSvc;
    private ShippingService shipSvc;
    private CheckoutService service;
    private final LocalDate TODAY = LocalDate.of(2025, 10, 19);

    @BeforeEach
    void setUp() {
        couponSvc = new CouponService();
        shipSvc = new ShippingService();
        service = new CheckoutService(couponSvc, shipSvc);
    }

    @Test
    public void CupomVazioOuNulo() {
        var itens = List.of(new Item("ROUPA", 50.00, 1));

        var res1 = service.checkout(itens, CustomerTier.BASIC, false, "SUL", 1.0, null, TODAY, null);
        assertEquals(0.00, res1.discountValue);

        var res2 = service.checkout(itens, CustomerTier.BASIC, false, "SUL", 1.0, "", TODAY, null);
        assertEquals(0.00, res2.discountValue);
        var res3 = service.checkout(itens, CustomerTier.BASIC, false, "SUL", 1.0, "  ", TODAY, null);
        assertEquals(0.00, res3.discountValue);
    }


    @Test
    public void Desc20SucessoComNullExpiry() {
        var itens = List.of(new Item("ROUPA", 100.00, 1));
        var res = service.checkout(
                itens,
                CustomerTier.BASIC,
                false,
                "SUL", 1.0, "DESC20", TODAY, null
        );

        assertEquals(20.00, res.discountValue);
    }

    @Test
    public void Desc20DuplaFalha() {
        var itens = List.of(new Item("ROUPA", 50.00, 1));
        var res = service.checkout(
                itens,
                CustomerTier.BASIC,
                false,
                "SUL", 1.0, "DESC20", TODAY, TODAY.minusDays(1)
        );

        assertEquals(0.00, res.discountValue);
    }

    @Test
    public void IgnoraPrimeiraCompraSubtotalBaixo() {
        var itens = List.of(new Item("ROUPA", 49.99, 1));
        var res = service.checkout(itens, CustomerTier.BASIC, true, "SUL", 1.0, null, TODAY, null);

        assertEquals(0.00, res.discountValue);
    }

    @Test
    public void SubtotalZero() {
        var itens = List.<Item>of();
        var res = service.checkout(itens, CustomerTier.BASIC, false, "NORTE", 1.0, null, TODAY, null);

        assertEquals(0.00, res.subtotal);
        assertEquals(0.00, res.discountValue);
        assertEquals(0.00, res.tax);
        assertEquals(30.00, res.shipping);
        assertEquals(30.00, res.total);
    }


    @Test
    public void TestarItemGetters() {
        var item = new Item("ROUPA", 10.00, 2);

        assertEquals("ROUPA", item.getCategoria());
        assertEquals(10.00, item.getPrecoUnitario());
        assertEquals(2, item.getQuantidade());
        assertEquals(20.00, item.subtotal());
    }

    @Test
    public void ItemInvalidoPreco() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Item("TESTE", -1.0, 1);
        });
    }

    @Test
    public void ItemInvalidoQuantidade() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Item("TESTE", 10.0, 0);
        });
    }

    @Test
    public void DescontoTierBasic() {
        var itens = List.of(new Item("ROUPA", 100.00, 1));
        var res = service.checkout(itens, CustomerTier.BASIC, false, "SUL", 1.0, null, TODAY, null);

        assertEquals(0.00, res.discountValue);
        assertEquals(12.00, res.tax);
    }

    @Test
    public void CupomDesconhecido() {
        var itens = List.of(new Item("ROUPA", 100.00, 1));
        var res = service.checkout(itens, CustomerTier.BASIC, false, "SUL", 1.0, "NAOEXISTE", TODAY, null);

        assertEquals(0.00, res.discountValue);
    }

    @Test
    public void AplicarDesconto() {
        var itens = List.of(new Item("ROUPA", 100.00, 1));
        var res = service.checkout(
                itens,
                CustomerTier.SILVER,
                false,
                "SUL", 1.0, null, TODAY, null
        );

        assertEquals(100.00, res.subtotal);
        assertEquals(5.00, res.discountValue);
        assertEquals(11.40, res.tax);
        assertEquals(20.00, res.shipping);
        assertEquals(126.40, res.total);
    }

    @Test
    public void DescontoPrimeiraCompra() {
        var itens = List.of(new Item("ROUPA", 100.00, 1));
        var res = service.checkout(
                itens,
                CustomerTier.GOLD,
                true,
                "SUL", 1.0, null, TODAY, null
        );

        assertEquals(100.00, res.subtotal);
        assertEquals(15.00, res.discountValue);
    }

    @Test
    public void SomadosDescontos() {
        var itens = List.of(new Item("ROUPA", 100.00, 1));
        var res = service.checkout(
                itens,
                CustomerTier.SILVER,
                true,
                "SUL", 1.0, "DESC10", TODAY, null
        );

        assertEquals(100.00, res.subtotal);
        assertEquals(20.00, res.discountValue);
    }

    @Test
    public void Aplicar30Desconto() {
        var itens = List.of(new Item("ROUPA", 100.00, 1));
        var res = service.checkout(
                itens,
                CustomerTier.GOLD,
                true,
                "SUL", 1.0, "DESC20", TODAY, TODAY.plusDays(1)
        );

        assertEquals(100.00, res.subtotal);
        assertEquals(30.00, res.discountValue);
    }

    @Test
    public void SubtotalInsuficiente() {
        var itens = List.of(new Item("ROUPA", 99.99, 1));
        var res = service.checkout(
                itens,
                CustomerTier.BASIC,
                false,
                "SUL", 1.0, "DESC20", TODAY, TODAY.plusDays(1)
        );

        assertEquals(0.00, res.discountValue);
    }

    @Test
    public void DescontoExpirado() {
        var itens = List.of(new Item("ROUPA", 100.00, 1));
        var res = service.checkout(
                itens,
                CustomerTier.BASIC,
                false,
                "SUL", 1.0, "DESC20", TODAY, TODAY.minusDays(1)
        );

        assertEquals(0.00, res.discountValue);
    }

    @Test
    public void FreteGratis() {
        var itens = List.of(new Item("ROUPA", 10.00, 1));
        var res = service.checkout(
                itens,
                CustomerTier.BASIC,
                false,
                "NORTE",
                5.0,
                "FRETEGRATIS",
                TODAY,
                null
        );

        assertEquals(0.00, res.shipping);
    }

    @Test
    public void IgnorarFrete() {
        var itens = List.of(new Item("ROUPA", 10.00, 1));
        var res = service.checkout(
                itens,
                CustomerTier.BASIC,
                false,
                "NORTE",
                5.1,
                "FRETEGRATIS",
                TODAY,
                null
        );

        assertEquals(80.00, res.shipping);
    }

    @Test
    public void FreteAposDesconto() {
        var itens = List.of(new Item("ROUPA", 353.00, 1));
        var res = service.checkout(
                itens,
                CustomerTier.SILVER,
                false,
                "NORTE",
                50.0,
                "DESC10",
                TODAY,
                null
        );

        assertEquals(0.00, res.shipping);
    }

    @Test
    public void MudarFrete() {
        var itens = List.of(new Item("ROUPA", 10.00, 1));

        var res1 = service.checkout(itens, CustomerTier.BASIC, false, "NORTE", 1.5, null, TODAY, null);
        assertEquals(30.00, res1.shipping);

        var res2 = service.checkout(itens, CustomerTier.BASIC, false, "NORTE", 4.0, null, TODAY, null);
        assertEquals(55.00, res2.shipping);

        var res3 = service.checkout(itens, CustomerTier.BASIC, false, "NORTE", 6.0, null, TODAY, null);
        assertEquals(80.00, res3.shipping);
    }

    @Test
    public void MudarFreteSudeste() {
        var itens = List.of(new Item("ROUPA", 10.00, 1));

        var res1 = service.checkout(itens, CustomerTier.BASIC, false, "SUL", 1.5, null, TODAY, null);
        assertEquals(20.00, res1.shipping);

        var res2 = service.checkout(itens, CustomerTier.BASIC, false, "SUDESTE", 4.0, null, TODAY, null);
        assertEquals(35.00, res2.shipping);

        var res3 = service.checkout(itens, CustomerTier.BASIC, false, "SUL", 6.0, null, TODAY, null);
        assertEquals(50.00, res3.shipping);
    }

    @Test
    public void FreteFixo() {
        var itens = List.of(new Item("ROUPA", 10.00, 1));

        var res1 = service.checkout(itens, CustomerTier.BASIC, false, "CENTRO-OESTE", 1.0, null, TODAY, null);
        assertEquals(40.00, res1.shipping);

        var res2 = service.checkout(itens, CustomerTier.BASIC, false, null, 1.0, null, TODAY, null);
        assertEquals(40.00, res2.shipping);
    }

    @Test
    public void deveLancarExcecaoSePesoDeFreteNegativo() {
        var itens = List.of(new Item("ROUPA", 10.00, 1));
        assertThrows(IllegalArgumentException.class, () -> {
            service.checkout(itens, CustomerTier.BASIC, false, "SUL", -1.0, null, TODAY, null);
        });
    }

    @Test
    public void ImpostoZero() {
        var itens = List.of(
                new Item("BOOK", 100.00, 1),
                new Item("book", 50.00, 2)
        );

        var res = service.checkout(
                itens,
                CustomerTier.BASIC,
                false,
                "SUL", 1.0, null, TODAY, null
        );

        assertEquals(200.00, res.subtotal);
        assertEquals(0.00, res.tax);
    }

    @Test
    public void ImpostoTributavel() {
        var itens = List.of(new Item("ELETRONICO", 100.00, 1));
        var res = service.checkout(
                itens,
                CustomerTier.SILVER,
                false,
                "SUL", 1.0, null, TODAY, null
        );

        assertEquals(11.40, res.tax);
    }
}