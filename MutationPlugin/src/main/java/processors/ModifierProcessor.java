package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.ModifierKind;

import java.util.ArrayList;

public class ModifierProcessor extends AbstractProcessor<CtBinaryOperator<ModifierKind>> {


    ArrayList<ModifierKind> listGenerated = generateList();
    ArrayList<ModifierKind> listTwo = Utils.choseTwoModifier(listGenerated);

    @Override
    public boolean isToBeProcessed(CtBinaryOperator<ModifierKind> candidate) {
        return false;//candidate.getType() == listTwo.get(0); TODO
    }

    @Override
    public void process(CtBinaryOperator<ModifierKind> element) {
        if (isToBeProcessed(element)) {
            //element.setType(listTwo.get(1)); TODO
        }
    }

    public ArrayList<ModifierKind> generateList () {

        ArrayList<ModifierKind> possibilityList = new ArrayList<ModifierKind>();

        possibilityList.add(ModifierKind.PUBLIC);
        possibilityList.add(ModifierKind.PRIVATE);
        possibilityList.add(ModifierKind.PROTECTED);

        return possibilityList;
    }

}
