package htmlgenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.regex.*;


/**
 * Created by Lucas on 12/02/2016.
 */
public class TestDom4J {

    static public Map lireXML() {
    	Map<String, Boolean> mutants = new HashMap<String, Boolean>(); //Couples mutant (string) et bool (true:killed et false:non killed)
        
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Element racine = null;

        try {
            //Création du parseur
            final DocumentBuilder builder = factory.newDocumentBuilder();

            String[] dir = new java.io.File("./output/ResultatsXml").list( );
     
            for (int i=0; i<dir.length; i++)
            {
                String[] mutantRep = new java.io.File("./output/ResultatsXml/"+dir[i]).list( );
                boolean tue=true;
                for (int j=0; j<mutantRep.length; j++)
                { //On parcourt les résultats des tests, classe par classe
                    final Document document = builder.parse(new File("./output/ResultatsXml/" + dir[i]+"/"+mutantRep[j]));
                    racine = document.getDocumentElement();
                    if( !racine.getAttribute("failures").equals(racine.getAttribute("tests")))//si un test n'a pas fail
                    	tue=false;
                }
                mutants.put(dir[i], tue);
    	    }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mutants;
    }

     static public void remplirDataGraph (Map<String, Boolean> mutants) {
        String adressedufichier = "./Resultat-HTML/js/morris-data.js";
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
			String nouvelleString="value: " + pourcentages.get(true) +", label: 'Mutants tués'";
			String l2= ligne.replaceAll("value: (\\d*), label: 'Mutants tués'", nouvelleString);
			String nouvelleString2="value: " + pourcentages.get(false) +", label: 'Mutants non tués'";
			String l3= l2.replaceAll("value: (\\d*), label: 'Mutants non tués'", nouvelleString2);
			
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
		Map<Boolean, Double> res=new HashMap(); //mutant non tués (false), pourcentage...
		int nbTot=mutants.size();//2
		int nbTues=0;
		for(Boolean b: mutants.values()){
			if(b) nbTues++;
		}
    	res.put(true, ((double)nbTues/(double)nbTot)*100);
    	res.put(false, ((double)(nbTot-nbTues)/(double)nbTot)*100);
    	return res;
     }
     
     
     static public void genererOutPutHtml(Map<String, Boolean> mutants) {
        for(String s: mutants.keySet()){
        	//System.out.println("Clé:"+ s + "- tué:"+ mutants.get(s));
        }
        String adressedufichier = "./Resultat-HTML/index.html";
        
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
     
     public static void main(final String[] args) {

         Map<String, String> map = new HashMap<String, String>();
         ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

         /*map = lireXML("test.xml");
         list.add(map);
         map = lireXML("test.xml");
         list.add(map);

         remplirDataGraph("output.html", list);
         genererOutPutHtml("output.html", list);*/
         
         Map<String, Boolean> res=lireXML();
         remplirDataGraph(res);
         genererOutPutHtml(res);
  
     }
     
}
