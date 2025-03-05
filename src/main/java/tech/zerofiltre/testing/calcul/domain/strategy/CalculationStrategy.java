package tech.zerofiltre.testing.calcul.domain.strategy;

import tech.zerofiltre.testing.calcul.exception.strategy.CalculationException;

@FunctionalInterface
public interface CalculationStrategy {
    double compute(double... operands) throws CalculationException;
}
