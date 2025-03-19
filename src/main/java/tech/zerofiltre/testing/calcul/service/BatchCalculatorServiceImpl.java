package tech.zerofiltre.testing.calcul.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.zerofiltre.testing.calcul.domain.model.CalculationModel;

@Named
public class BatchCalculatorServiceImpl implements BatchCalculatorService {

	private final CalculatorService calculatorService;
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchCalculatorServiceImpl.class);


	public BatchCalculatorServiceImpl(CalculatorService calculatorService) {
		this.calculatorService = calculatorService;
	}

	@Override
	public List<CalculationModel> batchCalculate(Stream<String> operations) {
		return operations
				.map(this::parseAndCalculateSafely)
				.toList();
	}

	private CalculationModel parseAndCalculateSafely(String operation) {
		try {
			CalculationModel model = CalculationModel.fromText(operation);
			return calculatorService.calculate(model);
		} catch (Exception e) {
			LOGGER.error("Erreur lors du calcul de l'opération '{}'", operation, e);
            return createErrorModel(operation, e);
		}
	}

	private CalculationModel createErrorModel(String operation, Exception e) {
		CalculationModel errorModel = new CalculationModel();
		errorModel.setError("Erreur avec '" + operation + "': " + e.getMessage());
		return errorModel;
	}

}
