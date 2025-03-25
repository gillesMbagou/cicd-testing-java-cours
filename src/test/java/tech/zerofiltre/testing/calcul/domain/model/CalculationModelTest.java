package tech.zerofiltre.testing.calcul.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculationModelTest {

    String regex = "^\\s*([-+]?\\d*\\.?\\d+)\\s*([+\\-*/x])\\s*([-+]?\\d*\\.?\\d+)\\s*$";


    @Test
    void testUnaryConstructor() {
        CalculationModel model = new CalculationModel("sqr", 4);
        assertEquals(CalculationType.SQUARE, model.getType());
        assertEquals(4, model.getLeftArgument());
        assertNull(model.getRightArgument());
    }

    @Test
    void testBinaryConstructor() {
        CalculationModel model = new CalculationModel("+", 3, 5);
        assertEquals(CalculationType.ADDITION, model.getType());
        assertEquals(3, model.getLeftArgument());
        assertEquals(5, model.getRightArgument());
    }

    @Test
    void testFromTextValid() {
        CalculationModel model = CalculationModel.fromText("3 + 5");
        assertEquals(CalculationType.ADDITION, model.getType());
        assertEquals(3, model.getLeftArgument());
        assertEquals(5, model.getRightArgument());
    }

    @Test
    void testFromTextInvalidFormat() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CalculationModel.fromText("3 +")
        );
        assertEquals("Format d'opération invalide: 3 +", exception.getMessage());
    }

    @ Test
    void testSettersAndGetters() {
        CalculationModel model = new CalculationModel();
        model.setLeftArgument(10.0);
        model.setRightArgument(2.0);
        model.setType(CalculationType.DIVISION);
        model.setSolution(5.0);
        model.setFormattedSolution("10 / 2 = 5");
        model.setError("Aucune erreur");

        assertEquals(10.0, model.getLeftArgument());
        assertEquals(2.0, model.getRightArgument());
        assertEquals(CalculationType.DIVISION, model.getType());
        assertEquals(5.0, model.getSolution());
        assertEquals("10 / 2 = 5", model.getFormattedSolution());
        assertEquals("Aucune erreur", model.getError());
    }
    @Test
    void testFromText_ValidInputWithX() {
        CalculationModel model = CalculationModel.fromText("6 x 8");
        assertEquals("*", model.getType().getPrimarySymbol()); // 'x' doit être converti en '*'
        assertEquals(6.0, model.getLeftArgument());
        assertEquals(8.0, model.getRightArgument());
    }


    @Test
    void testFromText_InvalidOperator() {
        assertThrows(IllegalArgumentException.class, () -> CalculationModel.fromText("6 ^ 8")); // '^' est invalide
    }

    @Test
    void testFromText_ValidInputWithOtherOperators() {
        CalculationModel model = CalculationModel.fromText("5 * 3");
        assertEquals("*",model.getType().getPrimarySymbol());
        assertEquals(5.0, model.getLeftArgument());
        assertEquals(3.0, model.getRightArgument());

    }

    @Test
    void testRegex_ValidInputs() {

        assertTrue("2 + 3".matches(regex));
        assertTrue("5.5 * 2.3".matches(regex));
        assertTrue("-10 / -5".matches(regex));
        assertTrue("2x3".matches(regex)); // Sans espaces
    }

    @Test
    void testRegex_InvalidInputs() {
        assertFalse("".matches(regex)); // Chaîne vide
        assertFalse("2+".matches(regex)); // Trop peu d'éléments
        assertFalse("2 + 3 + 4".matches(regex)); // Trop d'éléments
        assertFalse("2 ^ 3".matches(regex)); // Opérateur invalide
        assertFalse("abc + 3".matches(regex)); // Nombre invalide
    }

}