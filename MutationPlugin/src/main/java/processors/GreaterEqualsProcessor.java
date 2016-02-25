package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;

public class GreaterEqualsProcessor extends AbstractProcessor<CtBinaryOperator<BinaryOperatorKind>> {

    @Override
    public boolean isToBeProcessed(CtBinaryOperator<BinaryOperatorKind> candidate) {
        return candidate.getKind() == BinaryOperatorKind.GT;
    }

    @Override
    public void process(CtBinaryOperator<BinaryOperatorKind> element) {
        if (isToBeProcessed(element)) {
            element.setKind(BinaryOperatorKind.EQ);
        }
    }
}
