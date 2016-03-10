# Rapport du projet

Notre framework de tests par mutations permet de valider des tests. En créant des mutants d'un programme donné et en leur appliquant ces tests, on regarde le nombre de mutants tués par les tests, quels sont les tests qui ont tué le plus de mutant… Ces informations sont mises à disposition à la fin de la chaîne dans un rapport HTML. On voit alors quels tests sont les plus fiables (ceux qui tuent le plus de mutants) et ceux qui le sont le moins. 

Si au moins un test n'est pas passé sur le mutant, on dit qu'il l'a tué. Dans le rapport HTML, s’il y a un test qui a tué le mutant on dit que la classe qui le contient a tué le mutant. On a ainsi un diagramme en barre indiquant pour chaque classe de tests, le nombre de mutant tué.
Lors de la génération d'un mutant, certaines modifications sont trop incohérentes et le programme ne compile pas, c'est un mutant mort né. On peut voir dans le rapport un diagramme circulaire, donnant le pourcentage de mutants tués, non tués et morts nés. Ce diagramme donne une vue d’ensemble sur l’état des mutants, mais pour en savoir plus, il faut lire le reste du fichier.

## Architecture
Les différents outils utilisés sont Spoon pour générer les mutants, Surefire pour lancer les tests unitaires JUnit sur les mutants, Bootstrap pour la mise en page du rapport HTML et Maven pour assembler ces éléments et automatiser la chaîne de build.
Différentes bibliothèques sont utilisées pour simplifier certaines tâches comme zt-zip pour dézipper le template HTML utilisé pour pour le rapport de test par mutation, et maven-invoker pour appeler Surefire depuis du code java.

Notre framework est composé de trois mojos : generate, testing et report pour respectivement générer les mutants, les tester et générer le rapport.

##### Le mojo “generate”
Récupère grâce à ses paramètres la liste des mutations à appliquer au code source, et utilise l’api de Spoon pour générer les mutants puis les compiler. Un rapport de génération est crée et indique si le mutant passe la compilation ou non (mutant mort-né).
Nous avons 5 types de processeurs mutations différents :
  * BinaryOperatorProcessor qui effectue des mutations sur les opérateurs de comparaison
  * LogicOperatorProcessor qui effectue des mutations sur les opérateurs logiques
  * ModifierProcessor qui effectue des mutations sur des mots clefs (public, private, protected)
  * OperatorProcessor qui effectue des mutations sur les opérateurs de calcul
  * ReturnProcessor qui effectue une mutations sur les retours de type String

##### Le mojo “testing”
Parcourt les projets générés précédemment et exécute via l’api de maven-invoker le plugin Surefire qui s’occupe de lancer les tests JUnit et de générer les rapports.

##### Le mojo “report”
Crée le rapport du test par mutation. Pour récupérer les informations sur les mutants, nous parsons le rapport de génération, un fichier XML, pour chaque mutant (coupleOutput.xml). Il contient une première balise avec la modification effectuée et une autre contenant un booléen disant si le programme compile. S'il compile, nous lisons alors les rapports de JUnit, sur les tests appliqués à ce mutant. Il y en a un par classe de test et ils nous permettent de savoir le nombre de tests ne passant pas sur le mutant, c’est-à-dire de remplir le diagramme circulaire (en complément avec coupleOutput.xml pour les morts nés).

## Forces et faiblesses
#### Forces :
  * Possibilité de mettre les opérations dans différentes phases (modulaire)
  * Rapport permet d’avoir une bonne vue d’ensemble sur le projet
  * Facile à utiliser
  * On peut choisir les types de mutations et leur nombre
  
#### Faiblesses : 
  * Petite base de mutations
  * On ne montre pas exactement où la mutation a été effectuée
  * On ne peut pas choisir les sélecteurs
  * Ne fonctionne pas si le projet cible contient des dépendances que notre plugin n’a pas.
