/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.erlide.model.root;

/**
 * Markers used by the Erlang model.
 * <p>
 * This interface declares constants only; it is not intended to be implemented
 * or extended.
 * </p>
 */
public interface IErlModelMarker {

    /**
     * Erlang model problem marker type (value
     * <code>"org.erlide.model.erlang.problem"</code>). This can be used to
     * recognize those markers in the workspace that flag problems detected by
     * the Erlang tooling during compilation.
     */
    String ERLANG_MODEL_PROBLEM_MARKER = "org.erlide.model" + ".problem"; //$NON-NLS-1$

    /**
     * Erlang model transient problem marker type (value
     * <code>"org.erlide.model.erlang.transient_problem"</code>). This can be
     * used to recognize those markers in the workspace that flag transient
     * problems detected by the Erlang tooling (such as a problem detected by
     * the outliner, or a problem detected during a code completion)
     */
    String TRANSIENT_PROBLEM = "org.erlide.model" + ".transient_problem"; //$NON-NLS-1$

    /**
     * Erlang model task marker type (value
     * <code>"org.erlide.model.erlang.task"</code>). This can be used to
     * recognize task markers in the workspace that correspond to tasks
     * specified in Erlang source comments and detected during compilation (for
     * example, 'TO-DO: ...'). Tasks are identified by a task tag, which can be
     * customized through <code>ErlangCore</code> option
     * <code>"org.erlide.model.erlang.compiler.taskTag"</code>.
     * 
     * 
     */
    String TASK_MARKER = "org.erlide.model" + ".task"; //$NON-NLS-1$

    /**
     * Id marker attribute (value <code>"arguments"</code>). Arguments are
     * concatenated into one String, prefixed with an argument count (followed
     * with colon separator) and separated with '#' characters. For example: {
     * "foo", "bar" } is encoded as "2:foo#bar", { } is encoded as "0: "
     * 
     * 
     */
    String ARGUMENTS = "arguments"; //$NON-NLS-1$

    /**
     * Id marker attribute (value <code>"id"</code>).
     */
    String ID = "id"; //$NON-NLS-1$

    /**
     * Flags marker attribute (value <code>"flags"</code>). Reserved for future
     * use.
     */
    String FLAGS = "flags"; //$NON-NLS-1$

    /**
     * Build path problem marker type (value
     * <code>"org.erlide.model.erlang.buildpath_problem"</code>). This can be
     * used to recognize those markers in the workspace that flag problems
     * detected by the Erlang tooling during classpath setting.
     */
    String BUILDPATH_PROBLEM_MARKER = "org.erlide.model" + ".buildpath_problem"; //$NON-NLS-1$

    /**
     * Path entry problem marker type (value
     * <code>"org.erlide.model.erlang.pathentry_problem"</code>). This can be
     * used to recognize those markers in the workspace that flag problems
     * detected by the Erlang tooling during classpath setting.
     */
    String PATHENTRY_PROBLEM_MARKER = "org.erlide.model" + ".pathentry_problem"; //$NON-NLS-1$

    /**
     * PathEntry file format marker attribute (value
     * <code>"PathEntryFileFormat"</code>). Used only on path entry store
     * problem markers. The value of this attribute is either "true" or "false".
     * 
     */
    String PATHENTRY_FILE_FORMAT = "pathEntryFileFormat"; //$NON-NLS-1$
}
