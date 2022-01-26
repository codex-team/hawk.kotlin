package so.hawk.catcher

import so.hawk.catcher.network.HawkClient
import java.util.concurrent.Future

/**
 * Post exception information to hawk server
 * @param client
 */
class PostExecutionService(private val client: HawkClient) {

    /**
     * Post exception information to hawk server
     *
     * @param exceptionInfoString
     */
    fun postEvent(exceptionInfoString: String): Future<*> {
        return HawkExecutorServiceFactory.create
            .submit {
                client.sendEvent(exceptionInfoString)
            }
    }
}
