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
package org.acre.lang.application;

import org.sablecc.sablecc.analysis.*;
import org.sablecc.sablecc.lexer.*;
import org.sablecc.sablecc.node.*;
import org.sablecc.sablecc.parser.*;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PushbackReader;

/**
 * A class to generate a nice HTML page from your SableCC source
 * grammar.
 *
 * @author Roger Keays <rogerk@ieee.org>
 *
 * Contributions (thanks!):
 *   Roger Peel - patch for missing </a> tags
 *
 * BUGS:
 * - non valid HTML characters such as '<' and '>' are not replaced
 * with their HTML equivalents.
 */
public class HtmlPrinter extends DepthFirstAdapter
{
	private String source;
	private PrintStream out;
	private boolean link;


	/**
	 * Creates a new HtmlPrinter using the given output printer, and
	 * giving the name of the source file.
	 *
	 * @param file the source file of the AST
	 * @param output the output printer
	 */
	public HtmlPrinter (String file, PrintStream output)
	{
		source = file;
		out = output;
		link = false;
	}


	/**
	 * Creates a new HtmlPrinter, telling it the source file of the
	 * parsed tree. Output is written to stdout.
	 *
	 * @param file the source file of the AST.
	 */
	public HtmlPrinter (String file)
	{
		source = file;
		out = System.out;
		link = false;
	}


	/**
	 * Creates a new HtmlPrinter, without the knowledge of which file
	 * the tree came from (presumably standard input).
	 */
	public HtmlPrinter ()
	{
		source = "PQL: Patterns Query Language";
		out = System.out;
		link = false;
	}

    public void inAGrammar(AGrammar node)
    {
		out.print
			("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 " +
			 " Transitional//EN\">\n" +
			 "<html>\n" +
			 "  <head>\n" +
			 "    <title>" + source + " (a SableCC grammar)</title>\n" +
			 "    <meta http-equiv=\"Content-Type\"" +
			 "          content=\"text/html; charset=utf-8\">" +
			 "    <style type=\"text/css\">\n" +
			 "      body {\n" +
			 "         font-family: Helvetica;\n" +
			 "      }\n" +
			 "      h1 {\n" +
			 "         text-align: center;\n" +
			 "      }\n" +
			 "      h2 {\n" +
			 "         text-align: center;\n" +
			 "         color: navy;\n" +
			 "      }\n" +
			 "      div {\n" +
			 "         margin-top: 20px;\n" +
			 "      }\n" +
			 "      a {\n" +
			 "         text-decoration: none;\n" +
			 "         color: black;\n" +
			 "      }\n" +
			 "      a:hover {\n " +
			 "         background-color: yellow;\n" +
			 "      }\n" +
			 "      .package {\n" +
			 "         background-color: ivory;\n" +
			 "         border: double;\n" +
			 "      }\n" +
			 "      .helpers {\n" +
			 "         background-color: linen;\n" +
			 "         border: double;\n" +
			 "      }\n" +
			 "      .states {\n" +
			 "         background-color: lightcyan;\n" +
			 "         border: double;\n" +
			 "      }\n" +
			 "      .tokens {\n" +
			 "         background-color: honeydew;\n" +
			 "         border: double;\n" +
			 "      }\n" +
			 "      .ignored-tokens {\n" +
			 "         background-color: lavender;\n" +
			 "         border: double;\n" +
			 "      }\n" +
			 "      .productions {\n" +
			 "         background-color: lightgoldenrodyellow;\n" +
			 "         border: double;\n" +
			 "      }\n" +
			 "      .ignored-alt {\n" +
			 "         font-style: italic;\n" +
			 "      }\n" +
			 "      .char {\n" +
			 "         color: red;\n" +
			 "      }\n" +
			 "      .dec-char {\n" +
			 "         color: blue;\n" +
			 "      }\n" +
			 "      .hex-char {\n" +
			 "         color: deeppink;\n" +
			 "      }\n" +
			 "      .string {\n" +
			 "         color: green;\n" +
			 "      }\n" +
			 "      .un-op {\n" +
			 "         color: goldenrod;\n" +
			 "         font-weight: bold;\n" +
			 "      }\n" +
			 "      .colname {\n" +
			 "         text-align: right;\n" +
			 "         vertical-align: top;\n" +
			 "         font-weight: bold;\n" +
			 "      }\n" +
			 "      .colequal {\n" +
			 "         vertical-align: top;\n" +
			 "      }\n" +
			 "      .coldata {\n" +
			 "      }\n" +
			 "      .colspare {\n" +
			 "         font-family: courier;\n" +
			 "      }\n" +
			 "      .elem-name {\n" +
			 "         color: blue;\n" +
			 "      }\n" +
			 "      .alt-name {\n" +
			 "         color: red;\n" +
			 "         font-weight: normal;\n" +
			 "      }\n" +
			 "    </style>\n" +
			 "  </head>\n" +
			 "  <body>\n" +
			 "    <h1>" + source + "</h1>\n");
    }

