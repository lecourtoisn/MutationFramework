package processors;

import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.declaration.ModifierKind;

import java.util.ArrayList;
import java.util.Random;

public class Utils {

    /**
     * Retourne une liste avec deux mutations choisi aléatoirement dans la liste
     * @param possibilityList
     * @return liste de type BinaryOperatorKind
     */
    public static ArrayList<BinaryOperatorKind> choseTwoBinaryOperator(ArrayList<BinaryOperatorKind> possibilityList ) {

        ArrayList<BinaryOperatorKind> listTwo = new ArrayList<BinaryOperatorKind>();

        Random rand = new Random();
        int randomNumber = rand.nextInt(possibilityList.size());
        int randomNumber2 = rand.nextInt(possibilityList.size());

        while(randomNumber == randomNumber2) {
            randomNumber2 = rand.nextInt(possibilityList.size());
        }

        listTwo.add(possibilityList.get(randomNumber));
        listTwo.add(possibilityList.get(randomNumber2));

        return listTwo;
    }

    /**
     * Retourne une liste avec deux mutations choisi aléatoirement dans la liste
     * @param possibilityList
     * @return liste de type ModifierKind
     */
    public static ArrayList<ModifierKind> choseTwoModifier(ArrayList<ModifierKind> possibilityList ) {

        ArrayList<ModifierKind> listTwo = new ArrayList<ModifierKind>();

        Random rand = new Random();
        int randomNumber = rand.nextInt(possibilityList.size());
        int randomNumber2 = rand.nextInt(possibilityList.size());

        while(randomNumber == randomNumber2) {
            randomNumber2 = rand.nextInt(possibilityList.size());
        }

        listTwo.add(possibilityList.get(randomNumber));
        listTwo.add(possibilityList.get(randomNumber2));

        return listTwo;
    }
}
