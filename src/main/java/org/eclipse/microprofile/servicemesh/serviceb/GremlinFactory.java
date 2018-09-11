/*
 *******************************************************************************
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *******************************************************************************/
package org.eclipse.microprofile.servicemesh.serviceb;

import java.util.Random;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * GremlinFactory will randomly generate failures based on a configurable probability
 *
 */
@Dependent
public class GremlinFactory {

    /**
     * The probability that fail() will return true.
     * 0.0 means always false
     * 1.0 means always true
     * 0.5 means 50% chance to return true
     */
    @Inject
    @ConfigProperty(name = "failProbability", defaultValue = "0.2")
    private double failProbability;
    
    /**
     * Once a failure has begun, how long should it last for, in milliseconds
     * 0 means failure is always recalculated using the failProbability
     * 500 means that if a failure had already started, if fail is called again within 500ms, it will fail again
     */
    @Inject
    @ConfigProperty(name = "failureLength", defaultValue = "2000")
    private long failureLength;

    private boolean failureStarted = false;
    private long failureStartTime = System.nanoTime();
    
    private Random random = new Random();
    
    /**
     * Test to see if a failure is in progress. This could be one which was already started by a previous call or a new one.
     * 
     * If the failure is a new one then it is determined by the value of failProbability.
     * 
     * @return true if a failure is in progress
     */
    public boolean fail() {
        
        boolean fail = true;
        
        if(failureLength <= 0) {
            fail = caclulateFailure();
        }
        else {
            synchronized (this) {
                long now = System.nanoTime();
                //if a failure was already in progress, see how long it has been and if the max length has been reached
                if(failureStarted) {
                    long elapsedNanos = now - failureStartTime;
                    long failureLengthNanos = failureLength * 1000000;
                    //if the elaspsed failure time has exceeded the max length then clear the failure
                    if(elapsedNanos > failureLengthNanos) {
                        failureStarted = false;
                    }
                }
                
                //if there is no failure currently in progress then see if a new one should start
                if(!failureStarted) {
                    fail = caclulateFailure();
                    if(fail) {
                        failureStarted = true;
                        failureStartTime = now;
                    }
                }
            }
        }
        return fail;
    }
    
    private boolean caclulateFailure() {
        boolean fail = true;
        
        if (failProbability <= 0.0) {
            fail = false;
        } else if (failProbability > 0.0 && failProbability < 1.0) {
            double value = random.nextDouble();
            // any value up to and including the failProbability is a fail
            // any value greater than the failProbability is a pass
            if(value > failProbability) {
                fail = false;
            }
        }
        return fail;
    }

    public double getFailProbability() {
        return failProbability;
    }

}
