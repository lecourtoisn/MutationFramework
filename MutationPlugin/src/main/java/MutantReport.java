import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.zeroturnaround.zip.ZipUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mojo(name = "report")
public class MutantReport extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        copierTemplate();

        Map<String, String> map = new HashMap<String, String>();
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

        Map<String, Boolean> res=lireXML();
        remplirDataGraph(res);
        genererOutPutHtml(res);
    }

    public static void main(String[] args) {
        copierTemplate();
    }
    private static void copierTemplate() {
        String resourceName="Resultat-HTML.zip";
        //URL url=ClassLoader.getSystemResource(resourceName);
        InputStream is = MutantReport.class.getResourceAsStream(resourceName);
        try {
            File target = new File("./target/" + resourceName);
            OutputStream os = new FileOutputStream(target);
            IOUtils.copy(is, os);
            ZipUtil.unpack(target, new File("target"));
            target.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public Map lireXML() {
        Map<String, Boolean> mutants = new HashMap<String, Boolean>(); //Couples mutant (string) et bool (true:killed et false:non killed)

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Element racine = null;

        try {
            //Cr�ation du parseur
            final DocumentBuilder builder = factory.newDocumentBuilder();

            String[] dir = new java.io.File("./target/spooned").list( );

            for (int i=0; i<dir.length; i++) {
                String[] mutantRep = new java.io.File("./target/spooned/"+dir[i]+"/target/surefire-reports").list( );
                boolean tue = false;
                for (int j=0; j<mutantRep.length; j++) { //On parcourt les r�sultats des tests, classe par classe
                    if (mutantRep[j].endsWith(".xml")) {

                        final Document document = builder.parse(new File("./target/spooned/" + dir[i]+"/target/surefire-reports/"+mutantRep[j]));
                        racine = document.getDocumentElement();
                        if (Integer.valueOf(racine.getAttribute("failures")) >= 1) {
                            tue = true;
                        }
                    }
                }
                mutants.put(dir[i], tue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mutants;
    }

    static public void remplirDataGraph (Map<String, Boolean> mutants) {
        String adressedufichier = "./target/Resultat-HTML/js/morris-data.js";
        Map<Boolean, Integer> pourcentages= calculerPourcentage(mutants);

        try
        {
            FileReader lireFichier= new FileReader(adressedufichier);
            String ligne = "";
            int i = 0;
            // Boucle permettant de parcourir tout le fichier
            while((i = lireFichier.read()) != -1){
                ligne += (char)i;
            }
            String nouvelleString="value: " + pourcentages.get(true) +", label: 'Mutants tues'";
            String l2= ligne.replaceAll("value: (\\d*).(\\d*), label: 'Mutants tues'", nouvelleString);
            String nouvelleString2="value: " + pourcentages.get(false) +", label: 'Mutants non tues'";
            String l3= l2.replaceAll("value: (\\d*).(\\d*), label: 'Mutants non tues'", nouvelleString2);

            FileWriter ecrireFichier = new FileWriter(adressedufichier, false);
            ecrireFichier.write(l3);
            // "Fermeture" du FileWriter
            ecrireFichier.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    static private Map calculerPourcentage(Map<String, Boolean> mutants){
        Map<Boolean, Double> res=new HashMap(); //mutant non tu�s (false), pourcentage...
        int nbTot=mutants.size();//2
        int nbTues=0;
        for(Boolean b: mutants.values()){
            if(b) nbTues++;
        }
        DecimalFormat df = new DecimalFormat ( ) ;
        df.setMaximumFractionDigits(2) ; //arrondi à 2 chiffres apres la virgules
        df.setMinimumFractionDigits(2) ;
        res.put(true,  Double.parseDouble(df.format(((double)nbTues/(double)nbTot)*100)));
        res.put(false,  Double.parseDouble(df.format(((double)(nbTot-nbTues)/(double)nbTot)*100)));
        return res;
    }


    static public void genererOutPutHtml(Map<String, Boolean> mutants) {

        String adressedufichier = "./target/Resultat-HTML/index.html";

        try
        {
            FileReader lireFichier= new FileReader(adressedufichier);
            String ligne = "";
            int i = 0;
            // Boucle permettant de parcourir tout le fichier
            while((i = lireFichier.read()) != -1){
                ligne += (char)i;
            }
            String[] s=ligne.split("<!--Debut des details-->");
            String nouveauContenu=s[0]+"<!--Debut des details-->";
            for(String nomMutant: mutants.keySet()){
                if(mutants.get(nomMutant)){
                    nouveauContenu+="<div class=\"col-lg-8\"> <div class=\"panel panel-success\"> <div class=\"panel-heading\">Mutant tue </div><div class=\"panel-body\">";
                    nouveauContenu+=("<p>Mutation: "+ nomMutant +"</p> </div> </div> </div>");
                }else {
                    nouveauContenu+="<div class=\"col-lg-8\"> <div class=\"panel panel-danger\"> <div class=\"panel-heading\">Mutant non tue </div><div class=\"panel-body\">";
                    nouveauContenu+=("<p>Mutation: "+ nomMutant +"</p> </div> </div> </div>");
                }
            }
            nouveauContenu+="<!--Fin des details-->";
            String end=s[1].split("<!--Fin des details-->")[1];
            nouveauContenu+=end;

            FileWriter ecrireFichier = new FileWriter(adressedufichier, false);
            ecrireFichier.write(nouveauContenu);
            // "Fermeture" du FileWriter
            ecrireFichier.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
