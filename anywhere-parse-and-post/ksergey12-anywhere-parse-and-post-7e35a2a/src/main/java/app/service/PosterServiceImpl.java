package app.service;

import app.model.Connection;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * The PosterServiceImpl class implements posting to blog via
 * XML-RPC MetaWeblog API. Details in the context of WordPress
 * can be found on http://codex.wordpress.org/XML-RPC_MetaWeblog_API
 *
 * Algorithm to post blog entry with attached featured image:
 *
 * 1) use the newPost function and post the content without
 * the featured image and also set publish to false, record
 * the post_id returned by this;
 *
 * 2) upload the image and set the post_id to the id of the
 * postEntry just posted, record the image_id;
 *
 * 3) when done edit the postEntry and set the wp_post_thumbnail equal
 * to the image_id you just uploaded and also set publish to true(if needed).
 */

public class PosterServiceImpl implements PosterService {
    private Connection connection;

    public PosterServiceImpl(String properties) {
        connection = new Connection(properties).invoke();
    }

    @Override
    public Object postEntry(Map postContent) throws ParseException {

        String xmlRpcUrl = connection.getXmlRpcUrl();
        String username = connection.getUsername();
        String password = connection.getPassword();

        try {
            XmlRpcClient client = new XmlRpcClient(xmlRpcUrl, true);

            return client.invoke("metaWeblog.newPost", new Object[]{new Integer(1), username, password,
                    postContent, false});
        } catch (XmlRpcException | XmlRpcFault | MalformedURLException e) {
            e.printStackTrace();
        }
        return new Object();
    }

    @Override
    public Object uploadImage(int postToAttach, String urlString, String imageFileName) throws IOException {
        String xmlRpcUrl = connection.getXmlRpcUrl();
        String username = connection.getUsername();
        String password = connection.getPassword();

        URL url = new URL(urlString);
        byte[] bits = downloadUrl(url);
        assert bits != null;

        try {
            XmlRpcClient client = new XmlRpcClient(xmlRpcUrl, true);
            Map<String, Object> data = new HashMap<>();
            String mime = getMimeType(urlString);

            data.put("name", imageFileName);
            data.put("type", mime);
            data.put("bits", bits);
            data.put("post_id", postToAttach);
            data.put("overwrite", true);

            return client.invoke("metaWeblog.newMediaObject", new Object[]{new Integer(1), username, password,
                    data, true});
        } catch (XmlRpcException | XmlRpcFault | MalformedURLException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    @Override
    public Object attachImage(int postId, int attachID) throws ParseException {

        String xmlRpcUrl = connection.getXmlRpcUrl();
        String username = connection.getUsername();
        String password = connection.getPassword();

        try {
            XmlRpcClient client = new XmlRpcClient(xmlRpcUrl, true);

            HashMap content = new HashMap();
            content.put("wp_post_thumbnail", attachID);

            return client.invoke("metaWeblog.editPost", new Object[]{postId, username, password,
                    content, false});
        } catch (XmlRpcException | XmlRpcFault | MalformedURLException e) {
            e.printStackTrace();
        }
        return new Object();
    }

    private byte[] downloadUrl(URL toDownload) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] chunk = new byte[1048576];
            int bytesRead;
            InputStream stream = toDownload.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return outputStream.toByteArray();
    }

    private String getMimeType(String fileUrl)
            throws IOException {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();

        return fileNameMap.getContentTypeFor(fileUrl);
    }
}
