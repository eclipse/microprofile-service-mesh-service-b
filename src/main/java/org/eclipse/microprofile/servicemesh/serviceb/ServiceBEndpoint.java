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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.servicemesh.common.CallCounter;

@Path("serviceB")
@RequestScoped
public class ServiceBEndpoint {

    @Inject
    @ConfigProperty(name = "failFrequency", defaultValue = "0.8") // 0.8 means fail 80% of the time
    private double failFrequency;

    @Inject
    CallCounter callCounter;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceData call() throws Exception {
        String hostname;

        int callCount = callCounter.increment();

        System.out.println("callCount: " + callCount);

        boolean pass = true;
        if (failFrequency >= 1.0) {
            pass = false;
        } else if (failFrequency > 0 && failFrequency < 1) {
            int passRate = (int) (1.0 / (1.0 - failFrequency));
            int remainder = callCount % passRate;
            pass = remainder == 0;
        }

        if (pass) {

            try {
                hostname = InetAddress.getLocalHost()
                                      .getHostName();
            } catch (java.net.UnknownHostException e) {
                hostname = e.getMessage();
            }

            ServiceData data = new ServiceData();
            data.setSource(this.toString() + " on " + hostname + ", failFrequency: " + failFrequency);
            data.setMessage("Hello from serviceB");
            data.setCallCount(callCount);
            data.setTries(1);

            return data;
            
        } else {

            Exception e = new Exception("ServiceB deliberately caused to fail. Call count: " + callCount
                    + ", failFrequency: " + failFrequency);
            System.out.println("Throwing: " + e.getMessage());
            throw e;

        }
    }
}
