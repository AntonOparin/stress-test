package com.stresstest.random;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CachedValueGeneratorFactory implements ValueGeneratorFactory {

    final private ValueGeneratorFactory delegateValueGenerator;

    final private LoadingCache<Class<?>, ValueGenerator<?>> cachedValueGenerators = CacheBuilder.newBuilder().build(new CacheLoader<Class<?>, ValueGenerator<?>>() {
        @Override
        public ValueGenerator<?> load(Class<?> key) throws Exception {
            return delegateValueGenerator.getValueGenerator(key);
        }
    });

    public CachedValueGeneratorFactory(ValueGeneratorFactory valueGeneratorFactory) {
        this.delegateValueGenerator = checkNotNull(valueGeneratorFactory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ValueGenerator<T> getValueGenerator(Class<T> klass) {
        try {
            return (ValueGenerator<T>) cachedValueGenerators.get(klass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
