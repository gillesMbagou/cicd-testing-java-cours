package tech.zerofiltre.testing.calcul.domain.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import tech.zerofiltre.testing.calcul.exception.strategy.CalculationException;
import tech.zerofiltre.testing.calcul.service.CalculatorServiceImpl;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

class CalculationTypeTest {
    CalculatorServiceImpl classUnderTest;
    Logger logger = mock(Logger.class);



    @BeforeEach
    void setUp()  {
        classUnderTest = new CalculatorServiceImpl();
    }

    @ParameterizedTest
    @MethodSource("validSymbolProvider")
    void fromSymbol_shouldResolveAllSymbolVariants(String symbol, CalculationType expectedType) {
        assertThat(CalculationType.fromSymbol(symbol)).isEqualTo(expectedType);
    }

    @Test
    void fromSymbol_shouldBeCaseInsensitive() {
        assertThat(CalculationType.fromSymbol("Add")).isEqualTo(CalculationType.ADDITION);
        assertThat(CalculationType.fromSymbol("DIV")).isEqualTo(CalculationType.DIVISION);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "++", "xyz"})
    void fromSymbol_shouldThrowForInvalidSymbols(String invalidSymbol) {
        assertThatThrownBy(() -> CalculationType.fromSymbol(invalidSymbol))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("non supportée");
    }

    @ParameterizedTest
    @MethodSource("operationExecutionProvider")
    void apply_shouldExecuteCorrectOperation(CalculationType type, double[] operands, double expected) {
        assertThat(type.apply(operands)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("invalidOperandCountProvider")
    void apply_shouldValidateOperandCount(CalculationType type, double[] operands) {
        assertThatThrownBy(() -> type.apply(operands))
                .isInstanceOf(CalculationException.class)
                .hasMessageContaining("opérandes invalide");
    }

    @Test
    void apply_shouldHandleSpecialCases() {
        assertThatThrownBy(() -> CalculationType.DIVISION.apply(new double[]{5, 0}))
                .isInstanceOf(CalculationException.class)
                .hasMessage("Division par zéro impossible");

        assertThatThrownBy(() -> CalculationType.SQUARE_ROOT.apply(new double[]{-4}))
                .isInstanceOf(CalculationException.class)
                .hasMessage("Racine carrée d'un nombre négatif");
    }

    @Test
    void getPrimarySymbol_shouldReturnCorrectSymbol() {
        assertThat(CalculationType.ADDITION.getPrimarySymbol()).isEqualTo("+");
        assertThat(CalculationType.SQUARE_ROOT.getPrimarySymbol()).isEqualTo("√");
    }

    private static Stream<Arguments> validSymbolProvider() {
        return Stream.of(
                arguments("+", CalculationType.ADDITION),
                arguments("plus", CalculationType.ADDITION),
                arguments("×", CalculationType.MULTIPLICATION),
                arguments("x", CalculationType.MULTIPLICATION),
                arguments("sqrt", CalculationType.SQUARE_ROOT),
                arguments("²", CalculationType.SQUARE),
                arguments("mod", CalculationType.MODULO)
        );
    }

    private static Stream<Arguments> operationExecutionProvider() {
        return Stream.of(
                arguments(CalculationType.ADDITION, new double[]{2, 3}, 5),
                arguments(CalculationType.SUBTRACTION, new double[]{5, 2}, 3),
                arguments(CalculationType.MULTIPLICATION, new double[]{4, 2.5}, 10),
                arguments(CalculationType.DIVISION, new double[]{10, 4}, 2.5),
                arguments(CalculationType.MODULO, new double[]{10, 3}, 1),
                arguments(CalculationType.SQUARE, new double[]{5}, 25),
                arguments(CalculationType.SQUARE_ROOT, new double[]{9}, 3)
        );
    }

    private static Stream<Arguments> invalidOperandCountProvider() {
        return Stream.of(
                arguments(CalculationType.ADDITION, new double[]{5}),
                arguments(CalculationType.SQUARE, new double[]{2, 3}),
                arguments(CalculationType.MODULO, new double[]{5})
        );
    }


    @Test
    void testAddition() {
        double result = CalculationType.ADDITION.apply(new double[]{2, 3});
        assertEquals(5, result);
    }

    @Test
    void testSubtraction() {
        double result = CalculationType.SUBTRACTION.apply(new double[]{5, 3});
        assertEquals(2, result);
    }

    @Test
    void testMultiplication() {
        double result = CalculationType.MULTIPLICATION.apply(new double[]{2, 3});
        assertEquals(6, result);
    }

    @Test
    void testDivision() {
        double result = CalculationType.DIVISION.apply(new double[]{6, 2});
        assertEquals(3, result);
    }

    @Test
    void testDivisionByZero() {
        Exception exception = assertThrows(CalculationException.class, () ->
                CalculationType.DIVISION.apply(new double[]{6, 0})
        );
        assertEquals("Division par zéro impossible", exception.getMessage());
    }

    @Test
    void testModulo() {
        double result = CalculationType.MODULO.apply(new double[]{5, 2});
        assertEquals(1, result);
    }

    @Test
    void testModuloByZero() {
        Exception exception = assertThrows(CalculationException.class, () ->
                CalculationType.MODULO.apply(new double[]{5, 0})
        );
        assertEquals("Modulo par zéro impossible", exception.getMessage());
    }

    @Test
    void testSquare() {
        double result = CalculationType.SQUARE.apply(new double[]{4});
        assertEquals(16, result);
    }

    @Test
    void testSquareRoot() {
        double result = CalculationType.SQUARE_ROOT.apply(new double[]{9});
        assertEquals(3, result);
    }

    @Test
    void testSquareRootNegative() {
        Exception exception = assertThrows(CalculationException.class, () ->
                CalculationType.SQUARE_ROOT.apply(new double[]{-9})
        );
        assertEquals("Racine carrée d'un nombre négatif", exception.getMessage());
    }

    @Test
    void testFromSymbolValid() {
        assertEquals(CalculationType.ADDITION, CalculationType.fromSymbol("+"));
        assertEquals(CalculationType.SUBTRACTION, CalculationType.fromSymbol("moins"));
        assertEquals(CalculationType.MULTIPLICATION, CalculationType.fromSymbol("*"));
        assertEquals(CalculationType.DIVISION, CalculationType.fromSymbol("div"));
    }

    @Test
    void testFromSymbolInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CalculationType.fromSymbol("unknown")
        );
        assertEquals("Opération non supportée: unknown", exception.getMessage());
    }

}