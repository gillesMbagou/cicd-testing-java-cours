package tech.zerofiltre.testing.calcul.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import tech.zerofiltre.testing.calcul.domain.model.CalculationModel;

 class CalculatorServiceIT {

	// Initialiser la classe à tester
	private final CalculatorService underTest = new CalculatorServiceImpl();

	@Test
	 void calculatorService_shouldCalculateASolution_whenGivenACalculationModel() {
		// GIVEN
		final CalculationModel calculation = new CalculationModel("+",100.0,101.0);
			// WHEN
		final CalculationModel result = underTest.calculate(calculation);

		// THEN
		assertThat(result.getSolution()).isEqualTo(201.);
	}
}
