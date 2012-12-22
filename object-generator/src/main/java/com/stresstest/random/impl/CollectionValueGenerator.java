package com.stresstest.random.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.stresstest.random.ValueGenerator;

public class CollectionValueGenerator<T> implements ValueGenerator<Collection<T>>{
    
    @Override
    public Collection<T> generate() {
        return new ArrayList<T>();
    }

}
