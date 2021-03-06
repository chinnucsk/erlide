package org.erlide.ui.editors.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;
import org.erlide.model.ErlModelException;
import org.erlide.model.erlang.IErlModule;
import org.erlide.model.root.ErlModelManager;
import org.erlide.model.root.IErlElementLocator;
import org.erlide.model.root.IErlModel;
import org.erlide.model.util.ModelUtils;

public class ErlangExternalEditorInputFactory implements IElementFactory {

    public static final String ID = "org.erlide.ui.ErlangExternalEditorInputFactory";
    private static final String TAG_EXTERNAL_MODULE_PATH = "external_module_path";
    private static final String TAG_URI = "uri";

    public static void saveState(final IMemento memento,
            final ErlangExternalEditorInput input) {
        final IErlElementLocator model = ErlModelManager.getErlangModel();
        final String externalModulePath = ModelUtils.getExternalModulePath(
                model, input.getModule());
        memento.putString(TAG_EXTERNAL_MODULE_PATH, externalModulePath);
        final URI uri = input.getURI();
        memento.putString(TAG_URI, uri.toString());
    }

    @Override
    public IAdaptable createElement(final IMemento memento) {
        // Get the file name.
        final String externalModulePath = memento
                .getString(TAG_EXTERNAL_MODULE_PATH);
        if (externalModulePath == null) {
            return null;
        }
        IErlModule module;
        try {
            final IErlModel model = ErlModelManager.getErlangModel();
            module = ModelUtils.getModuleFromExternalModulePath(model,
                    externalModulePath);
        } catch (final ErlModelException e1) {
            return null;
        }
        if (module == null) {
            return null;
        }
        // Get the file name.
        final String uriString = memento.getString(TAG_URI);
        if (uriString == null) {
            return null;
        }

        URI uri;
        try {
            uri = new URI(uriString);
        } catch (final URISyntaxException e) {
            return null;
        }
        try {
            return new ErlangExternalEditorInput(EFS.getStore(uri), module);
        } catch (final CoreException e) {
            return null;
        }
    }
}
