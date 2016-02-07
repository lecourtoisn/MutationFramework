# Spécifications

## 1- Description de la chaine de build

![](https://raw.githubusercontent.com/lecourtoisn/MutationFramework/master/build.png)

A partir d'un programme donné, nous allons lancer la chaine de compilation via Maven:
  * Pendant la phase « test », Maven va exécuter un bash qui va aller chercher un couple (mutant, sélecteur) et l'appliquer à ce programme à l'aide de Spoon. Ainsi, à l'endroit indiqué par le sélecteur, la mutation va s'appliquer et un programme mutant sera généré. Par la même occasion, on créera un xml permettant d'identifier ce mutant pour le rapport final.
Les mutants et les sélecteurs seront écrits au préalable avec Spoon.
  * Ensuite, via une commande Maven, le bash exécutera les tests unitaires JUnit sur le mutant. Les résultats de ces tests seront écrits dans un fichier xml par JUnit. Les mutants ne compilant pas seront abandonnés, il ne suivront pas la suite de la chaîne de build.
  * On reprendra ces deux premières étapes jusqu'à ce que qu'on ait un nombre de mutants nous permettant de valider le programme d'origine. Ainsi, on aura une liste de fichiers xml avec les résultats pour chacun des mutants.
  * A partir de cette liste et des fichiers xml générés lors de la création des mutants, nous allons créer une page HTML résumant l'ensemble des mutants avec le pourcentage de tests n'étant pas passés.
On pourra voir les tests qui sont passés malgré les mutations et les mutations appliquées à chaque mutant.
Cette page HTML sera générée avec Java et utilisera bootstrap pour la mise en page.</p>


## 2- Mutations

Nous avons établi une liste contenant un grand nombre de mutations sur différents types d’opérateurs, valeurs ou mots clefs en précisant l’endroit où elles s’appliquent (sélecteurs).

#### Mutation sur les opérateurs de calcul :

  * +, -, *, /, %, pow(), sqrt()

#### Mutation sur les opérateurs de comparaison :

  * >,<, >=, <=, ==
  * .equals() , ==

#### Mutation sur les opérateurs logiques :

  * &&, ||
  * A && B <-> B && A (de même pour ||)

#### Mutation sur les booléens :

  * true, false

#### Mutation sur les valeurs :

  * Chiffres/nombres, 	random()
  * modifier la valeur des strings
  * Modifier la valeur des constantes
  * Modifier le tableau args du main 

#### Mutation sur les valeurs d’incrémentation d’une boucle :

  * i++, i+=random()
  * Post-incrémentation , pré-incrémentation

#### Mutation sur les mots clefs :

  * Public, private, protected 		
  * Supprimer this	
  * Supprimer super
  * Ajouter break
  * Ajouter static

#### Mutation sur des lignes de code :

  * Supprimer la ligne
  * Dupliquer une ligne	
  * Permuter des lignes

#### Mutation sur des fonctions :

  * Changer l’ordre des arguments
  * Changer le nombre d’arguments
  * Changer le noms des fonctions ayant l’annotation “@override”
  * Supprimer les fonctions ayant l’annotation “@override”

#### Mutation sur les getters et setters :

  * Les modifier/supprimer

#### Mutation sur les types :

  * Supprimer un cast
  * Inverser classe mères/filles

#### Mutation sur les conditions :

  * Inverser l’ordre des bloques if, else if
    
Nous avons décidé de n’appliquer qu'une seule mutation par mutant, afin d'avoir un résultat plus précis en bout de chaîne. En effet, avec trop de mutations sur un programme, il sera plus difficile de cibler les portions de code qui ne sont pas correctement testées. 
Il faudra créer suffisamment de mutants pour que le résultat soit pertinent mais il ne faudra pas non plus trop en créer pour avoir un temps d’exécution raisonnable. 
Un couple (mutation,sélecteur) à appliquer sera sélectionnée de façon aléatoire dans la liste des mutations décrite ci-dessus. Si ce couple peut être appliqué à plusieurs emplacements du programme, l’un d’entre eux sera sélectionné aléatoirement.
Ce dispositif nous permet donc de tester un grand nombre de mutations dans des conditions différentes de façon automatique.



