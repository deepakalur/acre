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
q *
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
package org.apache.tools.ant.gui.command;
import org.apache.tools.ant.gui.core.AppContext;
import org.apache.tools.ant.gui.event.ErrorEvent;
import org.apache.tools.ant.gui.acs.ACSProjectElement;
import org.apache.tools.ant.gui.acs.ACSTargetElement;
import org.apache.tools.ant.gui.event.ShowConsoleEvent;

/**
 * Starts an Ant build.
 *
 * @version $Revision: 1.1.1.1 $
 * @author Simeon Fitch
 */
public class BuildCmd extends AbstractCommand {

    /** Project to build. */
    private ACSProjectElement _project = null;
    /** Targets to build. */
    private ACSTargetElement[] _targets = null;

	/**
	 * Standard ctor.
	 *
	 */
    public BuildCmd(AppContext context) {
        super(context);
    }

    /**
     * Set the specific project to build (instead of the default).
     *
     * @param project Project to build.
     */
    public void setProject(ACSProjectElement project) {
        _project = project;
    }

    /**
     * Set the specific targets to build (instead of the default).
     *
     * @param targets Array of targets to build.
     */
    public void setTargets(ACSTargetElement[] targets) {
        _targets = targets;
    }

	/**
	 * Start the Ant build.
	 *
	 */
    public void run() {

        // Show the build console
        getContext().getEventBus().postEvent(
            new ShowConsoleEvent(getContext()));

        if(_project == null) {
            _project = getContext().getSelectionManager().getSelectedProject();
        }

        if(_targets == null) {
            _targets = getContext().getSelectionManager().getSelectedTargets();
        }

        if(_project != null) {
            try {
                getContext().getProjectManager().build(_project, _targets);
            }
            catch(Throwable ex) {
                getContext().getEventBus().postEvent(
                    new ErrorEvent(getContext(), ex));
            }
        }
    }
}