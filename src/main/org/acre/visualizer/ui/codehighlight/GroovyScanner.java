/**
 *   Copyright 2004-2005 Sun Microsystems, Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.acre.visualizer.ui.codehighlight;

/**
 * Simple Groovy scanner
 */
public class GroovyScanner extends JavaScanner {
    /**
     * Initializes list of Groovy keywords
     */
    protected void initSymbolTable() {
        lookup(KEYWORD, "abstract");
        lookup(KEYWORD, "as");
        lookup(KEYWORD, "assert");
        lookup(KEYWORD, "boolean");
        lookup(KEYWORD, "break");
        lookup(KEYWORD, "byte");
        lookup(KEYWORD, "case");
        lookup(KEYWORD, "catch");
        lookup(KEYWORD, "char");
        lookup(KEYWORD, "class");
        lookup(KEYWORD, "continue");
        lookup(KEYWORD, "default");
        lookup(KEYWORD, "do");
        lookup(KEYWORD, "double");
        lookup(KEYWORD, "else");
        lookup(KEYWORD, "extends");
        lookup(KEYWORD, "final");
        lookup(KEYWORD, "finally");
        lookup(KEYWORD, "for");
        lookup(KEYWORD, "get");
        lookup(KEYWORD, "if");
        lookup(KEYWORD, "implements");
        lookup(KEYWORD, "import");
        lookup(KEYWORD, "in");
        lookup(KEYWORD, "instanceof");
        lookup(KEYWORD, "int");
        lookup(KEYWORD, "interface");
        lookup(KEYWORD, "it");
        lookup(KEYWORD, "long");
        lookup(KEYWORD, "mixin");
        lookup(KEYWORD, "native");
        lookup(KEYWORD, "new");
        lookup(KEYWORD, "package");
        lookup(KEYWORD, "private");
        lookup(KEYWORD, "property");
        lookup(KEYWORD, "protected");
        lookup(KEYWORD, "public");
        lookup(KEYWORD, "return");
        lookup(KEYWORD, "set");
        lookup(KEYWORD, "short");
        lookup(KEYWORD, "static");
        lookup(KEYWORD, "super");
        lookup(KEYWORD, "switch");
        lookup(KEYWORD, "synchronized");
        lookup(KEYWORD, "this");
        lookup(KEYWORD, "throw");
        lookup(KEYWORD, "transient");
        lookup(KEYWORD, "true");
        lookup(KEYWORD, "try");
        lookup(KEYWORD, "void");
        lookup(KEYWORD, "volative");
        lookup(KEYWORD, "while");

        // TODO: Additional Standard function names
        // TODO: Extend scanner and display in a different color
        if (false) {
            lookup(KEYWORD, "abs");
            lookup(KEYWORD, "accept");
            lookup(KEYWORD, "allProperties");
            lookup(KEYWORD, "and");
            lookup(KEYWORD, "any");
            lookup(KEYWORD, "append");
            lookup(KEYWORD, "asImmutable");
            lookup(KEYWORD, "asList");
            lookup(KEYWORD, "asSynchronized");
            lookup(KEYWORD, "asWritable");
            lookup(KEYWORD, "center");
            lookup(KEYWORD, "collect");
            lookup(KEYWORD, "compareTo");
            lookup(KEYWORD, "contains");
            lookup(KEYWORD, "count");
            lookup(KEYWORD, "decodeBase64");
            lookup(KEYWORD, "div");
            lookup(KEYWORD, "dump");
            lookup(KEYWORD, "each");
            lookup(KEYWORD, "eachByte");
            lookup(KEYWORD, "eachFile");
            lookup(KEYWORD, "eachFileRecurse");
            lookup(KEYWORD, "eachLine");
            lookup(KEYWORD, "eachMatch");
            lookup(KEYWORD, "eachProperty");
            lookup(KEYWORD, "eachPropertyName");
            lookup(KEYWORD, "eachWithIndex");
            lookup(KEYWORD, "encodeBase64");
            lookup(KEYWORD, "every");
            lookup(KEYWORD, "execute");
            lookup(KEYWORD, "filterLine");
            lookup(KEYWORD, "find");
            lookup(KEYWORD, "findAll");
            lookup(KEYWORD, "findIndexOf");
            lookup(KEYWORD, "flatten");
            lookup(KEYWORD, "get");
            lookup(KEYWORD, "getAt");
            lookup(KEYWORD, "getErr");
            lookup(KEYWORD, "getIn");
            lookup(KEYWORD, "getLastMatcher");
            lookup(KEYWORD, "getOut");
            lookup(KEYWORD, "getText");
            lookup(KEYWORD, "grep");
            lookup(KEYWORD, "inject");
            lookup(KEYWORD, "inspect");
            lookup(KEYWORD, "intdiv");
            lookup(KEYWORD, "intersect");
            lookup(KEYWORD, "invokeMethod");
            lookup(KEYWORD, "isCase");
            lookup(KEYWORD, "join");
            lookup(KEYWORD, "leftShift");
            lookup(KEYWORD, "max");
            lookup(KEYWORD, "min");
            lookup(KEYWORD, "minus");
            lookup(KEYWORD, "mod");
            lookup(KEYWORD, "multiply");
            lookup(KEYWORD, "negate");
            lookup(KEYWORD, "newInputStream");
            lookup(KEYWORD, "newOutputStream");
            lookup(KEYWORD, "newPrintWriter");
            lookup(KEYWORD, "newReader");
            lookup(KEYWORD, "newWriter");
            lookup(KEYWORD, "next");
            lookup(KEYWORD, "or");
            lookup(KEYWORD, "padLeft");
            lookup(KEYWORD, "padRight");
            lookup(KEYWORD, "plus");
            lookup(KEYWORD, "pop");
            lookup(KEYWORD, "power");
            lookup(KEYWORD, "previous");
            lookup(KEYWORD, "print");
            lookup(KEYWORD, "println");
            lookup(KEYWORD, "putAt");
            lookup(KEYWORD, "readBytes");
            lookup(KEYWORD, "readLine");
            lookup(KEYWORD, "readLines");
            lookup(KEYWORD, "reverse");
            lookup(KEYWORD, "reverseEach");
            lookup(KEYWORD, "rightShift");
            lookup(KEYWORD, "rightShiftUnsigned");
            lookup(KEYWORD, "round");
            lookup(KEYWORD, "size");
            lookup(KEYWORD, "sort");
            lookup(KEYWORD, "splitEachLine");
            lookup(KEYWORD, "start");
            lookup(KEYWORD, "startDaemon");
            lookup(KEYWORD, "step");
            lookup(KEYWORD, "subMap");
            lookup(KEYWORD, "times");
            lookup(KEYWORD, "toCharacter");
            lookup(KEYWORD, "toDouble");
            lookup(KEYWORD, "toFloat");
            lookup(KEYWORD, "toInteger");
            lookup(KEYWORD, "toList");
            lookup(KEYWORD, "toLong");
            lookup(KEYWORD, "toURL");
            lookup(KEYWORD, "tokenize");
            lookup(KEYWORD, "transformChar");
            lookup(KEYWORD, "transformLine");
            lookup(KEYWORD, "upto");
            lookup(KEYWORD, "use");
            lookup(KEYWORD, "waitForOrKill");
            lookup(KEYWORD, "withInputStream");
            lookup(KEYWORD, "withOutputStream");
            lookup(KEYWORD, "withPrintWriter");
            lookup(KEYWORD, "withReader");
            lookup(KEYWORD, "withStream");
            lookup(KEYWORD, "withStreams");
            lookup(KEYWORD, "withWriter");
            lookup(KEYWORD, "withWriterAppend");
            lookup(KEYWORD, "write");
            lookup(KEYWORD, "writeLine");
        }
    }
}

