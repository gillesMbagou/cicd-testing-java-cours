package tech.zerofiltre.testing.calcul.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SolutionFormatterTest {

	private SolutionFormatter solutionFormatter;

	@BeforeEach
	public void initFormatter() {
		solutionFormatter = new SolutionFormatterImpl();
	}

	@Test
	public void format_shouldFormatAnyBigNumber() {
		// GIVEN
		final int number = 1234567890;

		// WHEN
		final String result = solutionFormatter.format(number);

		// Normaliser les espaces dans le résultat
		final String normalizedResult = result.replaceAll("[\\u00A0\\u202F]", " "); // Remplace U+00A0 et U+202F par un espace standard

		// THEN
		assertThat(normalizedResult).isEqualTo("1 234 567 890"); // Utilise un espace standard
	}

}
