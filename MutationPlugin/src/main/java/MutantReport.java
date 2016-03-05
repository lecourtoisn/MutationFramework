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
    private Map<String, Integer> testsTueurs; //nombre de mutant tué pour chaque classe de tests
    private Map<String, String> mutations; //Noms et mutation pour chaque mutant
    private int nbMortNes;

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
        testsTueurs= new HashMap<String, Integer>();
        mutations= new HashMap<String, String>();

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
            for (File mutantProject : mutantProjects) {//Pour chaque mutant
                //On lit les information (descripitons et si mort né) sur le mutant
                final Document infoDoc = builder.parse(mutantProject.getPath().concat("/coupleOutput.xml"));
                mutations.put(mutantProject.getName(),infoDoc.getDocumentElement().getChildNodes().item(1).getTextContent());
                if(infoDoc.getDocumentElement().getChildNodes().item(3).getTextContent().contains("false")){
                    nbMortNes++;
                }else {
                    //S'il n'est pas mort né, on regarde s'il a été tué par les tests
                    File xmlReportsDir = new File(mutantProject.getPath().concat("/target/surefire-reports"));
                    File[] xmlReports = xmlReportsDir.listFiles();
                    if (xmlReports == null) {
                        throw new Exception("Pas de rapport à parser pour le mutant " + mutantProject.getName());
                    }
                    boolean tue = false;
                    for (File xmlReport : xmlReports) {
                        if (xmlReport.getName().endsWith(".xml")) {
                            final Document document = builder.parse(xmlReport);
                            racine = document.getDocumentElement();
                            if (Integer.valueOf(racine.getAttribute("failures")) >= 1) {
                                tue = true;
                                String nom = xmlReport.getName().substring(5).replace(".xml", "");
                                Integer temp = testsTueurs.get(nom);
                                if (temp != null) temp++;
                                else temp = 1;
                                testsTueurs.put(nom, temp);
                            }
                        }
                    }
                    mutants.put(mutantProject.getName(), tue);
                }
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
        Map<Integer, Double> pourcentages= calculerPourcentage(mutants);

        try
        {
            FileReader lireFichier= new FileReader(adressedufichier);
            String ligne = "";
            int i = 0;
            // Boucle permettant de parcourir tout le fichier
            while((i = lireFichier.read()) != -1){
                ligne += (char)i;
            }

            //Changements pour le 1er diagramme
            String nouvelleString="value: " + pourcentages.get(0) +", label: 'Mutants tues'";
            String l2= ligne.replaceAll("value: (\\d*).(\\d*), label: 'Mutants tues'", nouvelleString);
            String nouvelleString2="value: " + pourcentages.get(1) +", label: 'Mutants non tues'";
            String l3= l2.replaceAll("value: (\\d*).(\\d*), label: 'Mutants non tues'", nouvelleString2);
            String nouvelleString3="value: " + pourcentages.get(2) +", label: 'Mutants morts nes'";
            String l4= l3.replaceAll("value: (\\d*).(\\d*), label: 'Mutants morts nes'", nouvelleString3);
            //Changements pour le 2eme diagramme
            String dataTestTueurs="";
            for (String classTest: testsTueurs.keySet()){
                dataTestTueurs+= "{ nom: '"+ classTest + "', nb: " + testsTueurs.get(classTest)+"},";
            }
            l4= l4.replace("//debut datas", dataTestTueurs);

            //Ecriture des résultats
            FileWriter ecrireFichier = new FileWriter(adressedufichier, false);
            ecrireFichier.write(l4);
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
     * @return Map : (mutants tués (0), pourcentage de mutants tués) et (mutants non tués (1), pourcentage) et (mutants morts nés (2), pourcentage de mutants non tués)
     */
    private Map calculerPourcentage(Map<String, Boolean> mutants){
        Map<Integer, Double> res=new HashMap(); //mutant tués (0), pourcentage
        int nbTot=mutants.size()+nbMortNes;
        int nbTues=0;
        for(Boolean b: mutants.values()){
            if(b) nbTues++;
        }
        DecimalFormat df = new DecimalFormat ( ) ;
        df.setMaximumFractionDigits(2) ; //arrondi à 2 chiffres apres la virgules
        df.setMinimumFractionDigits(2) ;
        res.put(0,  Double.parseDouble(df.format(((double)nbTues/(double)nbTot)*100)));
        res.put(1,  Double.parseDouble(df.format(((double)(nbTot-nbTues-nbMortNes)/(double)nbTot)*100)));
        res.put(2,  Double.parseDouble(df.format(((double)nbMortNes/(double)nbTot)*100)));
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
                    nouveauContenu+=("<p>Mutation: "+ nomMutant +" ("+ mutations.get(nomMutant).split("=>")[1] +" ) </p> </div> </div> </div>");
                } else {
                    nouveauContenu+="<div class=\"col-lg-8\"> <div class=\"panel panel-danger\"> <div class=\"panel-heading\">Mutant non tue </div><div class=\"panel-body\">";
                    nouveauContenu+=("<p>Mutation: "+ nomMutant +" ("+ mutations.get(nomMutant).split("=>")[1] +" ) </p> </div> </div> </div>");
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
