package app.service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.ibm.icu.text.Transliterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import app.model.Placeholder;


public class ParserServiceImpl implements ParserService {
    private Placeholder placeholder;

    public ParserServiceImpl(String properties) {
        placeholder = new Placeholder(properties).invoke();
    }

    @Override
    public Map<String, String> parseUrl(String inputLink) throws IOException {
        Document document = Jsoup.connect(inputLink)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; rv:46.0) Gecko/20100101 Firefox/46.0")
                .cookie("language", "ru")
                .cookie("currency", "UAH")
        /**
         * Workaround for "java.net.SocketTimeoutException: Read timed out"
         */
                .timeout(10 * 1000)
                .get();
        assert document != null;
        Element image = document.select(placeholder.getImage()).first();
        String imageUrl = image.absUrl("src"); //absolute URL on src
        String imageFileName = imageUrl.substring(imageUrl.lastIndexOf("/"), imageUrl.length()).replace("/", ""); // this will give you name.png

        Element titleEl = document.select(placeholder.getTitle()).first();
        String title = Jsoup.clean(String.valueOf(titleEl), Whitelist.none());

        String excerpt = "";
        document.getElementsByClass("article").remove();
        document.getElementsByClass("views").remove();
        Element excerptEl = document.select(placeholder.getExcerpt()).first();
        excerpt = excerpt + String.valueOf(excerptEl);

        String description = "";
        Elements descriptionEl1 = document.select(placeholder.getDescription());

        for (Element currentElement : descriptionEl1) {
            description += String.valueOf(currentElement) + "<br /><br />";
        }

        String footer = "<p>Купить <i>" + title + "</i> Вы можете в специализированных магазинах наложенным платежом или по предоплате с доставкой по всей Украине.</p>\n" +
                "\n" +
                "<p>Контактные телефоны: <b>(095) 278 84 47, (098) 349 16 10, (093) 56 23 777</b></p>" +
                "\n" +
                "<p>Внимание! Информация об этом товаре и контактные данные предоставлены нашим партнером.</p>";

        description = filterHtml(description);
        excerpt = Jsoup.clean(filterHtml(excerpt), Whitelist.none());
        String content = description + "<hr />" + footer;

        Map<String, String> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);
        result.put("excerpt", excerpt);
        result.put("imageFileName", imageFileName);
        result.put("imageUrl", imageUrl);

        return result;
    }

    public String filterHtml(String input) {
        input = input.replaceAll("(?s)<!--.*?-->", "")
                .replaceAll("(?s)<button.*?/button>", "")
                .replaceAll("\\r\\n|\\r|\\n", " ")
                .replaceAll("&nbsp;", " ")
                .replaceAll(" +", " ")
                .replaceAll("<p> </p>", "")
                .replaceAll("<p></p>", "")
                .trim();

        return Jsoup.clean(input, Whitelist.relaxed().removeTags("u", "div"));
    }

    public String transliteration(String input) {
        Transliterator cyrillicToLatin = Transliterator.getInstance("Cyrillic-Latin");
        return cyrillicToLatin.transliterate(input).replaceAll("ʹ", "");
    }
}