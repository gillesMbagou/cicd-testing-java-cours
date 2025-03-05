package tech.zerofiltre.testing.calcul.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import tech.zerofiltre.testing.calcul.domain.Calculator;
import tech.zerofiltre.testing.calcul.domain.model.CalculationModel;
import tech.zerofiltre.testing.calcul.domain.model.CalculationType;
import tech.zerofiltre.testing.calcul.service.CalculatorService;
import tech.zerofiltre.testing.calcul.service.CalculatorServiceImpl;
import tech.zerofiltre.testing.calcul.service.SolutionFormatter;

@WebMvcTest(controllers = { CalculatorController.class, CalculatorService.class })
@ExtendWith(SpringExtension.class)
public class CalculatorControllerSIT {

	@Inject
	private MockMvc mockMvc;

	@MockBean
	private SolutionFormatter solutionFormatter;

	@MockBean
	private Calculator calculator;

	@MockBean
	private CalculatorService calculatorService;

	/*@Test
	public void givenACalculatorApp_whenRequestToAdd_thenSolutionIsShown() throws Exception {
		// GIVEN
		when(calculator.add(2, 3)).thenReturn(5.);

		// WHEN
		final MvcResult result = mockMvc.perform(
				post("/calculator")
						.param("leftArgument", "2")
						.param("rightArgument", "3")
						.param("type", "+"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andReturn();

		// THEN
		assertThat(result.getResponse().getContentAsString())
				.contains("id=\"solution\"")
				.contains(">5</span");
		verify(calculator).add(2, 3);
		verify(solutionFormatter).format(5);
	}*/

	@Test
	public void givenACalculatorApp_whenRequestToAdd_thenSolutionIsShown() throws Exception {
		// GIVEN
		CalculationModel request = new CalculationModel();
		request.setLeftArgument(2.0);
		request.setRightArgument(3.0);
		request.setType(CalculationType.ADDITION); 

		CalculationModel expectedResponse = new CalculationModel();
		expectedResponse.setSolution(5.0);
		expectedResponse.setFormattedSolution("5");

		when(calculatorService.calculate(any(CalculationModel.class))).thenReturn(expectedResponse);

		// WHEN
		mockMvc.perform(post("/calculator")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(request)))

				// THEN
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect( jsonPath("$.solution", is(5.0)))
				.andExpect(jsonPath("$.formattedSolution", is("5")))
				.andExpect(jsonPath("$.error").doesNotExist());

		verify(calculatorService).calculate(argThat(model ->
				model.getLeftArgument() == 2.0 &&
						model.getRightArgument() == 3.0 &&
						model.getType() == CalculationType.ADDITION
		));
	}

	@Test
	public void givenTextOperation_whenCalculate_thenReturnJsonSolution() throws Exception {
		String requestBody = "{ \"type\": \"+\", \"leftArgument\": 2, \"rightArgument\": 3 }";

		// Créer une réponse mockée valide
		CalculationModel mockResponse = new CalculationModel();
		mockResponse.setSolution(5.0);
		mockResponse.setError(null);

		when(calculatorService.calculate(any(CalculationModel.class))).thenReturn(mockResponse);

		mockMvc.perform(post("/calculator")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.solution").exists())
				.andExpect(jsonPath("$.solution").value(5.0));
	}
}
