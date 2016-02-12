package htmlgenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Created by Lucas on 12/02/2016.
 */
public class TestDom4J {

    public static void main(final String[] args) {

        Map<String, String> map = new HashMap<String, String>();
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

        map = lireXML("test.xml");
        list.add(map);
        map = lireXML("test.xml");
        list.add(map);

        genererOutPut("output.html", list);
    }

    static public Map lireXML(String inpoutFile) {

        Map<String, String> map = new HashMap<String, String>();
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Element racine = null;

        try {
            //Création du parseur
            final DocumentBuilder builder = factory.newDocumentBuilder();

            //Création d'un document
            final Document document = builder.parse(new File("./output/" + inpoutFile));

            //On récupére la racine
            racine = document.getDocumentElement();

            map.put("time", racine.getAttribute("time"));
            map.put("tests", racine.getAttribute("tests"));
            map.put("errors", racine.getAttribute("errors"));
            map.put("skipped", racine.getAttribute("skipped"));
            map.put("failures", racine.getAttribute("failures"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

     static public void genererOutPut (String outputFile, ArrayList<Map<String, String>> list) {
        /*
        Ecriture
         */
        String adressedufichier = "./output/"+ outputFile;
        try
        {
            FileWriter fw = new FileWriter(adressedufichier, false);
            BufferedWriter output = new BufferedWriter(fw);

            output.write("<!DOCTYPE html>\n" + "<html>\n" + "<head>\n");
            output.write("<title>Output</title>\n" + "</head>\n"+"<body>\n");

            for( int i = 0; i < list.size(); i++) {
                output.write("<ul>\n");

                for (Map.Entry<String, String> entry : list.get(i).entrySet()) {
                    output.write("<li><a>" + entry.getKey() + " : " + entry.getValue() + "</li>\n");
                }

                output.write("</ul>\n");
            }

            output.write("</body>\n"+"</html>\n");

            output.flush();
            output.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
