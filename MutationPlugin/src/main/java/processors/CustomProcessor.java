package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtElement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public abstract class CustomProcessor<E extends CtElement> extends AbstractProcessor<E> {

    private String mutationID;

    public CustomProcessor(String mutationID) {
        this.mutationID = mutationID;
    }

    /**
     * Génére un xml contenant la mutation appliquée
     * @param target
     * @param compiled
     */
    public void generateXml(File target, boolean compiled) {
        String mutationIdentifier = this.getClass() + " => " + getMutationID();

        String adressedufichier = target.getPath().concat("/coupleOutput.xml");
        try
        {
            FileWriter fw = new FileWriter(adressedufichier, false);
            BufferedWriter output = new BufferedWriter(fw);

            output.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            output.write("<info>\n");
            output.write("\t<mutation>"+mutationIdentifier+"</mutation>\n");
            output.write("\t<compiled>"+compiled+"</compiled>\n");
            output.write("</info>\n");

            output.flush();
            output.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected final String getMutationID() {
        return mutationID;
    }
}
