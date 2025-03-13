package tech.zerofiltre.testing.calcul.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculationModelTest {

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

}