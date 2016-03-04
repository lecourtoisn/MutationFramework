import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import processors.*;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.processing.Processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "generate")
public class MutantGeneration extends AbstractMojo{


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        List<Processor> processorList = new ArrayList<Processor>();
        processorList.add(new ReturnProcessor());
        processorList.addAll(BinaryOperatorProcessor.getSomeCouples(5));
        processorList.addAll(OperatorProcessor.getSomeCouples(5));
        processorList.addAll(LogicOperatorProcessor.getEveryCouples());

        int i =0;

        for (Processor proc : processorList) {
            Launcher api = new Launcher();

            String mutantName = proc.getClass().getSimpleName();
            File mutantsRootPath = new File("target/spooned/");

            File mutantProject = new File(mutantsRootPath.getPath().concat("/" + mutantName + i));

            File mutantClasses = new File(mutantProject.getPath().concat("/target/classes"));
            File mutantSources = new File(mutantProject.getPath().concat("/src/main/java"));

            // The processor mutating the project is specified
            api.addProcessor(proc);
            // The source code to alter is specified too
            api.addInputResource("./src/main/java/");

            // The directories in which the sources will be generated and compiled are set
            api.setSourceOutputDirectory(mutantSources.getPath());
            api.setBinaryOutputDirectory(mutantClasses.getPath());

            // Spoon runs and generate the new sources ...
            api.run();

            SpoonModelBuilder builder = api.getModelBuilder();
            // ... and compile them
            boolean compiled = builder.compile();

            i++;
        }
    }
}
