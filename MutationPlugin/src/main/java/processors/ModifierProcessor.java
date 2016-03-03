package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.declaration.ModifierKind;

import java.util.ArrayList;
import java.util.List;

public class ModifierProcessor extends AbstractProcessor<CtTypeAccess<ModifierKind>> {

    private ModifierKind b;
    private ModifierKind a;

    public ModifierProcessor(ModifierKind a, ModifierKind b) {
        this.a = a;
        this.b = b;
    }

    //CtTypeAccess
    @Override
    public boolean isToBeProcessed(CtTypeAccess<ModifierKind> candidate) {
        return false;//candidate.getType() == listTwo.get(0); TODO
    }


    @Override
    public void process(CtTypeAccess<ModifierKind> element) {
        if (isToBeProcessed(element)) {
            //element.setKind(b); TODO
        }
    }

    private static ArrayList<ModifierKind> generateList () {

        ArrayList<ModifierKind> possibilityList = new ArrayList<ModifierKind>();

        possibilityList.add(ModifierKind.PUBLIC);
        possibilityList.add(ModifierKind.PRIVATE);
        possibilityList.add(ModifierKind.PROTECTED);

        return possibilityList;
    }

    public static List<ModifierProcessor> getEveryCouples() {

        ArrayList<ModifierKind> possibilityList = generateList();

        List<ModifierProcessor> list = new ArrayList<ModifierProcessor>();
        for (ModifierKind a : possibilityList) {
            for (ModifierKind b : possibilityList) {
                list.add(new ModifierProcessor(a, b));
            }
        }
        return list;
    }

}
