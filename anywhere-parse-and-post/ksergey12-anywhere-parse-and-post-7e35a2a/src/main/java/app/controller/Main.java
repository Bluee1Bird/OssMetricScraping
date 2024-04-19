package app.controller;

import app.service.ParserService;
import app.service.ParserServiceImpl;
import app.service.PosterService;
import redstone.xmlrpc.XmlRpcFault;
import app.service.PosterServiceImpl;
import app.view.Console;
import app.view.View;

import java.io.IOException;
import java.text.ParseException;


public class Main {
    public static void main(String[] args) throws XmlRpcFault, InterruptedException,
                                                  ParseException, IOException {
        ParserService parser = new ParserServiceImpl("src/main/resources/placeholder.properties");
        PosterService poster = new PosterServiceImpl("src/main/resources/xmlrpc.properties");
        View view = new Console();
        MainController controller = new MainController(parser, poster, view);

        controller.run();
    }
}
