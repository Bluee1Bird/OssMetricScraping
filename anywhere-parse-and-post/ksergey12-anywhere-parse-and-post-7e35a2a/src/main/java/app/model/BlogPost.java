package app.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BlogPost {

    private Map postContent = new HashMap();

    public BlogPost(String title, String content,
                    String excerpt, String status, Date date,
                    String[] categories, String[] tags) {
        postContent.put("title", title);
        postContent.put("description", content);
        postContent.put("post_status", status);
        postContent.put("mt_excerpt", excerpt);
        postContent.put("dateCreated", date);
        postContent.put("categories", categories);
        postContent.put("mt_keywords", tags);
    }

    public Map getPostContent(){
        return postContent;
    }
}
