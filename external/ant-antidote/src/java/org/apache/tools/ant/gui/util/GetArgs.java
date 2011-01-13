/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999, 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Ant" and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.tools.ant.gui.util;

import  java.io.*;
import  java.util.*;

/** Parses arguments and options from a string
 * e.g., from a command line. GetArgs views
 * the string as a series of blank-delimited
 * lexemes which it interprets as either plain
 * arguments or option-argument pairs as described
 * below.
 *
 *<p> GetArgs breaks up the passed-in string
 * into two lists, one of the plain arguments,
 * and one of the option-argument pairs.
 * GetArgs creates com.SoftWoehr.util.Argument
 * objects of each of these entities.
 *
 * <p>Each option is identified by a single character
 * preceded by an option introducer. Remaining characters
 * in such a lexeme are regarded as the argument to
 * the option, e.g.,
 * <pre>
 *		-Djava.foo=bar
 * </pre>
 * where -D is the option and java.foo=bar is the argument.
 *
 *<p>The option introducer is any individual character
 * in the string returned by <code>getOptionIntroducers()</code>,
 * by default the sole character '-' "dash". This string of
 * option introducers may be changed by
 * <code>setOptionIntroducers()</code>.
 *
 * <p><b>NOTE</b> that all options are presumed to have option
 * arguments, so if the lexeme recognized as an option consists
 * only of the option introducer or an option introducer and
 * one (1) following option character, the next lexeme
 * in the command line following the option is regarded as
 * the option argument <b>UNLESS</b> that next lexeme is itself
 * an option, i.e., a string headed by a member of the current
 * set of option introducers.
 *
 *<p>The position in which the command line argument or
 * option-argument pair occurred is recorded in the Argument
 * object also.
 *
 *<p>Derived from com.SoftWoehr.util.GetArgs by the author.
 * 
 * @author $Author: deepakalur $
 * @version $Revision: 1.1.1.1 $
 * 
 */
public class GetArgs {
    
    /** Holds the Argument objects, as many as parsed. */
    private Vector optList, argList;
    
    /** Option introducers. */
    private String optionIntroducers = "-";
    
    /** Arity/1 constructor. The arity/0 exists uselessly.
     * If you must use GetArgs/0 be sure to call reinit/1.
     * @param argv Arg string
     */
    public GetArgs(String argv[]) { reinit(argv); }
    
    /** Reinitialize the object, discarding previous state.
     * Creates two arrays, one of options and their
     * (possibly null) arguments, the other of plain arguments.
     * The members of these lists are accessible via other methods.
     * @param argv Arg string
     */
    public void reinit(String argv[]) {
        int i;
        
        optList = new Vector();
        argList = new Vector();
        
        String tempOpt;    /* A potential option while we're processing it.*/
        String theOpt;   /* The option marker_char + opt_letter, e.g., -x .*/
        String tempArg;		       /* A potential argument.		   */
        
        int position = 0;	       /* nthness in line		   */
        
        for (i = 0; i < argv.length; i++) {
            tempOpt = argv[i].trim();
            if (isOptionIntroducer(tempOpt.charAt(0)))      /* Is this an option?*/ {
                theOpt =/* Record option, introducer plus second char, if any.*/
                tempOpt.substring(0,Math.min(2, tempOpt.length()));
                
                if (tempOpt.length() > 2)    /* Is the optarg in the option itself?*/ {/* If so, extract that option argument.*/
                    tempArg = tempOpt.substring(2, tempOpt.length());
                }
                else		 /* No, optarg not included directly in opt string.*/ {/* Look for it in next lexical element*/
                    if ((i+1) < argv.length)     /* Do we have another lex elem left?*/ {
                        tempArg = argv[i+1].trim();/* Next lex an option on its own?*/
                        if (isOptionIntroducer(tempArg.charAt(0))) {
                            tempArg = null;/* Yup, so previous option is null-arged.*/
                        }
                        else /* No, it's not an option, so must be arg to previous opt.*/ {/* (We already read it into tempArg.)*/
                            i++;   /* Bump index past this lexical element.*/
                        }					  /* End if*/
                    }
                    else				/* Command line is exhausted.*/ {
                        tempArg = null;		      /* No arg to the opt.*/
                    }						  /* End if*/
                }  /*  Done looking for argument to option. */    /* End if*/
                
        /* We can now store our option and its argument (if any). */
                optList.addElement(new Argument(theOpt, tempArg, position));
            }  /* Done processing found option. */		  /* End if*/
            
            else	      /* Wasn't an option, must be just a plain argument.*/ {
                tempArg = tempOpt;		/* Already have it in hand.*/
                tempOpt = null;				       /* No option*/
        /* Add to list of plain arguments */
                argList.addElement(new Argument(tempOpt, tempArg, position));
            }							  /* End if*/
            position++;
        }  /* Done looping through string array of command line. *//* End for*/
    }						      /* End of constructor*/
    
