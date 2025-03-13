package tech.zerofiltre.testing.calcul.domain;

import java.math.BigInteger;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.inject.Named;

import static java.lang.String.*;

@Named
public class Calculator {
	private  final ConcurrentHashMap<Integer,BigInteger> memo = new ConcurrentHashMap<>();
	// Pré-calcul des valeurs tenant dans un long (0! à 20!)
	private static final long[] LONG_FACTORIALS = {
			1L,                   // 0!
			1L,                   // 1!
			2L,                   // 2!
			6L,                   // 3!
			24L,                  // 4!
			120L,                 // 5!
			720L,                 // 6!
			5040L,                // 7!
			40320L,               // 8!
			362880L,              // 9!
			3628800L,             // 10!
			39916800L,            // 11!
			479001600L,           // 12!
			6227020800L,          // 13!
			87178291200L,         // 14!
			1307674368000L,       // 15!
			20922789888000L,      // 16!
			355687428096000L,      // 17!
			6402373705728000L,     // 18!
			121645100408832000L,   // 19!
			2432902008176640000L   // 20!
	};
	private static final int PRECOMPUTED_LIMIT = LONG_FACTORIALS.length - 1;

	public double add(int a, int b) {
		return Math.addExact(a,b); // Détection des dépassements
	}

	public double sub(int a, int b) {
		return Math.subtractExact(a, b); // Détection des dépassements
	}

	public double multiply(int a, int b) {
		return Math.multiplyExact(a, b); // Détection des dépassements
	}

	public double divide(int a, int b) {
		if (b == 0) {
			throw new ArithmeticException("Division by zero");
		}
		return (double) a/b; // Détection des dépassements
	}
	public double divide(double multiply, int b) {
		if (b == 0) {
			throw new ArithmeticException("Division by zero");
		}
		return  multiply/b; // Détection des dépassements
	}
	public int mod(int a, int b) {  if (b == 0) {
		throw new ArithmeticException("Modulo by zero");
	}
		return a % b;}

	public BigInteger factorial(int n) {
		if (n < 0 ) {
			throw new IllegalArgumentException("a is negative");
		}
		if (n <= PRECOMPUTED_LIMIT) {
			return BigInteger.valueOf(LONG_FACTORIALS[n]);
		}
		return memo.computeIfAbsent(n, this::computeFactorial);
	}

	private  BigInteger computeFactorial(int n) {
		BigInteger result = BigInteger.valueOf(LONG_FACTORIALS[PRECOMPUTED_LIMIT]);
		for (int i = PRECOMPUTED_LIMIT + 1; i <= n; i++) {
			result = result.multiply(BigInteger.valueOf(i));
		}
		return result;
	}
	public  CompletableFuture<Void> longCalculation() {
		return CompletableFuture.runAsync(() -> {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});
	}

	public Set<Integer> digitsSet(int number) {
		return String.valueOf(Math.abs(number))
				.chars()
				.map(Character::getNumericValue)
				.boxed()
				.collect(Collectors.toSet());

	}


}
