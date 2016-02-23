package processors;

import spoon.reflect.code.CtReturn;

import spoon.processing.AbstractProcessor;
import spoon.support.reflect.code.CtLiteralImpl;

public class ReturnProcessor extends AbstractProcessor<CtReturn<String>> {
    @Override
    public void process(CtReturn<String> element) {
        CtLiteralImpl<String> lit = new CtLiteralImpl<String>();
        lit.setValue("Youpi banane");
        element.setReturnedExpression(lit);
    }
}
