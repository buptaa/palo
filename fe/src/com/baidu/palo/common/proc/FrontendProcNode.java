// Modifications copyright (C) 2017, Baidu.com, Inc.
// Copyright 2017 The Apache Software Foundation

// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package com.baidu.palo.common.proc;

import com.baidu.palo.catalog.Catalog;
import com.baidu.palo.system.Frontend;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Show current added frontends
 * SHOW PROC /frontend/
 */
public class FrontendProcNode implements ProcNodeInterface {
    private Catalog catalog;

    public FrontendProcNode(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public ProcResult fetchResult() {
        BaseProcResult result = new BaseProcResult();
        result.setNames(Arrays.asList("Host", "Port", "Role", "IsMaster", "ClusterId"));
        
        InetSocketAddress master = Catalog.getInstance().getHaProtocol().getLeader();
        String masterIp = master.getAddress().getHostAddress();
        int masterPort = master.getPort();
        
        for (Frontend fe : catalog.getFrontends()) {
            List<String> info = new ArrayList<String>();
            info.add(fe.getHost());
            info.add(Integer.toString(fe.getPort()));
            info.add(fe.getRole().name());
            if (fe.getHost().equals(masterIp) && fe.getPort() == masterPort) {
                info.add("true");
            } else {
                info.add("false");
            }
            info.add(Integer.toString(catalog.getClusterId()));
            result.addRow(info);
        }
        return result;
    }
}

