package tech.zerofiltre.testing.calcul.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tech.zerofiltre.testing.calcul.domain.model.Calculation;
import tech.zerofiltre.testing.calcul.domain.model.CalculationModel;

import tech.zerofiltre.testing.calcul.service.CalculatorService;

@AllArgsConstructor
@Controller
@CrossOrigin(origins = "http://localhost:8081/")
public class CalculatorController {

    public static final String CALCULATOR_TEMPLATE = "vue-calculator";
    CalculatorService calculatorService;
    @GetMapping("/")
    public String index(Calculation calculation) {
        return "redirect:/vue-calculator";
    }

    @GetMapping("/vue-calculator")
    public String root(Calculation calculation) {
        return CALCULATOR_TEMPLATE; // cf. resources/templates/calculator.html
    }

    @PostMapping("/calculator")
    public ResponseEntity<CalculationModel> calculate(@Valid @RequestBody CalculationModel calculation) {

        if (calculation.getError() != null) {
            return ResponseEntity.badRequest().body(calculation);
        }
        CalculationModel result = calculatorService.calculate(calculation);

        return result.getError() == null ?
                ResponseEntity.ok(result) :
                ResponseEntity.badRequest().body(result);

    }

}
