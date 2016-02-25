import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import processors.GreaterEqualsProcessor;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.processing.Processor;

import java.io.File;

@Mojo(name = "generate")
public class MutantGeneration extends AbstractMojo{


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Launcher api = new Launcher();

//        Processor proc = new ReturnProcessor();
        Processor proc = new GreaterEqualsProcessor();

        String mutantName = proc.getClass().getSimpleName();
        File mutantsRootPath = new File("target/spooned/");

        File mutantProject = new File(mutantsRootPath.getPath().concat("/" + mutantName));

        File mutantClasses = new File(mutantProject.getPath().concat("/target/classes"));
        File mutantSources = new File(mutantProject.getPath().concat("/src/main/java"));

        api.addProcessor(proc);
        api.addInputResource("./src/main/java/");

        api.setSourceOutputDirectory(mutantSources.getPath());
        api.setBinaryOutputDirectory(mutantClasses.getPath());

        api.run();

        SpoonModelBuilder builder = api.getModelBuilder();
        boolean compiled = builder.compile();
    }
}
