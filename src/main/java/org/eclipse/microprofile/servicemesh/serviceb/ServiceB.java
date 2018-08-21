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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metric;

@RequestScoped
public class ServiceB {

    @Inject
    GremlinFactory gremlinFactory;

    @Inject
    @Metric(name="callCounter")
    Counter callCounter;

    @Counted(name="callCounter", monotonic=true)
    public ServiceData call() throws Exception {
        long callCount = callCounter.getCount();

        String hostname;
        passOrFail();
        
        try {
            hostname = InetAddress.getLocalHost()
                                  .getHostName();
        } catch (java.net.UnknownHostException e) {
            hostname = e.getMessage();
        }

        ServiceData data = new ServiceData();
        double failProbability = gremlinFactory.getFailProbability();
        data.setSource(this.toString() + " on " + hostname + ", failProbability: " + failProbability);
        data.setMessage("Hello from serviceB @ "+data.getTime());
        data.setCallCount(callCount);
        data.setTries(1);

        return data;
            
    }
    
    
    /**
     * Use the Gremlin Factory to decide if this call should pass or fail. If it should fail then this method throws an Exception.
     * Otherwise it does nothing.
     * 
     * @throws Exception thrown if the call should fail
     */
    private void passOrFail() throws Exception {
        boolean fail = gremlinFactory.fail();

        if(fail) {
            long callCount = callCounter.getCount();
            double failProbability = gremlinFactory.getFailProbability();
            Exception e = new Exception("ServiceB deliberately caused to fail. Call count: " + callCount
                    + ", failProbability: " + failProbability);
            System.out.println("Throwing: " + e.getMessage());
            throw e;
        }
    }
}
