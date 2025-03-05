package tech.zerofiltre.testing.calcul.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * A model to represent a two argument integer calculation which needs to be
 * performed.
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculationModel {
	private static final String SEPARATOR = " ";
	private Double leftArgument;
	private Double rightArgument;
	@NotNull(message = "Le type de calcul est obligatoire")
	private CalculationType type;
	private Double solution;
	private String formattedSolution;
	private String formattedError;

	// Constructeur pour les opérations unaires
	public CalculationModel(String typeSymbol, double left) {
		this.type = CalculationType.fromSymbol(typeSymbol);
		this.leftArgument = left;
	}

	// Constructeur pour les opérations binaires
	public CalculationModel(String typeSymbol, double left, double right) {
		this.type = CalculationType.fromSymbol(typeSymbol);
		this.leftArgument = left;
		this.rightArgument = right;
	}

	/**
	 * Convenience Constructor used in test
	 */
	public CalculationModel(CalculationType calculationType, double leftArgument, double rightArgument, Double solution) {
		type = calculationType;
		this.leftArgument = leftArgument;
		this.rightArgument = rightArgument;
		this.solution = solution;
	}

	private CalculationModel(CalculationType calculationType, double left, double right) {
		this.type = calculationType;
		this.leftArgument = left;
		this.rightArgument = right;

	}

	public static CalculationModel fromText(String operation) {
		String[] parts = operation.split("\\s+");
		if(parts.length != 3) {
			throw new IllegalArgumentException("Format d'opération invalide: " + operation);
		}

		return new CalculationModel(
				parseOperator(parts[1]),
				Double.parseDouble(parts[0]),
				Double.parseDouble(parts[2])
		);
	}

	private static CalculationType parseOperator(String symbol) {
		return CalculationType.fromSymbol(symbol); // Utilise la logique de symboles multiples
	}


	public Double getLeftArgument() {
		return leftArgument;
	}

	public void setLeftArgument(Double leftArgument) {
		this.leftArgument = leftArgument;
	}

	public Double getRightArgument() {
		return rightArgument;
	}

	public void setRightArgument(Double rightArgument) {
		this.rightArgument = rightArgument;
	}

	public CalculationType getType() {
		return type;
	}

	public void setType(CalculationType type) {
		this.type = type;
	}

	public Double getSolution() {
		return solution;
	}

	public void setSolution(Double solution) {
		this.solution = solution;
	}

	public String getFormattedSolution() {
		return formattedSolution;
	}

	public void setFormattedSolution(String formattedSolution) {
		this.formattedSolution = formattedSolution;
	}

	public void setError(String message) {this.formattedError = message;}

	public String getError() { return  this.formattedError ; }

}
