import generator.MutantGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import processors.*;
import spoon.processing.Processor;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@Mojo(name = "generate")
public class MutantGeneration extends AbstractMojo{


    @Parameter (property = "generate.binaryOperator", defaultValue = "4")
    private int binaryOperator;

    @Parameter (property = "generate.logicOperator", defaultValue = "2")
    private int logicOperator;

    @Parameter (property = "generate.modifier", defaultValue = "4")
    private int modifier;

    @Parameter (property = "generate.operator", defaultValue = "4")
    private int operator;

    @Parameter (property = "generate.myReturn", defaultValue = "true")
    private boolean myReturn;

    public Set<CustomProcessor> retrieveProcessors() {
        Set<CustomProcessor> processors = new HashSet<CustomProcessor>();
        processors.addAll(LogicOperatorProcessor.getSomeCouples(logicOperator));
        processors.addAll(BinaryOperatorProcessor.getSomeCouples(binaryOperator));
        processors.addAll(OperatorProcessor.getSomeCouples(operator));
        processors.addAll(ModifierProcessor.getSomeCouples(modifier));
        if (myReturn) processors.add(new ReturnProcessor());
        return processors;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // Processors that are to be used by spoon
        Set<CustomProcessor> processorSet = retrieveProcessors();
        File mutantsRootPath = new File("target/spooned/");

        for (CustomProcessor proc : processorSet) {
            MutantGenerator generator = new MutantGenerator(this.getLog(), proc, mutantsRootPath);

            try {
                generator.applyProcessor();
                generator.compileMutants();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
