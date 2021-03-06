/*******************************************************************************
 * Copyright (c) 2005 Vlad Dumitrescu and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Vlad Dumitrescu
 *******************************************************************************/
package org.erlide.ui.util;

import org.eclipse.core.resources.IEncodedStorage;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.PartInitException;
import org.erlide.backend.api.BackendException;
import org.erlide.model.erlang.IErlFunction;
import org.erlide.model.erlang.IErlModule;
import org.erlide.model.erlang.IErlTypespec;
import org.erlide.model.erlang.ISourceRange;
import org.erlide.model.root.ErlModelManager;
import org.erlide.model.root.IErlElement;
import org.erlide.model.root.IErlElementLocator;
import org.erlide.model.root.IErlModel;
import org.erlide.model.root.IErlProject;
import org.erlide.model.util.ErlangFunction;
import org.erlide.model.util.ModelUtils;
import org.erlide.ui.editors.erl.AbstractErlangEditor;
import org.erlide.ui.editors.util.EditorUtility;
import org.erlide.ui.editors.util.ErlangExternalEditorInput;

public class ErlModelUtils {

    /**
     * @param moduleName
     * @param function
     * @param modulePath
     * @param module
     * @param project
     * @param scope
     * @return
     * @throws CoreException
     */
    public static boolean openExternalFunction(final String moduleName,
            final ErlangFunction function, final String modulePath,
            final IErlModule module, final IErlProject project,
            final IErlElementLocator.Scope scope) throws CoreException {
        final IErlElementLocator model = ErlModelManager.getErlangModel();
        final IErlModule module2 = ModelUtils.findModule(model, project,
                moduleName, modulePath, scope);
        if (module2 != null) {
            final IEditorPart editor = EditorUtility.openInEditor(module2);
            return openFunctionInEditor(function, editor);
        }
        return false;
    }

    public static void openElement(final IErlElement element)
            throws PartInitException {
        final IEditorPart editor = EditorUtility.openInEditor(element);
        EditorUtility.revealInEditor(editor, element);
    }

    public static void openSourceRange(final IErlModule module,
            final ISourceRange sourceRange) throws PartInitException {
        final IEditorPart editor = EditorUtility.openInEditor(module);
        EditorUtility.revealInEditor(editor, sourceRange);
    }

    /**
     * Activate editor and select erlang function
     * 
     * @param fun
     * @param arity
     * @param editor
     * @throws CoreException
     */
    public static boolean openFunctionInEditor(
            final ErlangFunction erlangFunction, final IEditorPart editor)
            throws CoreException {
        final AbstractErlangEditor erlangEditor = (AbstractErlangEditor) editor;
        final IErlModule module = erlangEditor.getModule();
        if (module == null) {
            return false;
        }
        module.open(null);
        final IErlFunction function = module.findFunction(erlangFunction);
        if (function == null) {
            return false;
        }
        EditorUtility.revealInEditor(editor, function);
        return true;
    }

    public static boolean openTypeInEditor(final String typeName,
            final IEditorPart editor) throws CoreException, BackendException {
        final AbstractErlangEditor erlangEditor = (AbstractErlangEditor) editor;
        final IErlModule module = erlangEditor.getModule();
        if (module == null) {
            return false;
        }
        module.open(null);
        final IErlTypespec typespec = ModelUtils.findTypespec(module, typeName);
        if (typespec == null) {
            return false;
        }
        EditorUtility.revealInEditor(editor, typespec);
        return true;
    }

    public static IErlModule getModule(final IEditorInput editorInput)
            throws CoreException {
        if (editorInput instanceof IFileEditorInput) {
            final IFileEditorInput input = (IFileEditorInput) editorInput;
            final IFile file = input.getFile();
            final IErlModel model = ErlModelManager.getErlangModel();
            IErlModule module = model.findModule(file);
            if (module != null) {
                return module;
            }
            final String path = file.getLocation().toPortableString();
            // TODO shouldn't we use the resource directly below?
            module = model.getModuleFromFile(model, file.getName(), path,
                    file.getCharset(), path);
            module.setResource(file);
            return module;
        }
        if (editorInput instanceof ErlangExternalEditorInput) {
            final ErlangExternalEditorInput erlangExternalEditorInput = (ErlangExternalEditorInput) editorInput;
            return erlangExternalEditorInput.getModule();
        }
        String path = null;
        String encoding = null;
        if (editorInput instanceof IStorageEditorInput) {
            final IStorageEditorInput sei = (IStorageEditorInput) editorInput;
            try {
                final IStorage storage = sei.getStorage();
                final IPath p = storage.getFullPath();
                path = p.toPortableString();
                if (storage instanceof IEncodedStorage) {
                    final IEncodedStorage encodedStorage = (IEncodedStorage) storage;
                    encoding = encodedStorage.getCharset();
                } else {
                    encoding = ResourcesPlugin.getEncoding();
                }
            } catch (final CoreException e) {
            }
        }
        if (editorInput instanceof IURIEditorInput) {
            final IURIEditorInput ue = (IURIEditorInput) editorInput;
            path = ue.getURI().getPath();
        }
        if (path != null) {
            final IErlElementLocator model = ErlModelManager.getErlangModel();
            final IErlModule module = ModelUtils.findModule(model, null, null,
                    path, IErlElementLocator.Scope.ALL_PROJECTS);
            if (module != null) {
                return module;
            }
            final IPath p = new Path(path);
            return ErlModelManager.getErlangModel().getModuleFromFile(null,
                    p.lastSegment(), path, encoding, path);
        }
        return null;
    }

    public static void openMFA(final String module, final String function,
            final int arity) throws CoreException {
        ErlModelUtils.openExternalFunction(module, new ErlangFunction(function,
                arity), null,
                ErlModelManager.getErlangModel().findModule(module), null,
                IErlElementLocator.Scope.ALL_PROJECTS);
    }

    public static void openMF(final String module, final String function)
            throws CoreException {
        openMFA(module, function, ErlangFunction.ANY_ARITY);
    }

    public static void openModule(final String moduleName) throws CoreException {
        final IErlElementLocator model = ErlModelManager.getErlangModel();
        final IErlModule module = ModelUtils.findModule(model, null,
                moduleName, null, IErlElementLocator.Scope.ALL_PROJECTS);
        if (module != null) {
            EditorUtility.openInEditor(module);
        }
    }

}
