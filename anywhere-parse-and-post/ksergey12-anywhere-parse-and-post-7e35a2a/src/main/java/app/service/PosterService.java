package app.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;


public interface PosterService {
    Object postEntry(Map postContent) throws ParseException;

    Object uploadImage(int postToAttach, String urlString, String imageFileName) throws IOException;

    Object attachImage(int postId, int attachID) throws ParseException;
}
