package de.iconten.client.rest.csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.opencsv.CSVWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvProvider {

	public static void writeData(String filePath, List<String[]> data) {
		final File file = new File(filePath);
		try (final FileOutputStream fos = new FileOutputStream(file); final Writer fw = new OutputStreamWriter(fos, StandardCharsets.ISO_8859_1)) {
			final CSVWriter writer = new CSVWriter(fw);
			writer.writeAll(data);
			writer.close();
		} catch (final IOException e) {
			e.printStackTrace();
			log.error("", e);
		}
	}

}
