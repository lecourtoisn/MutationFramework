import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import processors.*;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.reflect.declaration.ModifierKind;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "generate")
public class MutantGeneration extends AbstractMojo{


    @Parameter
    private int binaryOperator;

    @Parameter
    private int logicOperator;

    @Parameter
    private int modifier;

    @Parameter
    private int operator;

    @Parameter
    private boolean myReturn;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        //Liste des diff�rentes mutations
        List<CustomProcessor> processorList = new ArrayList<CustomProcessor>();
        //processorList.add(new RemoveConstructorProcessor());
        processorList.addAll(LogicOperatorProcessor.getSomeCouples(logicOperator));
        processorList.addAll(BinaryOperatorProcessor.getSomeCouples(binaryOperator));
        processorList.addAll(OperatorProcessor.getSomeCouples(operator));
        processorList.addAll(ModifierProcessor.getSomeCouples(modifier));
        if (myReturn) processorList.add(new ReturnProcessor());

        int i = 0;

        for (CustomProcessor proc : processorList) {
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

            // Spoon runs and generate the new sources
            api.run();

            SpoonModelBuilder builder = api.getModelBuilder();

            // ... and compile them


            boolean compiled = false;
            try {
                compiled = builder.compile();
            } catch (Exception e) {
                this.getLog().error("Uncompiled mutant : " + mutantName);
                e.printStackTrace();
            }

            proc.generateXml(mutantProject, compiled);

            i++;
        }
    }
}
