package com.syncano.library.callbacks;

import com.syncano.library.api.Response;
import com.syncano.library.api.HttpRequest;

/**
 * Communicates responses from Syncano. Only one method will be
 * invoked in response to a given request.
 * <p/>
 * Callback methods are executed using the {@link HttpRequest} callback executor. When none is
 * specified, the following defaults are used:
 * <ul>
 * <li>Android: Callbacks are executed on the application's main (UI) thread.</li>
 * <li>JVM: Callbacks are executed on the background thread which performed the request.</li>
 * </ul>
 *
 * @param <T> Response body type.
 */
public interface SyncanoCallback<T> {

    void success(Response<T> response, T result);

    void failure(Response<T> response);
}