    /** Return a string of all the options and arguments,
     * options first, then arguments, but otherwise in order.
     * @return string rep
     */
    public String toString() {
        Argument a;
        String result = "";
        
    /* Iterate through the option and argument lists */
        for (int i = 0; i < optList.size() ; i++) {
            a = nthOption(i);
            result += "(" + a.position + ") ";
            
            if (null != a.option) {
                result += a.option + " ";
            }							  /* End if*/
            if (null != a.argument) {
                result += a.argument + " ";
            }
        }							 /* End for*/
        
        for (int i = 0; i < argList.size() ; i++) {
            a = nthArgument(i);
            result += "(" + a.position + ") ";
            
            if (null != a.option) {
                result += a.option + " ";
            }							  /* End if*/
            if (null != a.argument) {
                result += a.argument + " ";
            }
        }							 /* End for*/
        
        return result;
    }
     
    /** Return string of option introducers.
     * @return string of option introducers.
     */
    public String getOptionIntroducers() { return optionIntroducers; }
    
    /** Set string of single-character option introducers.
     * Any individual char in the list will be considered an intro
     * to an option on the command line.
     * @param s string of single-character option introducers.
     */
    public void setOptionIntroducers(String s ) { optionIntroducers = s; }
    
    /** Is the given char found in the string of option introducers?
     * @param c char to test
     * @return <CODE>true</CODE> if option introdcer.
     */
    public boolean isOptionIntroducer(char c) {
        int found = optionIntroducers.indexOf(c);
        return (found != -1);
    }
    
    /** Return the nth option as an Argument object.
     * Returns null if no such nth option.
     * @param n nth 0-based opt to find
     * @return the sought option or <CODE>null</CODE>.
     */
    public Argument nthOption(int n) {
        Argument a = null;
        if (n < optList.size()) {
            a = (Argument) (optList.elementAt(n));
        }
        return a;
    }
    
    /** Returns nth argument as Argument object.
     * Returns null of no such nth argument.
     * @param n nth 0-based argument
     * @return sought Argument or <CODE>null</CODE>.
     */
    public Argument nthArgument(int n) {
        Argument a = null;
        if (n < argList.size()) {
            a = (Argument) (argList.elementAt(n));
        }
        return a;
    }
    
    /** Number of options parsed.
     * @return num opts
     */
    public int optionCount() {
        return optList.size();
    }
    
    /** Number of plain arguments parsed.
     * @return num args
     */
    public int argumentCount() {
        return argList.size();
    }
 
    /** Demo GetArgs by displaying any opts or args passed in.
     * @param argv Args to use to test the GetArgs
     */
    public static void main(String argv[]) {
        
        int i;
        Argument a;
        GetArgs g = new GetArgs(argv);
        boolean quitFlag = false;
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        StringTokenizer st = new StringTokenizer("");/* Holds next pass of input.*/
        String ss[];		       /* Holds next pass of input.	   */
        
    /* GPL'ed SoftWoehr announces itself. */
        System.out.println("GetArgs, Copyright (c) 1999, 2003 The Apache Software Foundation.  All rights reserved.");
        
        if (0 == argv.length) {
            System.err.println("Usage: GetArgs [-options args args -options args ...]");
            System.err.println(" ... Just analyzes the options, but there are two special");
            System.err.println(" ... options, -o and -q. -q quits. -o takes its argument");
            System.err.println(" ... and makes it the option introducers string.");
            return;
        }							  /* End if*/
        
    /* Loop taking arguments. */
        while (!quitFlag) {
            System.out.println("Entire command line, \"normalized\":\n");
            System.out.println(g.toString() + "\n");
            System.out.println("Options:");
            System.out.println("--------");
            for (i = 0; i < g.optionCount() ; i++) {
                a =  g.nthOption(i);
                System.out.println("  option   is " + a.option  );
                System.out.println("  argument is " + a.argument);
                System.out.println("  position is " + a.position);
                System.out.println("--------");
                
        /* See if user wants to quit. */
                if (a.option.length() > 1) {
                    if (a.option.substring(1,2).equals("q")) {
                        quitFlag = true;
                    }						  /* End if*/
                }
                
        /* See if user wants to change option string. */
                if (a.option.length() > 1 && a.option.substring(1,2).equals("o")) {
                    if (a.argument != null) {
                        g.setOptionIntroducers(a.argument);
                    }						  /* End if*/
                }						  /* End if*/
            }							 /* End for*/
            
      /* Now show the arguments. */
            System.out.println("Arguments");
            System.out.println("---------");
            for (i = 0; i < g.argumentCount() ; i++) {
                a =  g.nthArgument(i);
                System.out.println("  argument is " + a.argument);
                System.out.println("  position is " + a.position);
                System.out.println("---------");
            }							 /* End for*/
            
      /* Get another line from user if we're not done. */
            if (!quitFlag) {
        /* Get a new line. */
                try {
                    st = new StringTokenizer(br.readLine());
                }						 /* End try*/
                catch (Exception e) {
                    e.printStackTrace(System.err);
                }					       /* End catch*/
        /* Process the line/ */
                ss = new String [st.countTokens()];
                for (int j = 0; j < ss.length; j++) {
                    ss[j] = st.nextToken();
                }
                g.reinit(ss);			       /* arg-ize new input*/
            }							  /* End if*/
        }						       /* End while*/
    /* We're done, clean up. */
        try {
            br.close();
        }							 /* End try*/
        catch (Exception e) {
            e.printStackTrace(System.err);
        }						       /* End catch*/
    }							     /* End of main*/
}						    /* End of GetArgs class*/

/* End of GetArgs.java */


