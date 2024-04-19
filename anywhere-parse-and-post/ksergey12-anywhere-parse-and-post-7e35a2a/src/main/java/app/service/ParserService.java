package app.service;

import java.io.IOException;
import java.util.Map;


public interface ParserService {
    Map<String, String> parseUrl(String inputLink) throws IOException;

    String filterHtml(String input);

    String transliteration(String input);
}
