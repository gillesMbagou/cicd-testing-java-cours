package tech.zerofiltre.testing.calcul.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DoubleCalculatorTest {
	private Calculator calculatorUnderTest;

	@BeforeEach
	  void initCalculator() {
		calculatorUnderTest = new Calculator();
	}

	@Test
	  void subTwoIntegers_shouldReturnCorrectResult() {
		// GIVEN
		int a = 10;
		int b = 5;

		// WHEN
		double result = calculatorUnderTest.sub(a, b);

		// THEN
		assertEquals(5.0, result, "La soustraction de 10 et 5 devrait donner 5.0");
	}

	@Test
	  void subTwoIntegers_withOverflow_shouldThrowArithmeticException() {
		// GIVEN
		int a = Integer.MIN_VALUE;
		int b = 1;

		// WHEN & THEN
		ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
			calculatorUnderTest.sub(a, b);
		});

		assertEquals("integer overflow", exception.getMessage(), "L'exception devrait indiquer un dépassement de capacité");
	}

}
