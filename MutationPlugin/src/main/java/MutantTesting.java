import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.surefire.SurefirePlugin;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Mojo(name = "testing")
public class MutantTesting extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File mutantRootPath = new File("target/spooned/");
        File compiledClassDir = new File("target/test-classes");

        File[] files = mutantRootPath.listFiles();
        if (files == null) {
            System.out.println("No mutants to test");
            return;
        }
        for (File file : files) {
            File spoonTestClassDir = new File(file.getPath().concat("/target/"));
            try {
                FileUtils.copyDirectoryToDirectory(compiledClassDir, spoonTestClassDir);
                FileUtils.copyFileToDirectory(new File("pom.xml"), file);

                Runtime.getRuntime().exec("cmd.exe /c mvn surefire:test", null, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
