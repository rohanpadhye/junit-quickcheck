/*
 The MIT License

 Copyright (c) 2017 University of California, Berkeley

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

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Stream;

import com.pholser.junit.quickcheck.guided.GuidanceIOException;
import com.pholser.junit.quickcheck.guided.GuidanceManager;
import com.pholser.junit.quickcheck.internal.ParameterSampler;
import com.pholser.junit.quickcheck.internal.SeededValue;
import com.pholser.junit.quickcheck.internal.generator.PropertyParameterGenerationContext;
import com.pholser.junit.quickcheck.random.FileBackedRandom;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.util.stream.Collectors.*;

public class GuidedParameterSampler implements ParameterSampler {
    private final int trials;
    private final FileBackedRandom randomFile;
    private final SourceOfRandomness random;

    public GuidedParameterSampler(int trials) {
        this.trials = trials;
        this.randomFile = new FileBackedRandom(GuidanceManager.getGuidance().inputFile());
        this.random = new SourceOfRandomness(randomFile);
    }

    @Override public int sizeFactor(Parameter p) {
        return trials;
    }

    @Override public Stream<List<SeededValue>> sample(
            List<PropertyParameterGenerationContext> parameters) {

        Stream<List<SeededValue>> tupleStream =
                Stream.generate(() -> {
                    // Block until guided is ready
                    try {
                        GuidanceManager.getGuidance().waitForInput();
                    } catch (IOException e) {
                        throw new GuidanceIOException(e);
                    }

                    // Read input from random file
                    randomFile.open();
                    List<SeededValue> seededValues =
                            parameters.stream()
                            .map(SeededValue::new)
                            .collect(toList());
                    randomFile.close();
                    return seededValues;
                });

        return tupleStream.limit(trials);
    }

    @Override
    public SourceOfRandomness random() {
        return this.random;
    }


}
