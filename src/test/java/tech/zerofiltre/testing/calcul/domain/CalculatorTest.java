package tech.zerofiltre.testing.calcul.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import tech.zerofiltre.testing.calcul.domain.model.CalculationModel;
import tech.zerofiltre.testing.calcul.domain.model.CalculationType;

   class CalculatorTest {

	private static Instant startedAt;

	private Calculator calculatorUnderTest;


	@BeforeEach
	  void initCalculator() {
		calculatorUnderTest = new Calculator();
	}

	@AfterEach
	  void undefCalculator() {
		calculatorUnderTest = null;
	}

	@BeforeAll
	  static void initStartingTime() {
		System.out.println("Appel avant tous les tests");
		startedAt = Instant.now();
	}

	@AfterAll
	  static void showTestDuration() {
		System.out.println("Appel après tous les tests");
		final Instant endedAt = Instant.now();
		final long duration = Duration.between(startedAt, endedAt).toMillis();
		System.out.println(MessageFormat.format("Durée des tests : {0} ms", duration));
	}

	@Test
	  void testAddTwoPositiveNumbers() {
		// Arrange
		final int a = 2;
		final int b = 3;

		// Act
		final double somme = calculatorUnderTest.add(a, b);

		// Assert
		assertThat(somme).isEqualTo(5);
		assertEquals(5, somme);
	}

	@Test
	  void multiply_shouldReturnTheProduct_ofTwoIntegers() {
		// Arrange
		final int a = 42;
		final int b = 11;

		// Act
		final double produit = calculatorUnderTest.multiply(a, b);

		// Assert
		assertEquals(462, produit);
	}

	@ParameterizedTest(name = "{0} x 0 doit être égal à 0")
	@ValueSource(ints = { 1, 2, 42, 1011, 5089 })
	  void multiply_shouldReturnZero_ofZeroWithMultipleIntegers(int arg) {
		// Arrange -- Tout est prêt !

		// Act -- Multiplier par zéro
		final double actualResult = calculatorUnderTest.multiply(arg, 0);

		// Assert -- ça vaut toujours zéro !
		assertEquals(0, actualResult);
	}

	@ParameterizedTest(name = "{0} + {1} doit être égal à {2}")
	@CsvSource({ "1,1,2", "2,3,5", "42,57,99" })
	  void add_shouldReturnTheSum_ofMultipleIntegers(int arg1, int arg2, int expectResult) {
		// Arrange -- Tout est prêt !

		// Act
		final double actualResult = calculatorUnderTest.add(arg1, arg2);

		// Assert
		assertEquals(expectResult, actualResult);
	}

	@Timeout(1)
	@Test
	  void longCalcul_shouldComputeInLessThan1Second() throws Exception {
		CompletableFuture<Void> future = calculatorUnderTest.longCalculation();

		// Attend la fin du traitement avec un timeout
		future.get(1, TimeUnit.SECONDS); // Échoue si non terminé après 1s

		// Vérifie que le traitement est terminé
		assertThat(future.isDone()).isTrue();
		assertThat(future.isCompletedExceptionally()).isFalse();
	}

	@Test
	  void listDigits_shouldReturnsTheListOfDigits_ofPositiveInteger() {
		// GIVEN
		final int number = 95897;

		// WHEN
		final Set<Integer> actualDigits = calculatorUnderTest.digitsSet(number);

		// THEN
		assertThat(actualDigits).containsExactlyInAnyOrder(9, 5, 8, 7);
		final Set<Integer> expectedDigits = Stream.of(5, 7, 8, 9).collect(Collectors.toSet());
		assertEquals(expectedDigits, actualDigits);
	}

	@Test
	  void listDigits_shouldReturnsTheListOfDigits_ofNegativeInteger() {
		final int number = -124432;
		final Set<Integer> actualDigits = calculatorUnderTest.digitsSet(number);
		assertThat(actualDigits).containsExactlyInAnyOrder(1, 2, 3, 4);
	}

	@Test
	  void listDigits_shouldReturnsTheListOfZero_ofZero() {
		final int number = 0;
		final Set<Integer> actualDigits = calculatorUnderTest.digitsSet(number);
		assertThat(actualDigits).containsExactly(0);
	}

	@Test
	  void fact12_shouldReturnsTheCorrectAnswer() {
		// GIVEN
		final int number = 12;

		// WHEN
		final int cacheFactorial = calculatorUnderTest.factorial(number).intValue();

		// THEN
		assertThat(cacheFactorial).isEqualTo(12 * 11 * 10 * 9 * 8 * 7 * 6 * 5 * 4 * 3 * 2);

	}

	@Test
	  void digitsSetOfFact12_shouldReturnsTheCorrectAnswser() {
		// GIVEN
		final int cacheFactorial = 479001600;

		// WHEN
		final Set<Integer> actualDigits = calculatorUnderTest.digitsSet(cacheFactorial);

		// THEN
		assertThat(actualDigits).containsExactlyInAnyOrder(0, 1, 4, 6, 7, 9);
	}

	@Test
	  void multiplyAndDivide_shouldBeIdentity() {
		// GIVEN
		final Random r = new Random();
		final int a = 1 + r.nextInt(100);
		final int b = 1 + r.nextInt(3);

		// WHEN on multiplie a par b puis on divise par b
		final double c = calculatorUnderTest.divide(calculatorUnderTest.multiply(a, b), b);

		// THEN on ré-obtient a
		assertThat(c).isEqualTo(a);
	}
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

	@Test
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
	void testCalculatorOperations() {

		assertEquals(5,  calculatorUnderTest.add(2, 3));
		assertEquals(-1,  calculatorUnderTest.sub(2, 3));
		assertEquals(6,  calculatorUnderTest.multiply(2, 3));
		assertEquals(2.0,  calculatorUnderTest.divide(6, 3));
		assertThrows(ArithmeticException.class, () ->  calculatorUnderTest.divide(6, 0));
		assertEquals(2,  calculatorUnderTest.mod(5, 3));
		assertThrows(ArithmeticException.class, () ->  calculatorUnderTest.mod(5, 0));
	}

	@Test
	void testFactorial() {
		assertEquals(BigInteger.valueOf(120),  calculatorUnderTest.factorial(5));
		assertEquals(BigInteger.valueOf(1),  calculatorUnderTest.factorial(0));
		assertThrows(IllegalArgumentException.class, () ->  calculatorUnderTest.factorial(-1));
	}

	@Test
	void testAddition() {
		assertThat( calculatorUnderTest.add(2, 3)).isEqualTo(5);
		assertThatExceptionOfType(ArithmeticException.class)
				.isThrownBy(() ->  calculatorUnderTest.add(Integer.MAX_VALUE, 1));
	}

	@Test
	void testSubtraction() {
		assertThat( calculatorUnderTest.sub(5, 3)).isEqualTo(2);
		assertThatExceptionOfType(ArithmeticException.class)
				.isThrownBy(() ->  calculatorUnderTest.sub(Integer.MIN_VALUE, 1));
	}

	@Test
	void testMultiplication() {
		assertThat( calculatorUnderTest.multiply(3, 4)).isEqualTo(12);
		assertThatExceptionOfType(ArithmeticException.class)
				.isThrownBy(() ->  calculatorUnderTest.multiply(Integer.MAX_VALUE, 2));
	}

	@Test
	void testIntegerDivision() {
		assertThat( calculatorUnderTest.divide(10, 2)).isEqualTo(5.0);
		assertThatExceptionOfType(ArithmeticException.class)
				.isThrownBy(() ->  calculatorUnderTest.divide(5, 0))
				.withMessage("Division by zero");
	}

	@Test
	void testDoubleDivision() {
		assertThat( calculatorUnderTest.divide(10.0, 2)).isEqualTo(5.0);
		assertThatExceptionOfType(ArithmeticException.class)
				.isThrownBy(() ->  calculatorUnderTest.divide(5.0, 0))
				.withMessage("Division by zero");
	}

	@Test
	void testModulo() {
		assertThat( calculatorUnderTest.mod(10, 3)).isEqualTo(1);
		assertThatExceptionOfType(ArithmeticException.class)
				.isThrownBy(() ->  calculatorUnderTest.mod(10, 0))
				.withMessage("Modulo by zero");
	}

	@Test
	void testFactorial_PrecomputedValues() {
		assertThat( calculatorUnderTest.factorial(5)).isEqualTo(BigInteger.valueOf(120));
	}

	@Test
	void testFactorial_ComputedValues() {
		assertThat( calculatorUnderTest.factorial(25)).isEqualTo(new BigInteger("15511210043330985984000000"));
	}

	@Test
	void testFactorial_NegativeInput() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() ->  calculatorUnderTest.factorial(-1))
				.withMessage("a is negative");
	}

	@Test
	@Timeout(value = 1, unit = TimeUnit.SECONDS)
	void testLongCalculation() {
		CompletableFuture<Void> future =  calculatorUnderTest.longCalculation();

		Awaitility.await()
				.atMost(600, TimeUnit.MILLISECONDS)
				.until(future::isDone);

		assertThat(future).isCompleted();
	}

	@Test
	void testDigitsSet() {
		assertThat( calculatorUnderTest.digitsSet(1234)).containsExactlyInAnyOrder(1, 2, 3, 4);
		assertThat( calculatorUnderTest.digitsSet(-987)).containsExactlyInAnyOrder(9, 8, 7);
	}

   }
