package org.erlide.backend;

import org.eclipse.core.resources.IProject;
import org.erlide.backend.api.BackendException;
import org.erlide.backend.api.IBackend;
import org.erlide.backend.api.IBackendManager;
import org.erlide.backend.runtimeinfo.RuntimeInfoPreferencesSerializer;
import org.erlide.runtime.api.RuntimeCore;
import org.erlide.runtime.epmd.EpmdWatcher;
import org.erlide.runtime.runtimeinfo.IRuntimeInfoCatalog;

public class BackendCore {

    private static IBackendManager backendManager;
    private static EpmdWatcher epmdWatcher;
    private static EpmdWatchJob epmdWatcherJob;

    public static void init(final IBackendManager aBackendManager) {
        backendManager = aBackendManager;
    }

    public static final IBackendManager getBackendManager() {
        return backendManager;
    }

    public static IRuntimeInfoCatalog getRuntimeInfoCatalog() {
        return RuntimeCore
                .getRuntimeInfoCatalog(new RuntimeInfoPreferencesSerializer());
    }

    public static IBackend getBuildOrIdeBackend(final IProject project) {
        final IBackendManager manager = getBackendManager();
        if (project != null) {
            try {
                return manager.getBuildBackend(project);
            } catch (final BackendException e) {
            }
        }
        return manager.getIdeBackend();
    }

    public static EpmdWatcher getEpmdWatcher() {
        if (epmdWatcher == null) {
            // tryStartEpmdProcess();
            startEpmdWatcher();
        }

        return epmdWatcher;
    }

    private static void startEpmdWatcher() {
        epmdWatcher = new EpmdWatcher();
        epmdWatcher.addEpmdListener(getBackendManager());
        epmdWatcherJob = new EpmdWatchJob(epmdWatcher);
        epmdWatcherJob.schedule(1000);
    }

    public static void stop() {
        if (epmdWatcherJob != null) {
            epmdWatcherJob.stop();
        }
    }

}
