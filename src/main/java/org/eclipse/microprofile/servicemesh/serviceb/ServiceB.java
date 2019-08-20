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
 * Contributors:
 *   2018-06-19 - Jon Hawkes / IBM Corp
 *      Initial code
 *
 *******************************************************************************/

package org.eclipse.microprofile.servicemesh.serviceb;

import java.net.InetAddress;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metric;

@ApplicationScoped
public class ServiceB {

    @Inject
    @ConfigProperty(name = "minWorkTime", defaultValue = "100") //how long should the minimum simulated work be in ms
    private long minWorkTime;

    @Inject
    @ConfigProperty(name = "maxWorkTime", defaultValue = "5000") //how long should the maximum simulated work be in ms
    private long maxWorkTime;

    private Random random = new Random();

    @Inject
    @Metric(name="callCounter")
    Counter callCounter;

    @Counted(name="callCounter")
    public ServiceData call(String userAgent) throws Exception {
        long callCount = callCounter.getCount();

        simulateWork();

        String hostname;
        try {
            hostname = InetAddress.getLocalHost()
                                  .getHostName();
        } catch (java.net.UnknownHostException e) {
            hostname = e.getMessage();
        }

        ServiceData data = new ServiceData();
        data.setSource(this.toString() + " on " + hostname);
        data.setMessage("Hello from serviceB on >" + hostname + "< and agent: >" + userAgent+ "< @ "+data.getTime());
        data.setCallCount(callCount);
        data.setTries(1);

        return data;
    }

    /**
     * Simulate some work that takes somewhere between minWorkTime and maxWorkTime (in millis)
     */
    private void simulateWork() throws Exception {
        //simulate some work
        double randomDouble = random.nextDouble();
        long delta = (long) ((maxWorkTime-minWorkTime)*randomDouble);
        long workTime = minWorkTime+delta;

        Thread.sleep(workTime);
    }
}
