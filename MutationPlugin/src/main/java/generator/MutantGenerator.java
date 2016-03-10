package generator;

import org.apache.maven.plugin.logging.Log;
import processors.CustomProcessor;
import spoon.Launcher;
import spoon.SpoonModelBuilder;

import java.io.File;

public class MutantGenerator {

    private Launcher launcher;
    private Log logger;
    private CustomProcessor proc;
    private File mutantProject;

    private static int i = 0;

    public MutantGenerator(Log logger, CustomProcessor processor, File mutantsRootPath) {
        ++i;
        this.logger = logger;
        this.proc = processor;
        this.launcher = new Launcher();

        setMutantProject(mutantsRootPath);
    }

    /**
     * Apply the processor to the sources
     * @throws Exception
     */
    public void applyProcessor() throws Exception {

        File mutantClasses = new File(mutantProject.getPath().concat("/target/classes"));
        File mutantSources = new File(mutantProject.getPath().concat("/src/main/java"));

        // The proc mutating the project is specified
        launcher.addProcessor(proc);
        // The source code to alter is specified too
        launcher.addInputResource("./src/main/java/");

        // The directories in which the sources will be generated and compiled are set
        launcher.setSourceOutputDirectory(mutantSources.getPath());
        launcher.setBinaryOutputDirectory(mutantClasses.getPath());

        // Spoon runs and generate the new sources

        try {
            launcher.run();
        } catch (Exception e){
            logger.error("Couldn't generate mutants with processor : " + mutantDirectory());
            throw e;
        }
    }

    /**
     * Compile the mutants
     * @throws Exception
     */
    public void compileMutants() throws Exception {
        SpoonModelBuilder builder = launcher.getModelBuilder();

        boolean compiled = false;
        try {
            compiled = builder.compile();
        } catch (Exception e) {
            logger.error("Uncompiled mutant : " + mutantDirectory());
            throw e;
        }

        proc.generateXml(mutantProject, compiled);
    }

    public void setMutantProject(File mutantsRootPath) {
        mutantProject = new File(mutantsRootPath.getPath().concat("/" + mutantDirectory()));
    }

    private String mutantDirectory() {
        return proc.getClass().getSimpleName() + i;
    }
}
