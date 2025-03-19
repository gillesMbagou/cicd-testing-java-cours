package tech.zerofiltre.testing.calcul.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class CalculationTest {

    @Test
    void testCalculation_GettersAndSetters() {
        // GIVEN - Création d'une instance de Calculation
        Calculation calculation = new Calculation();

        // WHEN - Définition des valeurs
        calculation.setCalculationType("addition");
        calculation.setLeftArgument(10);
        calculation.setRightArgument(5);

        // THEN - Vérification des valeurs
        assertThat(calculation.getCalculationType()).isEqualTo("addition");
        assertThat(calculation.getLeftArgument()).isEqualTo(10);
        assertThat(calculation.getRightArgument()).isEqualTo(5);
    }

    @Test
    void testCalculation_DefaultValues() {
        // GIVEN - Une nouvelle instance
        Calculation calculation = new Calculation();

        // THEN - Vérification que les valeurs par défaut sont null
        assertThat(calculation.getCalculationType()).isNull();
        assertThat(calculation.getLeftArgument()).isNull();
        assertThat(calculation.getRightArgument()).isNull();
    }

}