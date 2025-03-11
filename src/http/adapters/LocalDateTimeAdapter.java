package http.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter  extends TypeAdapter<LocalDateTime> {
    private  final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        {
          jsonWriter.value(localDateTime.format(formatterWriter));
      }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
       String timeString=jsonReader.nextString();
        return LocalDateTime.parse(timeString, formatterReader);
    }
}
