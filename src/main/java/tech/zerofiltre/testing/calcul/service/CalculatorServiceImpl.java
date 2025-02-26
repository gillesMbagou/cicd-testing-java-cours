package tech.zerofiltre.testing.calcul.service;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.zerofiltre.testing.calcul.domain.Calculator;
import tech.zerofiltre.testing.calcul.domain.model.CalculationModel;
import tech.zerofiltre.testing.calcul.domain.model.CalculationType;

@Named
public class CalculatorServiceImpl implements CalculatorService {

	private static final Logger logger = LoggerFactory.getLogger(CalculatorServiceImpl.class);

	private final Calculator calculator;

	private final SolutionFormatter solutionFormatter;

	public CalculatorServiceImpl(Calculator calculator, SolutionFormatter solutionFormatter) {
		this.calculator = calculator;
		this.solutionFormatter = solutionFormatter;
	}

	@Override
	public CalculationModel calculate(CalculationModel calculationModel) {
		final CalculationType type = calculationModel.getType();

		Integer response = null;
		switch (type) {
		case ADDITION:
			response = calculator.add(calculationModel.getLeftArgument(), calculationModel.getRightArgument());
			break;
		case SUBTRACTION:
			response = calculator.sub(calculationModel.getLeftArgument(), calculationModel.getRightArgument());
			break;
		case MULTIPLICATION:
			response = calculator.multiply(calculationModel.getLeftArgument(), calculationModel.getRightArgument());
			break;
		case DIVISION:
			if (calculationModel.getRightArgument() == 0) {
				logger.warn("ATTEMPTED DIVISION BY ZERO DETECTED.");
					throw new IllegalArgumentException("the denominator cannot be zero");
				}
			response = calculator.divide(calculationModel.getLeftArgument(), calculationModel.getRightArgument());
		    break;
		case MODULO:
				if (calculationModel.getRightArgument() == 0) {
					logger.warn("MODULO ATTEMPT BY ZERO DETECTED.");
					throw new IllegalArgumentException("the modulo by zero is not defined.");
				}
				response = calculator.mod(calculationModel.getLeftArgument(), calculationModel.getRightArgument());
				break;
		default:
			throw new UnsupportedOperationException("Unsupported calculations");
		}

		calculationModel.setSolution(response);
		calculationModel.setFormattedSolution(solutionFormatter.format(response));
		return calculationModel;
	}

}
