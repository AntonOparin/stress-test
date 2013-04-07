package com.stresstest.random;

import com.stresstest.random.generator.CachedValueGeneratorFactory;
import com.stresstest.random.generator.RandomValueGeneratorFactory;
import com.stresstest.random.generator.ValueGeneratorFactory;

public class ObjectGenerator {

    final private static ValueGeneratorFactory STANDARD_VALUE_GENERATOR = new RandomValueGeneratorFactory();

    private static ValueGeneratorFactory valueGeneratorFactory = STANDARD_VALUE_GENERATOR;

    private ObjectGenerator(){
        throw new IllegalAccessError();
    }
    
    public static void enableCaching() {
        valueGeneratorFactory = new CachedValueGeneratorFactory(STANDARD_VALUE_GENERATOR);
    }

    public static void disableCaching() {
        valueGeneratorFactory = STANDARD_VALUE_GENERATOR;
    }

    public static <T> T generate(Class<T> classToGenerate) {
        return getValueGenerator(classToGenerate).generate();
    }

    public static <T> ValueGenerator<T> getValueGenerator(Class<T> classToGenerate) {
        return valueGeneratorFactory.getValueGenerator(classToGenerate);
    }
}
