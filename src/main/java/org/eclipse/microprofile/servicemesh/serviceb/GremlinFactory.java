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

    @Inject
    @ConfigProperty(name = "failProbablibity", defaultValue = "0.0") // 0.0 means always pass, 1.0 means always fail, 0.5 means 50% chance to fail
    private double failProbablibity;

    private Random random = new Random();
    
    public boolean fail() {
        boolean fail = true;
        if (failProbablibity <= 0.0) {
            fail = false;
        } else if (failProbablibity > 0.0 && failProbablibity < 1.0) {
            double value = random.nextDouble();
            // any value up to and including the failProbablibity is a fail
            // any value greater than the failProbablibity is a pass
            if(value > failProbablibity) {
                fail = false;
            }
        }
        return fail;
    }

    public double getFailProbablibity() {
        return failProbablibity;
    }

}
