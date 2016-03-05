package processors;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtType;

import java.util.Set;

public class RemoveConstructorProcessor extends CustomProcessor<CtClass> {
    public RemoveConstructorProcessor() {
        super("Remove every constructors");
    }

    @Override
    public void process(CtClass element) {
        Set<CtConstructor> constructors = element.getConstructors();
        for (CtConstructor constructor : constructors) {
            element.removeConstructor(constructor);
        }
    }
}
