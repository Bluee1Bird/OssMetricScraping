package app.model;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;


public class Utils {
    public static ArrayList<String> readFile(String fileName) throws FileNotFoundException {
        ArrayList<String> names = new ArrayList<>();
        Scanner scanner = new Scanner(new File(fileName));

        while (scanner.hasNextLine()) {
            names.add(scanner.nextLine());
        }
        scanner.close();

        return names;
    }

    public static void wrightFile(Map<String, String> map) throws IOException {
        File fileTwo = new File("d:\\list.csv");
        FileOutputStream fos = new FileOutputStream(fileTwo);
        PrintWriter pw = new PrintWriter(fos);

        for (Map.Entry<String, String> m : map.entrySet()) {
            pw.println(m.getKey() + "\t" + m.getValue());
        }
        pw.flush();
        pw.close();
        fos.close();
    }

    public static void saveImage(String imageLocation, String outputFolder, String name) throws IOException {
        //Open a URL Stream
        Connection.Response resultImageResponse = Jsoup.connect(imageLocation)
                .ignoreContentType(true).execute();

        // output here
        FileOutputStream out = (new FileOutputStream(new java.io.File(outputFolder + name)));
        out.write(resultImageResponse.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
        out.close();
        System.out.println("imageLocation -> " + imageLocation);
    }

    public static String getContentBetweenTags(String input, String startTag, String endTag) {
        int startPosition = input.indexOf(startTag) + startTag.length();
        int endPosition = input.indexOf(endTag, startPosition);

        return input.substring(startPosition, endPosition);
    }
}