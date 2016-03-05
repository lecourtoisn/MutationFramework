import edu.emory.mathcs.backport.java.util.Collections;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.io.IOException;

@Mojo(name = "testing")
public class MutantTesting extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File mutantRootPath = new File("target/spooned/");
        File compiledClassDir = new File("target/test-classes");
        Invoker invoker = new DefaultInvoker();
        InvocationRequest request = new DefaultInvocationRequest();
        request.setGoals(Collections.singletonList("surefire:test"));


        File[] mutantsDirectories = mutantRootPath.listFiles();
        if (mutantsDirectories == null) {
            System.out.println("No mutants to test");
            return;
        }
        this.getLog().info("The mutants are being tested. This operation can take several minutes");
        for (File mutantDir : mutantsDirectories) {
            File spoonTestClassDir = new File(mutantDir.getPath().concat("/target/"));
            try {
                FileUtils.copyDirectoryToDirectory(compiledClassDir, spoonTestClassDir);
                FileUtils.copyFileToDirectory(new File("pom.xml"), mutantDir);

                request.setPomFile(new File(mutantDir.getPath().concat("/pom.xml")));
                InvocationResult execute = invoker.execute(request);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MavenInvocationException e) {
                this.getLog().error("Couldn't run surefire for test : " + mutantDir.getName());
                e.printStackTrace();
            }
        }
        //checkSfOver(mutantsDirectories);
        this.getLog().info("Testing over");

    }
}
