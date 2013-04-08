package com.stresstest.random.constructor;


import java.lang.reflect.Modifier;
import java.util.Collection;

import com.stresstest.random.ObjectValueGenerator;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.generator.ValueGeneratorFactory;

/**
 * Constructor of empty Objects, for {@link ObjectValueGenerator}.
 * 
 * @author Anton Oparin
 * 
 * @param <T>
 *            parameterized {@link Class}.
 */
abstract public class ClassConstructor<T> {

    /**
     * Returns {@link Object} of defined type.
     * 
     * @return empty {@link Object} of defined type.
     */
    abstract public T construct();
    
    abstract public Collection<ValueGenerator<?>> getValueGenerators();

    /**
     * Generates {@link ClassConstructor}. It firstly checks Constructor, than FactoryMethod and the last is Builder based construction.
     * 
     * @param classToGenerate
     *            {@link Class} to generate.
     * @param valueGeneratorFactory
     *            {@link ValueGeneratorFactory} to use.
     * @return {@link ClassConstructor} if it is possible to generate one, <code>null</code> otherwise.
     */
	@SuppressWarnings({"unchecked"})
    public static <T> ClassConstructor<T> construct(final ClassAccessWrapper<?> classToGenerate, final ValueGeneratorFactory valueGeneratorFactory) {
        ClassConstructor<T> objectConstructor = null;
        if ((objectConstructor = ClassConstructorFactory.build(classToGenerate, valueGeneratorFactory)) != null)
            return objectConstructor;
        if ((objectConstructor = ClassConstructorBuilder.build(classToGenerate, valueGeneratorFactory)) != null)
            return objectConstructor;
        return (ClassConstructor<T>) ((classToGenerate.getModifiers() & Modifier.ABSTRACT) == 0 ? ClassConstructorSimple.build(classToGenerate,
                valueGeneratorFactory) : null);
    }

}
