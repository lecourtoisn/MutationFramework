package processors;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;

import java.util.Set;

public class RemoveConstructorProcessor extends CustomProcessor<CtClass> {
    public RemoveConstructorProcessor() {
        super("Supprime tous les constructeurs");
    }

    @Override
    public void process(CtClass element) {
        Set<CtConstructor> constructors = element.getConstructors();
        for (CtConstructor constructor : constructors) {
            element.removeConstructor(constructor);
        }
    }
}
