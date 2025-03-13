package tech.zerofiltre.testing.calcul.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import tech.zerofiltre.testing.calcul.domain.model.CalculationModel;
import tech.zerofiltre.testing.calcul.domain.model.CalculationType;

@ExtendWith(MockitoExtension.class)
 class CalculatorServiceTest {
	private CalculatorServiceImpl  classUnderTest;



	@BeforeEach
	void setUp() {
		 classUnderTest = new CalculatorServiceImpl();

	}

	@Test
	void calculate_shouldReturnError_whenTypeIsNull() {
		// GIVEN
		CalculationModel model = new CalculationModel();
		model.setType(null);

		// WHEN
		CalculationModel result =  classUnderTest.calculate(model);

		// THEN
		assertThat(result.getError()).isEqualTo("Type de calcul non spécifié");
	}

	@Test
	void calculate_shouldReturnResult_forValidAddition() {
		// GIVEN
		CalculationModel model = new CalculationModel(CalculationType.ADDITION, 5.0, 3.0, null);

		// WHEN
		CalculationModel result =  classUnderTest.calculate(model);

		// THEN
		assertThat(result.getSolution()).isEqualTo(8.0);
		assertThat(result.getError()).isNull();
	}

	@Test
	void calculate_shouldHandleError_whenCalculationExceptionOccurs() {
		// GIVEN
		CalculationModel model = new CalculationModel(CalculationType.DIVISION, 5.0, 0.0, null);

		// WHEN
		CalculationModel result =  classUnderTest.calculate(model);

		// THEN
		assertThat(result.getError()).isEqualTo("Division par zéro impossible");
		assertThat(result.getSolution()).isNull();
	}

	@Test
	void prepareOperands_shouldHandleUnaryOperation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		// Obtenir la méthode privée via réflexion
		Method prepareOperandsMethod = CalculatorServiceImpl.class.getDeclaredMethod("prepareOperands",
				CalculationModel.class, CalculationType.class);
		prepareOperandsMethod.setAccessible(true);
		// GIVEN
		CalculationModel model = new CalculationModel(CalculationType.SQUARE, 4.0, 0., null);

		// WHEN
		double[] result = (double[]) prepareOperandsMethod.invoke(classUnderTest,model, CalculationType.SQUARE);

		// THEN
		assertThat(result).hasSize(1);
		assertThat(result[0]).isEqualTo(4.0);
	}

	@Test
	void prepareOperands_shouldHandleBinaryOperation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		// Obtenir la méthode privée via réflexion
		Method prepareOperandsMethod = CalculatorServiceImpl.class.getDeclaredMethod("prepareOperands",
				CalculationModel.class, CalculationType.class);
		prepareOperandsMethod.setAccessible(true);
		// GIVEN
		CalculationModel model = new CalculationModel(CalculationType.ADDITION, 4.0, 2.0, null);

		// WHEN
		double[] result = (double[]) prepareOperandsMethod.invoke(classUnderTest,model, CalculationType.ADDITION);

		// THEN
		assertThat(result).hasSize(2);
		assertThat(result[0]).isEqualTo(4.0);
		assertThat(result[1]).isEqualTo(2.0);
	}
	@Test
	void calculate_shouldReturnError_whenTypeIsNullWithLRarguments() {
		// GIVEN
		CalculationModel calculationModel = new CalculationModel();
		calculationModel.setLeftArgument(2.0);
		calculationModel.setRightArgument(3.0);
		calculationModel.setType(null);

		// WHEN
		CalculationModel result = classUnderTest.calculate(calculationModel);

		// THEN
		assertThat(result.getError()).isEqualTo("Type de calcul non spécifié");
	}

	@Test
	void calculate_shouldHandleUnaryOperation_whenOperandCountIsOne() {
		// GIVEN
		CalculationModel calculationModel = new CalculationModel();
		calculationModel.setLeftArgument(4.0);
		calculationModel.setType(CalculationType.SQUARE);

		// WHEN
		CalculationModel result = classUnderTest.calculate(calculationModel);

		// THEN
		assertThat(result.getSolution()).isEqualTo(16.0);
	}
	@Test
	void testPrepareOperands() throws Exception {
		// Obtenir la méthode privée via réflexion
		Method prepareOperandsMethod = CalculatorServiceImpl.class.getDeclaredMethod("prepareOperands",
				CalculationModel.class, CalculationType.class);
		prepareOperandsMethod.setAccessible(true);

		// Créer les données de test
		CalculationModel model = new CalculationModel(CalculationType.ADDITION, 5.0, 3.0, null);

		// Invoquer la méthode privée
		double[] result = (double[]) prepareOperandsMethod.invoke(classUnderTest, model, CalculationType.ADDITION);

		// Vérifier le résultat
		assertThat(result).hasSize(2);
		assertThat(result[0]).isEqualTo(5.0);
		assertThat(result[1]).isEqualTo(3.0);
	}
}
