package org.erlide.wrangler.refactoring.core.internal;

import org.erlide.model.ErlModelException;
import org.erlide.model.root.ErlModelManager;
import org.erlide.model.root.IErlProject;
import org.erlide.model.util.ModelUtils;
import org.erlide.runtime.rpc.RpcResult;
import org.erlide.wrangler.refactoring.backend.internal.WranglerBackendManager;

/**
 * Class for common functionalities of adhoc refactorings ad hoc specific
 * methods should delegate to if
 * 
 * @author Aleksandra Lipiec <aleksandra.lipiec@erlang-solutions.com>
 * @version %I%, %G%
 */
public class UserAdhocRefactoring {

    private final UserRefactoring refac; // base refactoring

    public UserAdhocRefactoring(final UserRefactoring refac) {
        this.refac = refac;
    }

    /**
     * Loading user's callback module
     * 
     * @return
     */
    public boolean load() {
        String callbackPath;
        try {
            if (ErlModelManager.getErlangModel().findModule(
                    refac.getCallbackModule()) == null) {
                return false;
            }

            final IErlProject project = ModelUtils.getProject(ErlModelManager
                    .getErlangModel().findModule(refac.getCallbackModule()));
            callbackPath = project.getWorkspaceProject().getLocation()
                    .append(project.getOutputLocation()).toString();
        } catch (final ErlModelException e) {
            return false;
        }

        final RpcResult res = WranglerBackendManager.getRefactoringBackend()
                .callWithoutParser("load_callback_mod_eclipse", "ss",
                        refac.getCallbackModule(), callbackPath);
        if (!res.isOk()) {
            return false;
        }
        return true;
    }

}
