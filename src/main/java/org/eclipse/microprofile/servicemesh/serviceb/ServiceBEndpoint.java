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
import java.net.UnknownHostException;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;

@Path("serviceB")
@RequestScoped
public class ServiceBEndpoint {

  @Inject
  @ConfigProperty(name = "failFrequency", defaultValue = "0")
  private int failFrequency;

  private static int callCount;

  @GET
  @Retry
  public String hello() throws Exception {
    String hostname;

    ++callCount;

    if (failFrequency > 0 && callCount % failFrequency == 0) {
      throw new Exception("ServiceB deliberately caused to fail. Call count: " + callCount + ", failFrequency: " + failFrequency);
    }

    try {
      hostname = InetAddress.getLocalHost().getHostName();
    } catch(java.net.UnknownHostException e) {
      hostname = e.getMessage();
    }

    return "Hello from serviceB (" + this + ") at " + new Date() + " on " + hostname + " (ServiceB call count: " + callCount + ", failFrequency: " + failFrequency + ")";
  }
}
