package tech.zerofiltre.testing.calcul.domain.model;

public enum CalculationType {
    ADDITION,
    MULTIPLICATION,
    DIVISION,
    SUBTRACTION,
    MODULO,
    CONVERSION;

    public static CalculationType fromSymbol(String operation) {
        switch (operation) {
            case "+":
                return ADDITION;
            case "-":
                return SUBTRACTION;
            case "/":
                return DIVISION;
            case "*" :
            case "x":
                return MULTIPLICATION;
            case "%":
                return MODULO;
            default:
                throw new UnsupportedOperationException("Not implemented yet");
        }
    }
}
