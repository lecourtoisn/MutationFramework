#Sp�cifications

## 1- Description de la chaine de build

![](https://raw.githubusercontent.com/lecourtoisn/MutationFramework/master/build.png)

A partir d'un programme donn�, nous allons lancer la chaine de compilation via Maven:
  * Pendant la phase � test �, Maven va ex�cuter un bash qui va aller chercher un couple (mutant, s�lecteur) et l'appliquer � ce programme � l'aide de Spoon. Ainsi, � l'endroit indiqu� par le s�lecteur, la mutation va s'appliquer et un programme mutant sera g�n�r�. Par la m�me occasion, on cr�era un xml permettant d'identifier ce mutant pour le rapport final.
Les mutants et les s�lecteurs seront �crits au pr�alable avec Spoon.
  * Ensuite, via une commande Maven, le bash ex�cutera les tests unitaires JUnit sur le mutant. Les r�sultats de ces tests seront �crits dans un fichier xml par JUnit. Les mutants ne compilant pas seront abandonn�s, il ne suivront pas la suite de la cha�ne de build.
  * On reprendra ces deux premi�res �tapes jusqu'� ce que qu'on ait un nombre de mutants nous permettant de valider le programme d'origine. Ainsi, on aura une liste de fichiers xml avec les r�sultats pour chacun des mutants.
  * A partir de cette liste et des fichiers xml g�n�r�s lors de la cr�ation des mutants, nous allons cr�er une page HTML r�sumant l'ensemble des mutants avec le pourcentage de tests n'�tant pas pass�s.
On pourra voir les tests qui sont pass�s malgr� les mutations et les mutations appliqu�es � chaque mutant.
Cette page HTML sera g�n�r�e avec Java et utilisera bootstrap pour la mise en page.</p>


## 2- Mutations

Nous avons �tabli une liste contenant un grand nombre de mutations sur diff�rents types d�op�rateurs, valeurs ou mots clefs en pr�cisant l�endroit o� elles s�appliquent (s�lecteurs).

#### Mutation sur les op�rateurs de calcul :

  * +, -, *, /, %, pow(), sqrt()

#### Mutation sur les op�rateurs de comparaison :

  * >,<, >=, <=, ==
  * .equals() , ==

#### Mutation sur les op�rateurs logiques :

  * &&, ||
  * A && B <-> B && A (de m�me pour ||)

#### Mutation sur les bool�ens :

  * true, false

#### Mutation sur les valeurs :

  * chiffres/nombres, 	random()
  * modifier la valeur des strings
  * modifier la valeur des constantes
  * modifier le tableau args du main 

#### Mutation sur les valeurs d�incr�mentation d�une boucle :

  * i++, i+=random()
  * post-incr�mentation , pr�-incr�mentation

#### Mutation sur les mots clefs :

  * public, private, protected 		
  * supprimer this	
  * supprimer super
  * ajouter break
  * ajouter static

#### Mutation sur des lignes de code :

  * supprimer la ligne
  * dupliquer une ligne	
  * permuter des lignes

#### Mutation sur des fonctions :

  * Changer l�ordre des arguments
  * Changer le nombre d�arguments
  * Changer le noms des fonctions ayant l�annotation �@override�
  * Supprimer les fonctions ayant l�annotation �@override�

#### Mutation sur les getters et setters :

  * Les modifier/supprimer

#### Mutation sur les types :

  * Supprimer un cast
  * Inverser classe m�res/filles

#### Mutation sur les conditions :

  * inverser l�ordre des bloques if, else if
  
  
Nous avons d�cid� de n�appliquer qu'une seule mutation par mutant, afin d'avoir un r�sultat plus pr�cis en bout de cha�ne. En effet, avec trop de mutations sur un programme, il sera plus difficile de cibler les portions de code qui ne sont pas correctement test�es. 
Il faudra cr�er suffisamment de mutants pour que le r�sultat soit pertinent mais il ne faudra pas non plus trop en cr�er pour avoir un temps d�ex�cution raisonnable. 
Un couple (mutation,s�lecteur) � appliquer sera s�lectionn�e de fa�on al�atoire dans la liste des mutations d�crite ci-dessus. Si ce couple peut �tre appliqu� � plusieurs emplacements du programme, l�un d�entre eux sera s�lectionn� al�atoirement.
Ce dispositif nous permet donc de tester un grand nombre de mutations dans des conditions diff�rentes de fa�on automatique.



