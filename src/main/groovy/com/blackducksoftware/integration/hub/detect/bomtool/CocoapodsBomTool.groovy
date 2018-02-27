/*
 * hub-detect
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.integration.hub.detect.bomtool

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.blackducksoftware.integration.hub.bdio.graph.DependencyGraph
import com.blackducksoftware.integration.hub.bdio.model.Forge
import com.blackducksoftware.integration.hub.bdio.model.externalid.ExternalId
import com.blackducksoftware.integration.hub.bdio.model.externalid.ExternalIdFactory
import com.blackducksoftware.integration.hub.detect.bomtool.cocoapods.CocoapodsPackager
import com.blackducksoftware.integration.hub.detect.model.BomToolType
import com.blackducksoftware.integration.hub.detect.model.DetectCodeLocation

import groovy.transform.TypeChecked

@Component
@TypeChecked
class CocoapodsBomTool extends BomTool {
    private final Logger logger = LoggerFactory.getLogger(CocoapodsBomTool.class)

    public static final String PODFILE_LOCK_FILENAME= 'Podfile.lock'

    @Autowired
    CocoapodsPackager cocoapodsPackager

    @Autowired
    ExternalIdFactory externalIdFactory


    BomToolType getBomToolType() {
        return BomToolType.COCOAPODS
    }

    boolean isBomToolApplicable() {
        detectFileManager.containsAllFiles(sourcePath, PODFILE_LOCK_FILENAME)
    }

    List<DetectCodeLocation> extractDetectCodeLocations() {
        final String podLockText = new File(sourcePath, PODFILE_LOCK_FILENAME).text

        DependencyGraph dependencyGraph = cocoapodsPackager.extractDependencyGraph(podLockText)
        ExternalId externalId = externalIdFactory.createPathExternalId(Forge.COCOAPODS, sourcePath)

        def codeLocation = new DetectCodeLocation.Builder(getBomToolType(), sourcePath, externalId, dependencyGraph).build()
        [codeLocation]
    }
}