package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;

import java.util.ArrayList;

public class GreaterEqualsProcessor extends AbstractProcessor<CtBinaryOperator<BinaryOperatorKind>> {


    ArrayList<BinaryOperatorKind> listGenerated = generateList();
    ArrayList<BinaryOperatorKind> listTwo = Utils.choseTwoBinaryOperator(listGenerated);

    @Override
    public boolean isToBeProcessed(CtBinaryOperator<BinaryOperatorKind> candidate) {
        //return candidate.getKind() == BinaryOperatorKind.GE;
        return candidate.getKind() == listTwo.get(0);
    }

    @Override
    public void process(CtBinaryOperator<BinaryOperatorKind> element) {
        if (isToBeProcessed(element)) {
            //element.setKind(BinaryOperatorKind.EQ);
            element.setKind(listTwo.get(1));
        }
    }

    public ArrayList<BinaryOperatorKind> generateList () {

        ArrayList<BinaryOperatorKind> possibilityList = new ArrayList<BinaryOperatorKind>();

        possibilityList.add(BinaryOperatorKind.GE);
        possibilityList.add(BinaryOperatorKind.EQ);
        possibilityList.add(BinaryOperatorKind.GT);
        possibilityList.add(BinaryOperatorKind.LE);
        possibilityList.add(BinaryOperatorKind.LT);
        possibilityList.add(BinaryOperatorKind.NE);

        return possibilityList;
    }

}
