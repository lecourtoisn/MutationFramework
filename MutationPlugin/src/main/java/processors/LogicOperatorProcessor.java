package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;

import java.util.ArrayList;
import java.util.List;

public class LogicOperatorProcessor extends CustomProcessor<CtBinaryOperator<BinaryOperatorKind>>  {

    private BinaryOperatorKind b;
    private BinaryOperatorKind a;

    /**
     * Constructeur, prend en paramètre deux operateurs de mutations
     * @param a
     * @param b
     */
    public LogicOperatorProcessor(BinaryOperatorKind a, BinaryOperatorKind b) {
        super("Changes " + a + " to " + b);
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

        possibilityList.add(BinaryOperatorKind.AND);
        possibilityList.add(BinaryOperatorKind.OR);

        return possibilityList;
    }

    /**
     * Génére tout les couples possibles de mutations
     * @return liste des mutants
     */
    public static List<LogicOperatorProcessor> getEveryCouples() {

        ArrayList<BinaryOperatorKind> possibilityList = generateList();

        List<LogicOperatorProcessor> list = new ArrayList<LogicOperatorProcessor>();
        for (BinaryOperatorKind a : possibilityList) {
            for (BinaryOperatorKind b : possibilityList) {
                list.add(new LogicOperatorProcessor(a, b));
            }
        }
        return list;
    }

    /**
     * Génére le nombre de couples demandés de mutations
     * @param nbCouples
     * @return liste des mutants
     */
    public static List<LogicOperatorProcessor> getSomeCouples(int nbCouples) {

        ArrayList<BinaryOperatorKind> possibilityList = generateList();

        //Pour ne pas dépacer le nombre de mutations disponnibles
        if(nbCouples > generateList().size()*generateList().size()) {
            nbCouples = generateList().size()*generateList().size();
        }

        List<LogicOperatorProcessor> list = new ArrayList<LogicOperatorProcessor>();
        ArrayList<BinaryOperatorKind> listTwo;
        for(int i = 0; i < nbCouples; i++) {
            listTwo = Utils.choseTwoBinaryOperator(possibilityList);
            list.add(new LogicOperatorProcessor(listTwo.get(0), listTwo.get(1)));
        }

        return list;
    }
}