    public void outAGrammar(AGrammar node)
    {
		out.print
			("  </body>\n" +
			 "</html>\n");
    }

    public void inAPackage(APackage node)
    {
		out.print
			("    <div class=\"package\">\n" +
			 "    <h2>");
    }

	public void outAPackage(APackage node)
	{
		out.print
			("</h2>\n" +
			 "    </div>\n");
	}

    public void caseAHelpers(AHelpers node)
    {
		out.print
			("    <div class=\"helpers\">\n" +
			 "    <h2>");
		node.getHelpers().apply(this);
        out.print("</h2>\n" +
			 "      <table>\n" +
			 "        <col class=\"colname\">\n" +
			 "        <col class=\"colequal\">\n" +
			 "        <col class=\"coldata\">\n");

		Object temp[] = node.getHelperDefs().toArray();
		for(int i = 0; i < temp.length; i++) {
			((PHelperDef) temp[i]).apply(this);
		}
		out.print
			("      </table>\n" +
			 "    </div>\n");
    }

    public void caseAHelperDef(AHelperDef node)
    {
		out.print
			("        <tr>\n" +
			 "          <td><a name=\"" + node.getId().getText() +
			 "\" href=\"#" + node.getId().getText() + "\">");
		node.getId().apply(this);
		out.print("</a></td>\n" +
			 "          <td>");
		node.getEqual().apply(this);
		out.print("</td>\n" +
			 "          <td>");
		node.getRegExp().apply(this);
		out.print("</td>\n" +
			 "        </tr>\n");
    }

    public void caseAStates(AStates node)
    {
		out.print
			("    <div class=\"states\">\n" +
			 "    <h2>");
		node.getStates().apply(this);
		out.print ("</h2>\n");
		link = false;
		node.getIdList().apply(this);
		out.print
			("    </div>\n");
    }

    public void caseAIdList(AIdList node)
    {
		out.print
			("    <ul>\n" +
			 "      <li>");

		if (link == true) {
			out.print("<a href=\"#" + node.getId().getText() + "\">" +
					  node.getId().getText() + "</a> ");
		} else {
			node.getId().apply(this);
		}
        out.print("</li>\n");

		Object temp[] = node.getIds().toArray();
		for(int i = 0; i < temp.length; i++) {
			((PIdListTail) temp[i]).apply(this);
		}
		out.print
			("    </ul>\n");
    }

    public void caseAIdListTail(AIdListTail node)
    {
		out.print
			("      <li>");

		if (link == true) {
			out.print("<a href=\"#" + node.getId().getText() + "\">" +
					  node.getId().getText() + "</a> ");
		} else {
			node.getId().apply(this);
		}
		out.print("</li>\n");
    }

