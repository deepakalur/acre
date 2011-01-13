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

package org.acre.lang.tool;

import org.acre.lang.lexer.Lexer;
import org.acre.lang.lexer.LexerException;
import org.acre.lang.node.Node;
import org.acre.lang.parser.Parser;
import org.acre.lang.parser.ParserException;

import java.io.*;

/**
 * Generic parser utility API, Node builder. This class is not really a
 * builder (in terms of Builder pattern), while the result can be
 * collected directly from it. This builder will parse the input (string, stream)
 * and create Abstract Syntax Tree, via set of utility methods.
 *@author Mariusz Nowostawski
 */
public class TreeBuilder {

  public static Node getNode(final Reader r)
    throws IOException, LexerException, ParserException {
    return getNode(new PushbackReader(r));
  }

  public static Node getNode(final StringBuffer text)
    throws IOException, LexerException, ParserException {
    return getNode(text.toString());
  }

  public synchronized static Node getNode(final String text)
    throws IOException, LexerException, ParserException {
    return getNode(new PushbackReader
      (new BufferedReader
        (new StringReader(text))));
  }

  public synchronized static Node getNode(final PushbackReader r)
    throws IOException, LexerException, ParserException {
    Lexer lexer = new Lexer(r);
    Parser parser = new Parser(lexer);
    Node ast = parser.parse();
    return ast;
  }

} //TreeBuilder
