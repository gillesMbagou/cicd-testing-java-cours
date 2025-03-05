package tech.zerofiltre.testing.calcul.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import tech.zerofiltre.testing.calcul.domain.strategy.CalculationStrategy;
import tech.zerofiltre.testing.calcul.exception.strategy.CalculationException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public enum CalculationType {
    ADDITION(Set.of("+", "plus","add"),"+", 2, ops -> ops[0] + ops[1]),
    SUBTRACTION(Set.of("-","moins","sub"),"-", 2, ops -> ops[0] - ops[1]),
    MULTIPLICATION(Set.of("×", "x", "X", "*"),"×", 2, ops -> ops[0] * ops[1]),
    DIVISION(Set.of("/", "÷","div"),"/", 2, ops -> {
        if(ops[1] == 0) throw new CalculationException("Division par zéro impossible");
        return ops[0] / ops[1];
    }),
    MODULO(Set.of("%","mod"),"%", 2, ops -> {
        if(ops[1] == 0) throw new CalculationException("Modulo par zéro impossible");
        return ops[0] % ops[1];
    }),
    SQUARE(Set.of("x²","sqr","carre","²"),"x²", 1, ops -> ops[0] * ops[0]),
    SQUARE_ROOT(Set.of("√","root","racine","sqrt"),"√", 1, ops -> {
        if(ops[0] < 0) throw new CalculationException("Racine carrée d'un nombre négatif");
        return Math.sqrt(ops[0]);
    });

    private final Set<String> symbols;
    private final String primarySymbol;
    private final int operandCount;
    private final CalculationStrategy strategy;

    // Map globale pour la recherche rapide
    private static final Map<String, CalculationType> SYMBOL_MAP = new HashMap<>();

    static {
        // Initialiser la map pour toutes les valeurs
        for (CalculationType type : values()) {
            for (String symbol : type.symbols) {
                SYMBOL_MAP.put(symbol.toLowerCase(), type);
            }
        }
    }

    @JsonValue
    public String getPrimarySymbol() {
        return primarySymbol;
    }

    @JsonCreator
    public static CalculationType fromSymbol(String symbol) {
        CalculationType type = SYMBOL_MAP.get(symbol.toLowerCase());
        if (type == null) {
            throw new IllegalArgumentException("Opération non supportée: " + symbol);
        }
        return type;
    }

    CalculationType(Set<String> symbols, String primarySymbol, int operandCount, CalculationStrategy strategy) {
        this.symbols = symbols;
        this.primarySymbol = primarySymbol;
        this.operandCount = operandCount;
        this.strategy = strategy;
    }

    public double apply(double[] operands) {
        validateOperands(operands);
        return strategy.compute(operands);
    }

    private void validateOperands(double[] operands) {
        if(operands.length != operandCount) {
            throw new CalculationException("Nombre d'opérandes invalide pour " + primarySymbol);
        }
    }

}
