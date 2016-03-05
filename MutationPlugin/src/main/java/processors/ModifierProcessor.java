package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.declaration.ModifierKind;

import java.util.ArrayList;
import java.util.List;

public class ModifierProcessor extends AbstractProcessor<CtTypeAccess<ModifierKind>> {

    private ModifierKind b;
    private ModifierKind a;

    /**
     * Constructeur, prend en paramètre deux operateurs de mutations
     * @param a
     * @param b
     */
    public ModifierProcessor(ModifierKind a, ModifierKind b) {
        this.a = a;
        this.b = b;
    }

    //CtTypeAccess
    @Override
    public boolean isToBeProcessed(CtTypeAccess<ModifierKind> candidate) {
        return false;//candidate.getType() == listTwo.get(0); TODO
    }


    @Override
    public void process(CtTypeAccess<ModifierKind> element) {
        if (isToBeProcessed(element)) {
            //element.setKind(b); TODO
        }
    }


    /**
     * Liste de toutes les opérateurs possiblent
     * @return
     */
    private static ArrayList<ModifierKind> generateList () {

        ArrayList<ModifierKind> possibilityList = new ArrayList<ModifierKind>();

        possibilityList.add(ModifierKind.PUBLIC);
        possibilityList.add(ModifierKind.PRIVATE);
        possibilityList.add(ModifierKind.PROTECTED);

        return possibilityList;
    }

    /**
     * Génére tout les couples possibles de mutations
     * @return liste des mutants
     */
    public static List<ModifierProcessor> getEveryCouples() {

        ArrayList<ModifierKind> possibilityList = generateList();

        List<ModifierProcessor> list = new ArrayList<ModifierProcessor>();
        for (ModifierKind a : possibilityList) {
            for (ModifierKind b : possibilityList) {
                list.add(new ModifierProcessor(a, b));
            }
        }
        return list;
    }

    /**
     * Génére le nombre de couples demandés de mutations
     * @param nbCouples
     * @return liste des mutants
     */
    public static List<ModifierProcessor> getSomeCouples(int nbCouples) {

        ArrayList<ModifierKind> possibilityList = generateList();

        //Pour ne pas dépacer le nombre de mutations disponnibles
        if(nbCouples > generateList().size()*generateList().size()) {
            nbCouples = generateList().size()*generateList().size();
        }

        List<ModifierProcessor> list = new ArrayList<ModifierProcessor>();
        ArrayList<ModifierKind> listTwo;
        for(int i = 0; i < nbCouples; i++) {
            listTwo = Utils.choseTwoModifier(possibilityList);
            list.add(new ModifierProcessor(listTwo.get(0), listTwo.get(1)));
        }

        return list;
    }

}
