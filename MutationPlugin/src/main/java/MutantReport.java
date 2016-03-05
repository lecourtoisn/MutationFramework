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

    /**
     * Déplace le dossier template de rapport du plugin dans le projet et le dezipe
     */
    private void copierTemplate() {
        String resourceName="Resultat-HTML.zip";

        InputStream is = MutantReport.class.getResourceAsStream(resourceName);
        try {
            File target = new File("./target/" + resourceName);
            OutputStream os = new FileOutputStream(target);
            IOUtils.copy(is, os);
            ZipUtil.unpack(target, new File("target"));
            is.close(); os.close();
            target.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lit les rapports de tests de JUnit et renvoie le résultat
     * @return Map<Nom du mutant, bool true si tué (false sinon)>
     *     TODO : Mutant mort nés
     */
     private Map lireXML() {
        Map<String, Boolean> mutants = new HashMap<String, Boolean>(); //Couples mutant (string) et bool (true:killed et false:non killed)

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Element racine = null;

        try {
            //Création du parseur
            final DocumentBuilder builder = factory.newDocumentBuilder();

            // Dir des spooned
            File dir = new File("./target/spooned");
            File[] mutantProjects = dir.listFiles();
            if (mutantProjects == null) {
                throw new Exception("Le goal testing doit avoir été exécuté avant de générer le site web");
            }
            for (File mutantProject : mutantProjects) {
                // Peut-être null si le repertoire n'existe pas
                File xmlReportsDir = new File(mutantProject.getPath().concat("/target/surefire-reports"));
                File[] xmlReports = xmlReportsDir.listFiles();
                if (xmlReports == null) {
                    throw new Exception("Pas de rapport à parser pour le mutant "+ mutantProject.getName());
                }
                boolean tue = false;
                for (File xmlReport : xmlReports) {
                    if (xmlReport.getName().endsWith(".xml")) {
                        final Document document = builder.parse(xmlReport);
                        racine = document.getDocumentElement();
                        if (Integer.valueOf(racine.getAttribute("failures")) >= 1) {
                            tue = true;
                        }
                    }
                }
                mutants.put(mutantProject.getName(), tue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mutants;
    }

    /**
     * Remplit les données pour l'affichage d'un diagramme dans le rapport
     * @param mutants: Map contenant les informations sur les mutants (nom, tué/non tué)
     */
    private void remplirDataGraph (Map<String, Boolean> mutants) {
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

    /**
     * Calcul du pourcentage de mutants tués et non tués
     * @param mutants Map contenant les informations sur les mutants (nom, tué/non tué)
     * @return Map : (mutants tués (true), pourcentage de mutants tués) et (mutants non tués (false), pourcentage de mutants non tués)
     */
    private Map calculerPourcentage(Map<String, Boolean> mutants){
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


    /**
     * Remplit la page HTML de rapport
     * @param mutants Map contenant les informations sur les mutants (nom, tué/non tué)
     */
    private void genererOutPutHtml(Map<String, Boolean> mutants) {
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
                } else {
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
