/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck.runner.sampling;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import com.pholser.junit.quickcheck.internal.ParameterSampler;
import com.pholser.junit.quickcheck.internal.SeededValue;
import com.pholser.junit.quickcheck.internal.generator.PropertyParameterGenerationContext;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.stream.Collectors.*;

public class TupleParameterSampler implements ParameterSampler {
    private final int trials;
    private final SourceOfRandomness random;

    public TupleParameterSampler(int trials) {
        this.trials = trials;
        this.random = new SourceOfRandomness(new Random());
    }

    @Override public int sizeFactor(Parameter p) {
        return trials;
    }

    @Override public Stream<List<SeededValue>> sample(
        List<PropertyParameterGenerationContext> parameters) {

        Stream<List<SeededValue>> tupleStream =
            Stream.generate(
                () -> parameters.stream()
                    .map(SeededValue::new)
                    .collect(toList()));
        return tupleStream.limit(trials);
    }

    @Override
    public SourceOfRandomness random() {
        return this.random;
    }
}
