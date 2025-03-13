package tech.zerofiltre.testing.calcul.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import tech.zerofiltre.testing.calcul.domain.model.Calculation;
import tech.zerofiltre.testing.calcul.domain.model.CalculationModel;
import tech.zerofiltre.testing.calcul.service.CalculatorService;

 class CalculatorControllerTest {

    @Mock
    private CalculatorService calculatorService;

    @InjectMocks
    private CalculatorController calculatorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndexRedirectsToVueCalculator() {
        String viewName = calculatorController.index(new Calculation());
        assertEquals("redirect:/vue-calculator", viewName);
    }

    @Test
    void testRootReturnsCalculatorTemplate() {
        String viewName = calculatorController.root(new Calculation());
        assertEquals(CalculatorController.CALCULATOR_TEMPLATE, viewName);
    }

    @Test
    void testCalculateReturnsBadRequestWhenErrorExists() {
        CalculationModel calculation = new CalculationModel();
        calculation.setError("Invalid input");

        ResponseEntity<CalculationModel> response = calculatorController.calculate(calculation);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Invalid input", response.getBody().getError());
    }

    @Test
    void testCalculateReturnsOkWhenNoError() {
        CalculationModel input = new CalculationModel();
        CalculationModel result = new CalculationModel();
        when(calculatorService.calculate(input)).thenReturn(result);

        ResponseEntity<CalculationModel> response = calculatorController.calculate(input);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(result, response.getBody());
    }

    @Test
    void testCalculateReturnsBadRequestWhenServiceReturnsError() {
        CalculationModel input = new CalculationModel();
        CalculationModel result = new CalculationModel();
        result.setError("Calculation error");
        when(calculatorService.calculate(input)).thenReturn(result);

        ResponseEntity<CalculationModel> response = calculatorController.calculate(input);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Calculation error", response.getBody().getError());
    }
}
