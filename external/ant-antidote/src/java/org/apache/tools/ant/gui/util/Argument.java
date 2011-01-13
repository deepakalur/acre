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

/** Holds an argument parsed from a command line.
 * If it's a plain argument, records the argument
 * string and position. If it is a dash-option,
 * records the option string (-a -b etc.) and
 * the argument to the option, if any. In any
 * case, records the position in the command
 * line that the arg or opt-arg pair came in.
 * @author $Author: deepakalur $
 * @version $Revision: 1.1.1.1 $
 * @see org.apache.tools.ant.gui.util.GetArgs
 */
public class Argument {

    /** The "option", that is, dash-letter, e.g., -a -b etc. */
    public String option;

    /** The argument to the option, e.g., "-o full" where
     * "full" is the argument to the option "-o".
     */
    public String argument;

    /** The position among the options-and-arguments in which
     * this option-and-argument appears.
     */
    public int position;

    /** Create an Argument from an option, argument and position.
     * @param option    The command-line option
     * @param argument  The command line arg
     * @param position  The nth-ity of the entity.
     */
    public Argument(String option, String argument, int position) {
	this.option   = option;
	this.argument = argument;
	this.position = position;
    }

    /** Return the option and argument as a String.
     * @return The string representation of the option and argument
     */
    public String toString()
    {return option + " " + argument;}

    /** Return the option portion (if any) of the Argument.
     * @return  The option itself.
     */
    public String getOption() {return option;}

    /** Return the argument portion (if any) of the Argument.
     * @return  The argument itself.
     */
    public String getArgument() {return argument;}
}						   /* End of Argument class*/

/* End of Argument.java */