    public void caseATokens(ATokens node)
    {
		out.print
			("    <div class=\"tokens\">\n" +
			 "    <h2>");
		node.getTokens().apply(this);
		out.print("</h2>\n");

		out.print
			("    <table>\n" +
			 "      <col class=\"colspare\">\n" +
			 "      <col class=\"colname\">\n" +
			 "      <col class=\"colequal\">\n" +
			 "      <col class=\"coldata\">\n");

		Object temp[] = node.getTokenDefs().toArray();
		for(int i = 0; i < temp.length; i++) {
			((PTokenDef) temp[i]).apply(this);
		}

		out.print
			("    </table>\n" +
			 "    </div>\n");
    }

    public void caseATokenDef(ATokenDef node)
    {
		out.print
			("      <tr>\n" +
			 "        <td>");
        if(node.getStateList() != null)
        {
			node.getStateList().apply(this);
			out.print
				("</td>\n" +
				 "      </tr>\n" +
				 "      <tr>\n" +
				 "        <td>");
		}
		out.print
			("</td>\n" +
			 "        <td><a name=\"" + node.getId().getText() +
			 "\" href=\"#" + node.getId().getText() + "\">");
		node.getId().apply(this);
		out.print
			("</a></td>\n" +
			 "        <td>");
		node.getEqual().apply(this);
		out.print
			("</td>\n" +
			 "        <td>");
		node.getRegExp().apply(this);
		out.print
			("</td>\n" +
			 "      </tr>\n");
    }

    public void caseAIgnTokens(AIgnTokens node)
    {
		out.print
			("    <div class=\"ignored-tokens\">\n" +
			 "    <h2>");
		node.getIgnored().apply(this);
		node.getTokens().apply(this);
		out.print("</h2>\n");

		link = true;
		node.getIdList().apply(this);
		out.print
			("    </div>\n");
    }

	public void inAStringBasic(AStringBasic node)
	{
		out.print("<span class=\"string\">");
	}

	public void outAStringBasic(AStringBasic node)
	{
		out.print("</span>");
	}

	public void caseAIdBasic(AIdBasic node)
	{
		out.print("<a href=\"#" + node.getId().getText() + "\">" +
				  node.getId().getText() + "</a> ");
	}

    public void inACharChar(ACharChar node)
    {
		out.print("<span class=\"char\">");
    }

    public void outACharChar(ACharChar node)
    {
        out.print("</span>");
    }

    public void inADecChar(ADecChar node)
    {
		out.print("<span class=\"dec-char\">");
    }

    public void outADecChar(ADecChar node)
    {
        out.print("</span>");
    }

    public void inAHexChar(AHexChar node)
    {
		out.print("<span class=\"hex-char\">");
    }

    public void outAHexChar(AHexChar node)
    {
        out.print("</span>");
    }

    public void inAStarUnOp(AStarUnOp node)
    {
		out.print("<span class=\"un-op\">");
    }

    public void outAStarUnOp(AStarUnOp node)
    {
		out.print("</span>");
    }

    public void inAQMarkUnOp(AQMarkUnOp node)
    {
		out.print("<span class=\"un-op\">");
    }

    public void outAQMarkUnOp(AQMarkUnOp node)
    {
		out.print("</span>");
    }

    public void inAPlusUnOp(APlusUnOp node)
    {
		out.print("<span class=\"un-op\">");
    }

    public void outAPlusUnOp(APlusUnOp node)
    {
		out.print("</span>");
    }

    public void caseAProductions(AProductions node)
    {
		out.print
			("    <div class=\"productions\">\n" +
			 "    <h2>");
		node.getProductions().apply(this);
		out.print
			("</h2>\n" +
			 "      <table>\n" +
			 "        <col class=\"colname\">\n" +
			 "        <col class=\"colequal\">\n" +
			 "        <col class=\"coldata\">\n");

		Object temp[] = node.getProds().toArray();
		for(int i = 0; i < temp.length; i++) {
			((PProd) temp[i]).apply(this);
		}

		out.print
			("      </table>\n" +
			 "    </div>\n");

    }

