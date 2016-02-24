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
        String mutantProjectPath = "target/spooned/" + proc.getClass().getSimpleName() + "/";
        System.out.println(mutantProjectPath);

        Runtime rt = Runtime.getRuntime();
        File mutantProjectDir = new File(mutantProjectPath);
        System.out.println(mutantProjectDir.getAbsolutePath());
        System.out.println(mutantProjectDir);
        boolean mkdirs = mutantProjectDir.mkdirs();


        System.out.println(mkdirs + " => that the directories have been created");
        System.out.println(mutantProjectDir.exists() + " => that the directories have been created");

        try {
            //rt.exec("cmd.exe /c mvn archetype:generate -B -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-simple -DgroupId=truc -DartifactId=bidule -Dversion=1.0 -Dpackage=tructoo", null, mutantProjectDir);
            rt.exec("cmd.exe /c mvn archetype:generate -B -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-simple -DgroupId=truc -DartifactId=bidule -Dversion=1.0 -Dpackage=tructoo, -DbaseDir=" + mutantProjectPath);
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*MavenCli cli = new MavenCli();
        int result = cli.doMain(new String[]{"compile"},
                "/home/aioffe/workspace/MiscMaven",
                System.out, System.out);
        System.out.println("result: " + result);*/

        return new File(mutantProjectPath);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Launcher api = new Launcher();
        Processor proc = new ReturnProcessor();


        File mutantProject = createMutantProject(proc);
        if (1 == 1) return;

        api.addProcessor(proc);
        api.addInputResource("./src/main/java/");
        String outputSrc = "./target/spooned/src/" + proc.getClass().getName() + "/";
        String outputBin = "./target/spooned/classes/" + proc.getClass().getName() + "/";


        api.setSourceOutputDirectory(outputSrc);
        api.setBinaryOutputDirectory(outputBin);

        api.run();

        SpoonModelBuilder builder = api.getModelBuilder();
        boolean compiled = builder.compile();
    }
}
