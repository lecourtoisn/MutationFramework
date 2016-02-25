package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtReturn;
import spoon.support.reflect.code.CtLiteralImpl;

public class ReturnIntProcessor extends AbstractProcessor<CtReturn<Integer>> {

    @Override
    public void process(CtReturn<Integer> element) {
        CtLiteralImpl<Integer> lit = new CtLiteralImpl<Integer>();
        lit.setValue(3);
        element.setReturnedExpression(lit);
    }
}
