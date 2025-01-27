/**
 * detectable
 *
 * Copyright (c) 2019 Synopsys, Inc.
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
package com.synopsys.integration.detectable.detectables.bazel.pipeline;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import com.synopsys.integration.detectable.detectables.bazel.WorkspaceRule;
import com.synopsys.integration.exception.IntegrationException;

public class WorkspaceRuleChooser {

    @NotNull
    public WorkspaceRule choose(final WorkspaceRule ruleFromWorkspaceFile, final String providedBazelDependencyType) throws IntegrationException {
        final WorkspaceRule finalBazelDependencyType1;
        if (StringUtils.isNotBlank(providedBazelDependencyType) && !"UNSPECIFIED".equalsIgnoreCase(providedBazelDependencyType)) {
            finalBazelDependencyType1 = WorkspaceRule.lookup(providedBazelDependencyType);
        } else if (ruleFromWorkspaceFile != WorkspaceRule.UNKNOWN) {
            finalBazelDependencyType1 = ruleFromWorkspaceFile;
        } else {
            throw new IntegrationException("Unable to determine BazelWorkspace dependency rule; try setting it via the property");
        }
        final WorkspaceRule finalBazelDependencyType = finalBazelDependencyType1;
        return finalBazelDependencyType;
    }
}
