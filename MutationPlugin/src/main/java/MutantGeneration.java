import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import processors.ReturnProcessor;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.processing.Processor;

@Mojo(name="generate")
public class MutantGeneration extends AbstractMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Launcher api = new Launcher();
        Processor proc = new ReturnProcessor();
        api.addProcessor(proc);
        api.addInputResource("./src/main/java/");
        String outputSrc = "./spooned/src/" + proc.getClass().getName() + "/";
        String outputBin = "./spooned/classes/" + proc.getClass().getName() + "/";


        api.setSourceOutputDirectory(outputSrc);
        api.setBinaryOutputDirectory(outputBin);

        api.run();

        SpoonModelBuilder builder = api.getModelBuilder();
        boolean compiled = builder.compile();
    }
}
