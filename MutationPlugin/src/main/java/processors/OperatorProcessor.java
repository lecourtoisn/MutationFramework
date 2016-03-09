package processors;

import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;

import java.util.ArrayList;
import java.util.List;

public class OperatorProcessor extends CustomProcessor<CtBinaryOperator<BinaryOperatorKind>>  {

    private BinaryOperatorKind b;
    private BinaryOperatorKind a;

    /**
     * Constructeur, prend en paramètre deux operateurs de mutations
     * @param a
     * @param b
     */
    public OperatorProcessor(BinaryOperatorKind a, BinaryOperatorKind b) {
        super("Remplace les" + a + " en " + b);
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean isToBeProcessed(CtBinaryOperator<BinaryOperatorKind> candidate) {
        return candidate.getKind() == a;
    }

    @Override
    public void process(CtBinaryOperator<BinaryOperatorKind> element) {
        if (isToBeProcessed(element)) {
            element.setKind(b);
        }
    }


    /**
     * Liste de toutes les opérateurs possiblent
     * @return
     */
    private static ArrayList<BinaryOperatorKind> generateList () {

        ArrayList<BinaryOperatorKind> possibilityList = new ArrayList<BinaryOperatorKind>();

        possibilityList.add(BinaryOperatorKind.DIV);
        possibilityList.add(BinaryOperatorKind.MINUS);
        possibilityList.add(BinaryOperatorKind.MUL);
        possibilityList.add(BinaryOperatorKind.MOD);
        possibilityList.add(BinaryOperatorKind.PLUS);

        return possibilityList;
    }

    /**
     * Génére tout les couples possibles de mutations
     * @return liste des mutants
     */
    public static List<OperatorProcessor> getEveryCouples() {

        ArrayList<BinaryOperatorKind> possibilityList = generateList();

        List<OperatorProcessor> list = new ArrayList<OperatorProcessor>();
        for (BinaryOperatorKind a : possibilityList) {
            for (BinaryOperatorKind b : possibilityList) {
                list.add(new OperatorProcessor(a, b));
            }
        }
        return list;
    }

    /**
     * Génére le nombre de couples demandés de mutations
     * @param nbCouples
     * @return liste des mutants
     */
    public static List<OperatorProcessor> getSomeCouples(int nbCouples) {

        ArrayList<BinaryOperatorKind> possibilityList = generateList();

        //Pour ne pas dépacer le nombre de mutations disponnibles
        if(nbCouples > generateList().size()*generateList().size()) {
            nbCouples = generateList().size()*generateList().size();
        }

        List<OperatorProcessor> list = new ArrayList<OperatorProcessor>();
        ArrayList<BinaryOperatorKind> listTwo;
        for(int i = 0; i < nbCouples; i++) {
            listTwo = Utils.choseTwoBinaryOperator(possibilityList);
            list.add(new OperatorProcessor(listTwo.get(0), listTwo.get(1)));
        }

        return list;
    }

}
