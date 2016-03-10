#Comment utiliser le plug-in

Notre framework de test par mutation est composé des goals suivants :
generate: génère les mutants. Il est conseillé de l’associer à la phase generate-sources.
testing: lance les tests du projet sur les mutants générés. Il est conseillé de l’associer à la phase test.
report: génère un rapport de la chaîne au format HTML. Il est conseillé de l’associer à la phase site.
En plus du framework, nous proposons aussi un exemple d'utilisation, "SampleSourceCode".

/!\ Attention : Le goal “testing” a besoin des .class des tests à exécuter. La phase associée à “testing” doit donc être postérieure à la phase “test-compile” dans le cycle de vie de construction de maven.
On peut modifier pom.xml pour plus de modularité sur les phases, comme indiqué sur la phase de tests par exemple.
Quelque soit la méthode utilisée pour lancer les goals generate, testing et report, il doivent toujours être exécutés dans cet ordre et en respectant la contrainte ci-dessus.

  * Après avoir construit le framework (cf build.md), il faut ajouter le plug-in dans le projet maven à tester. Ouvrez le fichier pom.xml et ajotuez dans la balise plugins:

 ```xml
<plugin>
  <groupId>mutationframework</groupId>
  <artifactId>mutation-maven-plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
  <executions>
    <execution>
        <id>Generating mutants</id>
        <phase>generate-sources</phase>
        <goals>
            <goal>generate</goal>
        </goals>
        <configuration>
            <binaryOperator>4</binaryOperator>
            <logicOperator>2</logicOperator>
            <modifier>4</modifier>
            <operator>4</operator>
            <myReturn>true</myReturn>
        </configuration>
    </execution>
    <execution>
        <id>Testing mutants</id>
        <phase>test</phase>
        <goals>
            <goal>testing</goal>
        </goals>
    </execution>
    <execution>
        <id>Generating report</id>
        <phase>site</phase>
        <goals>
            <goal>report</goal>
        </goals>
    </execution>
  </executions>
</plugin>
```

Nous pouvons remarquer que le premier goal “generate” peut prendre en paramètre le nombre de mutants que nous voulons générer pour chaque processeur. Nous pouvons par la même occasion désactiver un processeur en donnant un nombre de 0 mutations. Les processeurs n’offrant qu’une seule mutation peuvent être activés ou déactivés avec un booléen. Les valeurs ci-dessus sont les valeurs par défaut.
Il se peut que votre IDE considère les paramètres du plugin comme des erreurs. Si le framework a bien été installé dans le repository local, ces “erreurs” disparaîtront après la première exécution de maven.

Dans la suite de ce tutoriel, nous considérerons que les phases associées aux goals du plugin sont celles de l’exemple ci-dessus. Si vous en avez utilisé d’autres, substituez les dans les commandes qui suivent.


  * Commencez par clean (depuis votre IDE ou avec la commande “mvn clean”), ceci supprime le dossier target du projet. On efface donc tout ce qui a déjà été fait par la chaîne de build.
  * Générez les mutants et lancez la phase test (depuis votre IDE ou avec la commande “mvn test”). La phase liée à “generate” devant être antérieure à “test-compile”, elle sera exécutée avant “test”, et les mutants seront générés.
On voit alors que le répertoire target du projet se remplie, notamment avec le dossier spooned, qui contient un répertoire pour chaque mutant. Ces répertoires contiennent les résultats des tests effectués par JUnit (les fichiers xml dans target\surefire-reports) et des informations sur les mutants (telles que la mutation effectuée et s’il compile, dans coupleOutput.xml) 
  * Enfin, lancez la génération du rapport (depuis votre IDE en lançant la phase “site” ou
avec la commande “mvn site”). Dans le terminal s’affiche alors une série de messages, dont l’un contenant le lien vers le rapport généré, sous la forme: “Result
of the mutation testing : ...\target\Resultat-HTML\index.html”. Copiez le lien et ouvrez-le dans un navigateur pour voir le résultat. 
Ce lien correspond à un répertoire qui a été copié du plug-in et complété en fonction des résultats: target\Resultat-HTML.

Ces trois étapes peuvent être réalisées en une ligne de commande : mvn clean test site




