# Anywhere Parse&amp;Post
Automated system for parsing sites and remote publication of the retrieved data via XML-RPC protocol.

## Quick start
1. Import as a Maven project.
2. Open `resources` folder and supply configuration properties:
  - `xmlrpc.properties`: login, password and path to xmlrpc.php file on yuor site;
  - `placeholder.properties`: specify the unique selector of title, excerpt, description and image elements of desired webpage;
  - `links.txt`: populate this file with links to webpages that would be parsed, each link on a new line.
3. In case of connection problems you can check xmlrpc functionality of your blog [here](http://xmlrpc.eritreo.it/).
4. Run `Main` class.
