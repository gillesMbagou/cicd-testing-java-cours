package tech.zerofiltre.testing.calcul.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import javax.inject.Named;

import static java.nio.file.Paths.get;

/**
 * Reads a text file and returns a stream of calculations
 */
@Named
public class BatchCalculationFileServiceImpl implements BatchCalculationFileService {

	/**
	 * Returns a stream of the rows in a file with file. The file format should have
	 * a calculation with a mathematical operator and an argument on each side,
	 * separated by a spaces. Eg:
	 *
	 * <pre>
	 *     2 + 2
	 *     3 - 2
	 *     2022 / 4
	 * </pre>
	 *
	 * @param file path of the batch file
	 * @return Stream of calculations
	 */
	@Override
	public Stream<String> read(String file) throws IOException {
		BufferedReader reader = Files.newBufferedReader(get(file));
		return reader.lines()
				.onClose(() -> {
					try {
						reader.close();
					} catch (IOException e) {
						throw new UncheckedIOException(e);
					}
				});
		}


}
