package com.dff.cordova.plugin.camera.actions;

import com.dff.cordova.plugin.camera.classes.json.JsonThrowable;
import com.dff.cordova.plugin.camera.dagger.annotations.PluginPermissions;
import com.dff.cordova.plugin.camera.helpers.PermissionHelper;
import com.dff.cordova.plugin.camera.log.Log;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

/**
 *  The PluginAction class provides general settings, logging and error handling for actions.
 *  A PluginAction  subclass has to implement the execute method.
 */
public abstract class PluginAction implements Runnable {
    public static final String TAG = "PluginAction";

    private String action;
    private JSONArray args;
    CallbackContext callbackContext;
    JSONObject jsonArgs = null;
    boolean needsArgs = false;
    boolean requiresPermissions = false;

    @Inject
    protected Log log;

    @Inject
    JsonThrowable jsonThrowable;

    @Inject
    PermissionHelper permissionHelper;

    @Inject
    @PluginPermissions
    String[] pluginPermissions;

    public String getAction() {
        return action;
    }

    public PluginAction setAction(String action) {
        this.action = action;

        return this;
    }

    private JSONArray getArgs() {
        return args;
    }

    public PluginAction setArgs(JSONArray args) {
        this.args = args;

        return this;
    }

    CallbackContext getCallbackContext() {
        return callbackContext;
    }

    public PluginAction setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;

        return this;
    }

    /**
     * Log a message which action is running and handle exceptions in general.
     */
    @Override
    public final void run() {
        log.i(TAG, String.format("running action: %s(%s)", action, getActionName()));
        log.d(TAG, String.format("running action args: %s", getArgs()));

        try {
            if (requiresPermissions && !hasPermissions()) {
                throw new IllegalStateException(String.format(
                    "required permissions %s not granted",
                    Arrays.toString(pluginPermissions)
                ));
            }

            if (needsArgs) {
                // expect object with arguments as first element in args
                if (args != null) {
                    jsonArgs = args.getJSONObject(0);
                }

                if (jsonArgs == null) {
                    throw new IllegalArgumentException("args missing");
                }
            }

            execute();
        } catch (Exception e) {
            log.e(TAG, e.getMessage(), e);

            try {
                callbackContext.error(jsonThrowable.toJson(e));
            } catch (JSONException e1) {
                log.e(TAG, e.getMessage(), e);
                callbackContext.error(e.getMessage());
            }
        }
    }

    /**
     * Called by run.
     *
     * Subclasses have to override this method and do their real work here.
     *
     * @throws Exception When actions execution has an error.
     */
    protected abstract void execute() throws Exception;

    public abstract String getActionName();

    /**
     * Check if required args are included in args.
     *
     * @param requiredArgs Names of required args
     */
    void checkJsonArgs(String[] requiredArgs) {
        if (requiredArgs != null) {
            for (String arg: requiredArgs) {
                if (! jsonArgs.has(arg)) {
                    throw new IllegalArgumentException(String.format("missing arg %s", arg));
                }
            }
        }
    }

    private boolean hasPermissions() {
        return permissionHelper.hasAllPermissions(pluginPermissions);
    }
}
