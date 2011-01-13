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

public interface TokenTypes {
    public static final int UNRECOGNIZED = 0,
    WHITESPACE = 1,
    WORD = 2,
    NUMBER = 3,
    PUNCTUATION = 4,
    COMMENT = 5,
    START_COMMENT = 6,
    MID_COMMENT = 7,
    END_COMMENT = 8,
    TAG = 9,
    END_TAG = 10,
    KEYWORD = 11,
    KEYWORD2 = 12,
    IDENTIFIER = 13,
    LITERAL = 14,
    STRING = 15,
    CHARACTER = 16,
    OPERATOR = 17,
    BRACKET = 18,
    SEPARATOR = 19,
    URL = 20;

    /**
     * The names of the token types, indexed by type, are provided for
     * descriptive purposes.
     */
    public static final String[] typeNames =
            {
                "bad token",
                "whitespace",
                "word",
                "number",
                "punctuation",
                "comment",
                "start of comment",
                "middle of comment",
                "end of comment",
                "tag",
                "end tag",
                "keyword",
                "keyword 2",
                "identifier",
                "literal",
                "string",
                "character",
                "operator",
                "bracket",
                "separator",
                "url"};
}
