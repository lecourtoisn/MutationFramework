package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;

import java.util.ArrayList;

public class LogicOperatorProcessor extends AbstractProcessor<CtBinaryOperator<BinaryOperatorKind>> {


    ArrayList<BinaryOperatorKind> listGenerated = generateList();
    ArrayList<BinaryOperatorKind> listTwo = Util.choseTwoBinaryOperator(listGenerated);

    @Override
    public boolean isToBeProcessed(CtBinaryOperator<BinaryOperatorKind> candidate) {
        return candidate.getKind() == listTwo.get(0);
    }

    @Override
    public void process(CtBinaryOperator<BinaryOperatorKind> element) {
        if (isToBeProcessed(element)) {
            element.setKind(listTwo.get(1));
        }
    }

    public ArrayList<BinaryOperatorKind> generateList () {

        ArrayList<BinaryOperatorKind> possibilityList = new ArrayList<BinaryOperatorKind>();

        possibilityList.add(BinaryOperatorKind.AND);
        possibilityList.add(BinaryOperatorKind.OR);

        return possibilityList;
    }

}