    public void caseAProd(AProd node)
    {
		out.print
			("         <tr>\n" +
			 "           <td><a name=\"" + node.getId().getText() +
			 "\" href=\"#" + node.getId().getText() + "\">");
		node.getId().apply(this);
		out.print
			("</a></td>\n" +
			 "           <td>");
		node.getEqual().apply(this);
		out.print
			("</td>\n" +
			 "           <td>\n");
		node.getAlts().apply(this);
		out.print
			("           </td>\n" +
			 "         </tr>\n");
    }

    public void caseAAlts(AAlts node)
    {
		out.print
			("              <table class=\"alternatives\">\n" +
			 "                <col class=\"colequal\">\n" +
			 "                <col class=\"colname\">\n" +
			 "                <col class=\"coldata\">\n" +
			 "                <tr>\n" +
			 "                <td></td>");

		node.getAlt().apply(this);

		Object temp[] = node.getAlts().toArray();
		for(int i = 0; i < temp.length; i++) {
			((PAltsTail) temp[i]).apply(this);
		}

		out.print
			 ("             </table>\n");
    }

    public void caseAAltsTail(AAltsTail node)
    {
		out.print
			 ("                <tr>\n" +
			  "                  <td>");
		node.getBar().apply(this);
		out.print("</td>\n");

		node.getAlt().apply(this);
    }

    public void caseAParsedAlt(AParsedAlt node)
    {
		out.print
			 ("                 <td>");
        if(node.getAltName() != null) {
			node.getAltName().apply(this);
        }
		out.print
			("</td>\n" +
			 "                 <td>");

		Object temp[] = node.getElems().toArray();
		for(int i = 0; i < temp.length; i++) {
			((PElem) temp[i]).apply(this);
		}
		out.print
			("</td>\n" +
			 "                </tr>\n");
    }

    public void caseAIgnoredAlt(AIgnoredAlt node)
    {
		out.print
			("                 <td class=\"ignored-alt\">");

        if(node.getAltName() != null) {
			node.getAltName().apply(this);
        }
		out.print
			("</td>\n" +
			 "                 <td class=\"ignored-alt\">");

		Object temp[] = node.getElems().toArray();
		for(int i = 0; i < temp.length; i++) {
			((PElem) temp[i]).apply(this);
		}
		out.print
			("</td>\n" +
			 "                </tr>\n");
    }

    public void caseAElem(AElem node)
    {
        if(node.getElemName() != null)
        {
            node.getElemName().apply(this);
        }
        if(node.getSpecifier() != null)
        {
            node.getSpecifier().apply(this);
        }
		out.print("<a href=\"#" + node.getId().getText() + "\">" +
				  node.getId().getText() + "</a> ");
        if(node.getUnOp() != null)
        {
            node.getUnOp().apply(this);
        }
        outAElem(node);
    }

    public void caseAAltName(AAltName node)
    {
		out.print
			("<span class=\"alt-name\">" + node.getLBrace().getText() +
			 node.getId().getText() + node.getRBrace().getText() +
			 "</span>");
	}

    public void caseAElemName(AElemName node)
    {
		out.print
			("<span class=\"elem-name\">" + node.getLBkt().getText() +
			 node.getId().getText() + node.getRBkt().getText() +
			 node.getColon().getText() + "</span>");
    }

	/*
	 * by default, all tokens just print themselves out and some
	 * whitespace
	 */
	public void defaultCase(Node node)
	{
		if (node instanceof Token) {
			out.print(((Token) node).getText() + " ");
		}
	}

	public void caseTBar(TBar node)
	{
		out.print("<span class=\"un-op\">" + node.getText() +
				  "</span> ");
	}


	/* fire up the parser */
	public static void main(String [] args)
	{

		try {
			Parser p = new Parser
				(new Lexer
					(new PushbackReader
						(new InputStreamReader(System.in), 1024)));

			Start tree = p.parse();
			tree.apply(new HtmlPrinter());
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}
	}
}

