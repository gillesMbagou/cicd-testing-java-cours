package tech.zerofiltre.testing.calcul.service;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.zerofiltre.testing.calcul.domain.Calculator;
import tech.zerofiltre.testing.calcul.domain.model.CalculationModel;
import tech.zerofiltre.testing.calcul.domain.model.CalculationType;
import tech.zerofiltre.testing.calcul.exception.strategy.CalculationException;

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

		// Validation préalable
		if(calculationModel.getType() == null) {
			calculationModel.setError("Type de calcul non spécifié");
			return calculationModel;
		}
		final CalculationType type = calculationModel.getType();
		double[] operands = prepareOperands(calculationModel, type);

		try {
			double result = type.apply(operands);
			calculationModel.setSolution(result);
		} catch (CalculationException e) {
			calculationModel.setError(e.getMessage());
		}

		return calculationModel;
	}

	private double[] prepareOperands(CalculationModel calculation, CalculationType type) {
		double[] operands = new double[type.getOperandCount()];
		operands[0] = calculation.getLeftArgument();

		if(type.getOperandCount() > 1) {
			operands[1] = calculation.getRightArgument();
		}

		return operands;
	}

}
