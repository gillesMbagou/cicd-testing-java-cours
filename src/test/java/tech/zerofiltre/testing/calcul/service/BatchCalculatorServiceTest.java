package tech.zerofiltre.testing.calcul.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tech.zerofiltre.testing.calcul.domain.model.CalculationModel;
import tech.zerofiltre.testing.calcul.domain.model.CalculationType;

@ExtendWith(MockitoExtension.class)
class BatchCalculatorServiceTest {

	@Mock
	CalculatorService calculatorService;


	BatchCalculatorService batchCalculatorService;


	BatchCalculatorServiceImpl batchCalculatorServiceNoMock;

	@BeforeEach
	  void init() {
		batchCalculatorService = new BatchCalculatorServiceImplBuilder().setCalculatorService(calculatorService).createBatchCalculatorServiceImpl		();

		batchCalculatorServiceNoMock = new BatchCalculatorServiceImplBuilder().setCalculatorService(new CalculatorServiceImpl()).createBatchCalculatorServiceImpl();
	}

	@Test
	  void givenOperationsList_whenbatchCalculate_thenReturnsCorrectAnswerList()
			 {
		// GIVEN
		final Stream<String> operations = Stream.of("2 + 2", "5 - 4", "6 x 8", "9 / 3");

		// WHEN
		final List<CalculationModel> results = batchCalculatorServiceNoMock.batchCalculate(operations);

		// THEN
		assertThat(results).extracting(CalculationModel::getSolution).containsExactly(4.0, 1.0, 48.0, 3.0);
	}

	@Test
	  void givenOperationsList_whenbatchCalculate_thenCallsServiceWithCorrectArguments() {
		// GIVEN
		final Stream<String> operations = Stream.of("2 + 2", "5 - 4", "6 x 8", "9 / 3");
		final ArgumentCaptor<CalculationModel> calculationModelCaptor = ArgumentCaptor.forClass(CalculationModel.class);

		// WHEN
		batchCalculatorService.batchCalculate(operations);

		// THEN
		verify(calculatorService, times(4)).calculate(calculationModelCaptor.capture());
		final List<CalculationModel> calculationModels = calculationModelCaptor.getAllValues();
		assertThat(calculationModels)
				.extracting(CalculationModel::getLeftArgument, CalculationModel::getType,
						CalculationModel::getRightArgument)
				.containsExactly(
						tuple(2.0, CalculationType.ADDITION, 2.0),
						tuple(5., CalculationType.SUBTRACTION, 4.),
						tuple(6., CalculationType.MULTIPLICATION, 8.),
						tuple(9., CalculationType.DIVISION, 3.));
	}

	@Test
	  void givenOperationsList_whenbatchCalculate_thenCallsServiceAndReturnsAnswer() {
		// GIVEN
		final Stream<String> operations = Stream.of("2 + 2", "5 - 4", "6 x 8", "9 / 3");
		when(calculatorService.calculate(any(CalculationModel.class)))
				.then(invocation -> {
					final CalculationModel model = invocation.getArgument(0, CalculationModel.class);
					switch (model.getType()) {
					case ADDITION:
						model.setSolution(4.0);
						break;
					case SUBTRACTION:
						model.setSolution(1.0);
						break;
					case MULTIPLICATION:
						model.setSolution(48.0);
						break;
					case DIVISION:
						model.setSolution(3.0);
						break;
					default:
					}
					return model;
				});

		// WHEN
		final List<CalculationModel> results = batchCalculatorService.batchCalculate(operations);

		// THEN
		verify(calculatorService, times(4)).calculate(any(CalculationModel.class));
		assertThat(results).extracting("solution").containsExactly(4., 1., 48., 3.);

	}

	@Test
	  void givenOperationsList_whenbatchCalculate_thenCallsServiceAndReturnsAnswer2() {
		// GIVEN
		final Stream<String> operations = Stream.of("2 + 2", "5 - 4", "6 x 8", "9 / 3");
		when(calculatorService.calculate(any(CalculationModel.class)))
				.thenReturn(new CalculationModel(CalculationType.ADDITION, 2, 2, 4.0))
				.thenReturn(new CalculationModel(CalculationType.SUBTRACTION, 5, 4, 1.0))
				.thenReturn(new CalculationModel(CalculationType.MULTIPLICATION, 6, 8, 48.0))
				.thenReturn(new CalculationModel(CalculationType.DIVISION, 9, 3, 3.0));

		// WHEN
		final List<CalculationModel> results = batchCalculatorService.batchCalculate(operations);

		// THEN
		verify(calculatorService, times(4)).calculate(any(CalculationModel.class));
		assertThat(results).extracting("solution").containsExactly(4.0, 1.0, 48.0, 3.0);

	}
	@Test
	void testCreateErrorModel() {
		// GIVEN
		String operation = "division";
		Exception exception = new ArithmeticException("Division by zero");

		// WHEN
		CalculationModel result = invokeCreateErrorModel(operation, exception);

		// THEN
		assertThat(result).isNotNull();
		assertThat(result.getError()).isEqualTo("Erreur avec 'division': Division by zero");
	}

	// Utilisation de la réflexion pour tester une méthode privée
	private CalculationModel invokeCreateErrorModel(String operation, Exception e) {
		try {
			java.lang.reflect.Method method = BatchCalculatorServiceImpl.class.getDeclaredMethod("createErrorModel", String.class, Exception.class);
			method.setAccessible(true);
			return (CalculationModel) method.invoke(batchCalculatorServiceNoMock, operation, e);
		} catch (Exception ex) {
			throw new RuntimeException("Error invoking private method", ex);
		}
	}
	@Test
	void testBatchCalculate_AllValidOperations() {
		// GIVEN - Des opérations valides
		Stream<String> operations = Stream.of("2 + 2", "3 * 3");
		CalculationModel model1 = CalculationModel.fromText("2 + 2");
		CalculationModel model2 = CalculationModel.fromText("3 * 3");

		CalculationModel result1 = new CalculationModel(); // Simule un résultat
		CalculationModel result2 = new CalculationModel();

		when(calculatorService.calculate(model1)).thenReturn(result1);
		when(calculatorService.calculate(model2)).thenReturn(result2);

		// WHEN
		List<CalculationModel> results = batchCalculatorService.batchCalculate(operations);

		// THEN
		assertThat(results).containsExactly(result1, result2);
		verify(calculatorService, times(1)).calculate(model1);
		verify(calculatorService, times(1)).calculate(model2);
	}

	@Test
	void testBatchCalculate_EmptyStream() {
		// GIVEN - Un stream vide
		Stream<String> operations = Stream.empty();

		// WHEN
		List<CalculationModel> results = batchCalculatorService.batchCalculate(operations);

		// THEN
		assertThat(results).isEmpty();
		verifyNoInteractions(calculatorService);
	}

	@Test
	void batchCalculate_InvalidOperation_ReturnsErrorModel() {
		String invalidOp = "invalid_operation";

		List<CalculationModel> results = batchCalculatorServiceNoMock.batchCalculate(Stream.of(invalidOp));

		assertThat(results).hasSize(1);
		assertThat(results.get(0).getError()).startsWith("Erreur avec 'invalid_operation'");
		verify(calculatorService, never()).calculate(any());
	}


	@Test
	void batchCalculate_EmptyStream_ReturnsEmptyList() {
		List<CalculationModel> results = batchCalculatorServiceNoMock.batchCalculate(Stream.empty());
		assertThat(results).isEmpty();
		verify(calculatorService, never()).calculate(any());
	}

}
