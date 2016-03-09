package processors;

import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.declaration.ModifierKind;
import spoon.support.reflect.declaration.CtFieldImpl;
import spoon.support.reflect.declaration.CtMethodImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModifierProcessor extends CustomProcessor<CtModifiable> {

    private ModifierKind b;
    private ModifierKind a;

    /**
     * Constructeur, prend en paramètre deux operateurs de mutations
     * @param a
     * @param b
     */
    public ModifierProcessor(ModifierKind a, ModifierKind b) {
        super("Remplace les " + a + " en " + b);
        this.a = a;
        this.b = b;
    }

    /**
     * Return true if the candidate is a method or a field and it contains the modifier to replace
     * @param candidate
     */
    @Override
    public boolean isToBeProcessed(CtModifiable candidate) {
        return (candidate instanceof CtMethodImpl || candidate instanceof CtFieldImpl) && candidate.getModifiers().contains(a);
    }

    @Override
    public void process(CtModifiable element) {
        if (isToBeProcessed(element)) {
            Set<ModifierKind> modifiers = element.getModifiers();
            modifiers.remove(a);
            modifiers.add(b);
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
                //list.add(new ModifierProcessor(a, b));
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
