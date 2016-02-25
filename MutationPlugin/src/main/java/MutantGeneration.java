import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import processors.ReturnProcessor;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.processing.Processor;

import java.io.File;
import java.io.IOException;

@Mojo(name = "generate")
public class MutantGeneration extends AbstractMojo{



    public File createMutantProject(Processor proc) {
        String mutantName = proc.getClass().getSimpleName();
        File mutantRootPath = new File("target/spooned/");
        String mutantProjectPath = "target/spooned/" + mutantName + "/";

        Runtime rt = Runtime.getRuntime();

        //File mutantProjectDir = new File(mutantProjectPath);

        boolean mkdirs = mutantRootPath.mkdirs();

        if (!mkdirs) {
            return null;
        }

        String archetypeCommand = "cmd.exe /c" +
                " mvn archetype:generate -B" +
                " -DarchetypeGroupId=org.apache.maven.archetypes" +
                " -DarchetypeArtifactId=maven-archetype-quickstart" +
                " -DgroupId=" + mutantName +
                " -DartifactId=" + mutantName +
                " -Dversion=1" +
                " -Dpackage=sample";

        try {
            Process exec = rt.exec(archetypeCommand, null, mutantRootPath);
            exec.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return new File(mutantProjectPath);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Launcher api = new Launcher();
        Processor proc = new ReturnProcessor();
        //Processor proc = new ReturnIntProcessor();


        File mutantProject = createMutantProject(proc);
        File mutantClasses = new File(mutantProject.getPath().concat("/target/classes"));
        File mutantSources = new File(mutantProject.getPath().concat("/target/src"));

        api.addProcessor(proc);
        api.addInputResource("./src/main/java/");


        api.setSourceOutputDirectory(mutantSources.getPath());
        api.setBinaryOutputDirectory(mutantClasses.getPath());

        api.run();

        SpoonModelBuilder builder = api.getModelBuilder();
        boolean compiled = builder.compile();
    }
}
