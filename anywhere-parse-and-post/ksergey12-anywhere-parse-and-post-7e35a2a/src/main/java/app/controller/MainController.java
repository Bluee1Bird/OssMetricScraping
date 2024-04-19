package app.controller;

import app.model.BlogPost;
import app.service.ParserService;
import app.service.PosterService;
import app.view.View;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.Date;

import static app.model.Utils.readFile;


class MainController {
    private ParserService parser;
    private PosterService poster;
    private View view;

    MainController(ParserService parser, PosterService poster, View view) {
        this.parser = parser;
        this.view = view;
        this.poster = poster;
    }

    void run() throws IOException, InterruptedException, ParseException {
        ArrayList<String> links = readFile("src/main/resources/links.txt");
        long lStartTime = System.currentTimeMillis();

        for (int i = 0; i < links.size(); i++) {
            String currentLink = links.get(i);
            view.write((i + 1) + " -> " + currentLink);

            Map<String, String> parseResult = parser.parseUrl(currentLink);
            String title = parseResult.get("title");
            String content = parseResult.get("content");
            String excerpt = parseResult.get("excerpt");
            String imageFileName = parseResult.get("imageFileName");
            imageFileName = parser.transliteration(imageFileName);
            String imageUrl = parseResult.get("imageUrl");

            int hour = new Date(System.currentTimeMillis()).getHours();
            int min = new Date(System.currentTimeMillis()).getMinutes();
            int sec = new Date(System.currentTimeMillis()).getSeconds();
            Date date = new Date("01/06/2012 " + hour + ":" + min + ":" + sec);

            String[] categories = {"Гербициды"};
            String[] tags = {"гербициды"};

            BlogPost entry = new BlogPost(title, content, excerpt, "draft", date, categories, tags);

            int newPostId;
            newPostId = Integer.parseInt(String.valueOf(poster.postEntry(entry.getPostContent())));
            Thread.sleep(1000);

            if (!imageFileName.isEmpty()) {
                Map uploaderResponse = (Map) poster.uploadImage(newPostId, imageUrl, imageFileName);
                view.write("uploadImage>>>" + uploaderResponse.toString());
                Thread.sleep(1000);

                int newImageId = Integer.parseInt(String.valueOf(uploaderResponse.get("id")));
                Object attachResponse = poster.attachImage(newPostId, newImageId);
                view.write("attachResponse>>>" + attachResponse.toString() + "\n");
            } else {
                view.write("No image found.");
            }
            /**
             * This will pause parsing at specified time in millis. Useful when you get
             * java.io.ioexception: too many redirects occurred trying to load url
             */
            Thread.sleep(5000);
        }

        long lEndTime = System.currentTimeMillis();
        long difference = lEndTime - lStartTime;

        view.write(String.format("Time: %d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(difference),
                TimeUnit.MILLISECONDS.toSeconds(difference) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difference))
        ));
    }
}
