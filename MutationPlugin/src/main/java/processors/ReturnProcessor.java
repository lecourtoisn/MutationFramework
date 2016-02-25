package processors;

import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtReturn;

import spoon.processing.AbstractProcessor;
import spoon.support.reflect.code.CtLiteralImpl;

public class ReturnProcessor extends AbstractProcessor<CtReturn<String>> {

    @Override
    public boolean isToBeProcessed(CtReturn candidate) {
        return candidate.getReturnedExpression().getType().getActualClass().equals(String.class);
    }

    @Override
    public void process(CtReturn<String> element) {
        if (!isToBeProcessed(element)) {
            System.out.println(element.getReturnedExpression());
            return;
        }

        CtLiteralImpl<String> newReturnValue = new CtLiteralImpl<String>();
        newReturnValue.setValue("Youpi banane");

        element.setReturnedExpression(newReturnValue);

    }
}
