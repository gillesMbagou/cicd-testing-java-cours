package tech.zerofiltre.testing.calcul.service;

import javax.inject.Named;

import lombok.Getter;
import tech.zerofiltre.testing.calcul.domain.model.CalculationModel;
import tech.zerofiltre.testing.calcul.domain.model.CalculationType;
import tech.zerofiltre.testing.calcul.exception.strategy.CalculationException;

@Getter
@Named
public class CalculatorServiceImpl implements CalculatorService {

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
