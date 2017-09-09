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
package com.pholser.junit.quickcheck.guided;

import java.io.IOException;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public abstract class GuidedJunitQuickcheckTest {
    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            try {
                GuidanceManager.getGuidance().notifyEndOfRun(true);
            } catch (IOException e) {
                throw new GuidanceIOException(e);
            }
        }

        @Override
        protected void failed(Throwable failure, Description description) {
            try {
                GuidanceManager.getGuidance().notifyEndOfRun(false);
            } catch (IOException e) {
                throw new GuidanceIOException(e);
            }
        }

        @Override
        protected void skipped(AssumptionViolatedException violation, Description description) {
            try {
                GuidanceManager.getGuidance().notifyEndOfRun(true);
            } catch (IOException e) {
                throw new GuidanceIOException(e);
            }
        }
    };

}
