package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;

import java.util.ArrayList;
import java.util.List;

public class LogicOperatorProcessor extends CustomProcessor<CtBinaryOperator<BinaryOperatorKind>>  {

    private BinaryOperatorKind b;
    private BinaryOperatorKind a;

    public LogicOperatorProcessor(BinaryOperatorKind a, BinaryOperatorKind b) {
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

    private static ArrayList<BinaryOperatorKind> generateList () {

        ArrayList<BinaryOperatorKind> possibilityList = new ArrayList<BinaryOperatorKind>();

        possibilityList.add(BinaryOperatorKind.AND);
        possibilityList.add(BinaryOperatorKind.OR);

        return possibilityList;
    }

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

    @Override
    protected String getMutationID() {
        return "Changes " + a + " to " + b;
    }

}
