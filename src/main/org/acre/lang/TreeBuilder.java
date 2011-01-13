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
package org.acre.lang;

import org.acre.lang.lexer.Lexer;
import org.acre.lang.lexer.LexerException;
import org.acre.lang.node.Node;
import org.acre.lang.parser.Parser;
import org.acre.lang.parser.ParserException;
import org.acre.lang.pql.pdbc.PQLException;

import java.io.*;

/**
 * Generic parser API for OQL input. This class is not really a
 * builder (in terms of Builder pattern), while the result can be
 * collected directly from it. This builder will parse the input string 
 * and create Abstract Syntax Tree, via set of utility methods.
 *
 *@author Mariusz Nowostawski
 *@version @version@ $Revision: 1.1.1.1 $
 */
public class TreeBuilder {

  public static Node getNode(final Reader r, boolean simplified)
    throws IOException, LexerException, ParserException {
    return getNode(new PushbackReader(r,10000), simplified);
  }
  
  public static Node getNode(final StringBuffer text, boolean simplified) 
    throws IOException, LexerException, ParserException {
    return getNode(text.toString(), simplified);
  }

  public static Node getNode(final String text)
    throws IOException, LexerException, ParserException {
    return getNode(text, true);
  }
  public static Node getNode(final String text, boolean simplified) 
    throws IOException, LexerException, ParserException {

      QueryPreProcessor preProcessor = new QueryPreProcessor();
      String processedQuery;
      try {
          processedQuery = preProcessor.compile(text);
      } catch (PQLException e) {
          throw new ParserException(null, e.getMessage());
      }
      return getNode(new PushbackReader
      (new BufferedReader
        (new StringReader(processedQuery))), simplified);
  }
  
  public static Node getNode(final PushbackReader r, boolean simplified) 
    throws IOException, LexerException, ParserException {
    Lexer lexer = new Lexer(r);
    Parser parser = new Parser(lexer);
    Node ast = parser.parse();
    if(simplified) SimpleAstWalker.simplify(ast);
    return ast;
  }
  
} //TreeBuilder
/////////////// end of file /////////////////
