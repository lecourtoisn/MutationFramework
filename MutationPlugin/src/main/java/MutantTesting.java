import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.io.IOException;

@Mojo(name = "testing")
public class MutantTesting extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File mutantRootPath = new File("target/spooned/");
        File compiledClassDir = new File("target/test-classes");

        File[] mutantsDirectories = mutantRootPath.listFiles();
        if (mutantsDirectories == null) {
            System.out.println("No mutants to test");
            return;
        }
        for (File mutantDir : mutantsDirectories) {
            File spoonTestClassDir = new File(mutantDir.getPath().concat("/target/"));
            try {
                FileUtils.copyDirectoryToDirectory(compiledClassDir, spoonTestClassDir);
                FileUtils.copyFileToDirectory(new File("pom.xml"), mutantDir);

                Runtime.getRuntime().exec("cmd.exe /C mvn surefire:test", null, mutantDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.getLog().info("The mutants are being tested. This operation can take several minutes");
        checkSfOver(mutantsDirectories);
        this.getLog().info("Testing over");

    }

    private void checkSfOver(File[] mutantsDirectories) {
        int nbOver = 0;
        do {
            int ct = countOver(mutantsDirectories);
            if (ct != nbOver) {
                nbOver = ct;
                this.getLog().info(nbOver + " mutant(s) out of " + mutantsDirectories.length + " have been tested");
            }
        } while (nbOver < mutantsDirectories.length);
    }

    private int countOver(File[] mutantsDirectories) {
        int ct = 0;
        for (File mutantDir : mutantsDirectories) {
            if (new File(mutantDir.getPath().concat("/target/surefire-reports")).exists()) {
                ++ct;
            }
        }
        return ct;
    }
}
