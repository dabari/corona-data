package de.iconten.client.rest.json;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DateFormatTypeAdapter extends TypeAdapter<Date> {

	@Override
	public void write(JsonWriter out, Date value) throws IOException {
		if (value != null) {
			out.value(value.getTime());
		} else {
			out.nullValue();
		}
	}

	@Override
	public Date read(JsonReader in) throws IOException {
		try {
			final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

			final String nextValue = in.nextString();
			if (nextValue.contains(",")) {
				final String value = nextValue.split(",")[0];
				return df.parse(value.trim());
			} else {
				final long timestamp = Long.parseLong(nextValue);
				final Date date = new Date(timestamp);
				return date;
			}
		} catch (final ParseException e) {
			throw new IOException(e);
		}
	}

}
