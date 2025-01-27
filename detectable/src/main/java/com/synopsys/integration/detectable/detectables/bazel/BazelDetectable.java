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
package com.synopsys.integration.detectable.detectables.bazel;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import com.synopsys.integration.detectable.Detectable;
import com.synopsys.integration.detectable.DetectableEnvironment;
import com.synopsys.integration.detectable.Extraction;
import com.synopsys.integration.detectable.ExtractionEnvironment;
import com.synopsys.integration.detectable.detectable.exception.DetectableException;
import com.synopsys.integration.detectable.detectable.executable.resolver.BazelResolver;
import com.synopsys.integration.detectable.detectable.file.FileFinder;
import com.synopsys.integration.detectable.detectable.result.DetectableResult;
import com.synopsys.integration.detectable.detectable.result.ExecutableNotFoundDetectableResult;
import com.synopsys.integration.detectable.detectable.result.FilesNotFoundDetectableResult;
import com.synopsys.integration.detectable.detectable.result.PassedDetectableResult;
import com.synopsys.integration.detectable.detectable.result.PropertyInsufficientDetectableResult;

public class BazelDetectable extends Detectable {
    public static final String WORKSPACE_FILENAME = "WORKSPACE";
    private final FileFinder fileFinder;
    private final BazelExtractor bazelExtractor;
    private final BazelResolver bazelResolver;
    private final BazelDetectableOptions bazelDetectableOptions;
    private File bazelExe;
    private BazelWorkspace bazelWorkspace;

    public BazelDetectable(final DetectableEnvironment environment, final FileFinder fileFinder, final BazelExtractor bazelExtractor,
        final BazelResolver bazelResolver, final BazelDetectableOptions bazelDetectableOptions) {
        super(environment, "bazel", "bazel");
        this.fileFinder = fileFinder;
        this.bazelExtractor = bazelExtractor;
        this.bazelResolver = bazelResolver;
        this.bazelDetectableOptions = bazelDetectableOptions;
    }

    @Override
    public DetectableResult applicable() {
        if (StringUtils.isBlank(bazelDetectableOptions.getTargetName())) {
            return new PropertyInsufficientDetectableResult();
        }
        return new PassedDetectableResult();
    }

    @Override
    public DetectableResult extractable() throws DetectableException {
        final File workspaceFile = fileFinder.findFile(environment.getDirectory(), WORKSPACE_FILENAME);
        if (workspaceFile == null) {
            return new FilesNotFoundDetectableResult(WORKSPACE_FILENAME);
        }
        bazelWorkspace = new BazelWorkspace(workspaceFile);
        bazelExe = bazelResolver.resolveBazel();
        if (bazelExe == null) {
            return new ExecutableNotFoundDetectableResult("bazel");
        }
        return new PassedDetectableResult();
    }

    @Override
    public Extraction extract(ExtractionEnvironment extractionEnvironment) {
        final BazelProjectNameGenerator projectNameGenerator = new BazelProjectNameGenerator();
        Extraction extractResult = bazelExtractor.extract(bazelExe, environment.getDirectory(), bazelWorkspace, bazelDetectableOptions.getTargetName(), projectNameGenerator, bazelDetectableOptions.getBazelDependencyRule());
        return extractResult;
    }
}
