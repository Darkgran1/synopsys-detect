package com.synopsys.integration.detect.cmake;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.synopsys.integration.detect.detector.clang.ClangCompileCommandParser;

public class LinkTxtParserTest {

    // TODO
    // Looks like ClangCompileCommandParser is pretty close to being able
    // to parsing link.txt files, at least the ones I've seen.
    // Need the ability to parse without adding -M, -MF <mkDepsFilePath>

    @Test
    public void testOpencv() throws IOException {
        final String compileCommandLine = FileUtils.readFileToString(new File("src/test/resources/cmake/link.txt"), StandardCharsets.UTF_8);
        final String expectedCommand = "/usr/bin/ar";
        final int expectedNumArgs = 24;
        final int argOneIndex = 3;
        final String argOneValue = "CMakeFiles/ade.dir/__/3rdparty/ade/ade-0.1.1d/sources/ade/source/assert.cpp.o";
        final int argTwoIndex = 19;
        final String argTwoValue = "/usr/bin/ranlib";

        doTest(compileCommandLine, expectedCommand, expectedNumArgs, argOneIndex, argOneValue, argTwoIndex, argTwoValue);
    }

    @Test
    public void testCustomerLinkFileContents() throws IOException {
        final String compileCommandLine = "/usr/bin/clang++-5.0  -fPIC  -Wall -Wextra -Wno-multichar -Winvalid-pch -Wno-unknown-pragmas -fvisibility=hidden -Wl,--disable-new-dtags -Wno-c++11-narrowing -g  -Wl,--no-undefined -shared -Wl,-soname,libcbl.so -o ../../bin/libcbl.so -L/opt/intel/system_studio_2015.1.045/mkl/lib/intel64 ../../vendors/double-conversion/libdouble-conversion.a -ldl /data/Qt/5.9.5/gcc_64/lib/libQt5Widgets.so.5.9.5 /opt/intel/ipp/lib/intel64/libippi.so /opt/intel/ipp/lib/intel64/libipps.so /opt/intel/ipp/lib/intel64/libippcore.so /opt/intel/ipp/lib/intel64/libippcc.so /opt/intel/ipp/lib/intel64/libippcv.so /opt/intel/ipp/lib/intel64/libippvm.so -lmkl_rt -liomp5 /data/Qt/5.9.5/gcc_64/lib/libQt5Gui.so.5.9.5 /data/Qt/5.9.5/gcc_64/lib/libQt5Core.so.5.9.5 -Wl,-rpath,\"\\$ORIGIN/../lib:\\$ORIGIN:/opt/intel/ipp/lib/intel64:/data/Qt/5.9.5/gcc_64/lib:/opt/intel/system_studio_2015.1.045/mkl/lib/intel64\"";
       final String expectedCommand = "/usr/bin/clang++-5.0";
        final int expectedNumArgs = 33;
        final int argOneIndex = 3;
        final String argOneValue = "-Wno-multichar";
        final int argTwoIndex = 19;
        final String argTwoValue = "/opt/intel/ipp/lib/intel64/libippi.so";

        doTest(compileCommandLine, expectedCommand, expectedNumArgs, argOneIndex, argOneValue, argTwoIndex, argTwoValue);
    }

    private void doTest(final String compileCommandLine, final String expectedCommand, final int expectedNumArgs, final int argOneIndex, final String argOneValue, final int argTwoIndex, final String argTwoValue) {
        final ClangCompileCommandParser parser = new ClangCompileCommandParser();
        final String linkCommand = parser.getCompilerCommand(compileCommandLine);
        assertEquals(expectedCommand, linkCommand);

        final String depsMkFilePath = "/tmp/deps.mk";
        final Map<String, String> optionOverrides = new HashMap<>();

        final List<String> parsedArgs = parser.getCompilerArgsForGeneratingDepsMkFile(compileCommandLine, depsMkFilePath, optionOverrides);

        assertEquals(expectedNumArgs, parsedArgs.size());
        assertEquals(argOneValue, parsedArgs.get(argOneIndex));
        assertEquals(argTwoValue, parsedArgs.get(argTwoIndex));
        System.out.printf("linkCommand: %s\n", linkCommand);
        for (String arg : parsedArgs) {
            System.out.printf("arg: %s\n", arg);
        }
    }

}
