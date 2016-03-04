package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;

import java.util.ArrayList;
import java.util.List;

public class BinaryOperatorProcessor extends CustomProcessor<CtBinaryOperator<BinaryOperatorKind>> {

    private BinaryOperatorKind b;
    private BinaryOperatorKind a;

    public BinaryOperatorProcessor(BinaryOperatorKind a, BinaryOperatorKind b) {
        super("Changes " + a + " to " + b);
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean isToBeProcessed(CtBinaryOperator<BinaryOperatorKind> candidate) {
        //return candidate.getKind() == BinaryOperatorKind.GE;
        return candidate.getKind() == a;
    }

    @Override
    public void process(CtBinaryOperator<BinaryOperatorKind> element) {
        if (isToBeProcessed(element)) {
            //element.setKind(BinaryOperatorKind.EQ);
            element.setKind(b);
        }
    }

    private static ArrayList<BinaryOperatorKind> generateList () {

        ArrayList<BinaryOperatorKind> possibilityList = new ArrayList<BinaryOperatorKind>();

        possibilityList.add(BinaryOperatorKind.GE);
        possibilityList.add(BinaryOperatorKind.EQ);
        possibilityList.add(BinaryOperatorKind.GT);
        possibilityList.add(BinaryOperatorKind.LE);
        possibilityList.add(BinaryOperatorKind.LT);
        possibilityList.add(BinaryOperatorKind.NE);

        return possibilityList;
    }

    public static List<BinaryOperatorProcessor> getEveryCouples() {

        ArrayList<BinaryOperatorKind> possibilityList = generateList();

        List<BinaryOperatorProcessor> list = new ArrayList<BinaryOperatorProcessor>();
        for (BinaryOperatorKind a : possibilityList) {
            for (BinaryOperatorKind b : possibilityList) {
                list.add(new BinaryOperatorProcessor(a, b));
            }
        }
        return list;
    }

    public static List<BinaryOperatorProcessor> getSomeCouples(int nbCouples) {

        ArrayList<BinaryOperatorKind> possibilityList = generateList();

        //Pour ne pas dépacer le nombre de mutations disponnibles
        if(nbCouples > generateList().size()*generateList().size()) {
            nbCouples = generateList().size()*generateList().size();
        }

        List<BinaryOperatorProcessor> list = new ArrayList<BinaryOperatorProcessor>();
        ArrayList<BinaryOperatorKind> listTwo;
        for(int i = 0; i < nbCouples; i++) {
            listTwo = Utils.choseTwoBinaryOperator(possibilityList);
            list.add(new BinaryOperatorProcessor(listTwo.get(0), listTwo.get(1)));
        }

        return list;
    }


}
