package hawk

import android.app.IntentService
import android.content.Intent
import android.util.Log
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by AksCorp on 12.11.2017.
 */
class PostExceptionService : IntentService("PostExceptionService"){

    private val EXCEPTION_POST_URL = "http://10.0.2.2:3000/catcher/javaAndroid"

    /**
     * Post exception information to hawk server
     *
     * @param exceptionInfoJSON
     */
    private fun postException(exceptionInfoJSON: String) {
        try {
            var url = URL(EXCEPTION_POST_URL)
            var conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.addRequestProperty("Content-Type", "application/json;charset=UTF-8")
            conn.doOutput = true
            conn.doInput =true

            //Log.i("JSON", jsonParam.toString());
            var os = DataOutputStream(conn.outputStream)
            os.writeBytes(exceptionInfoJSON)

            os.flush()
            os.close()

            Log.i("STATUS", conn.responseCode.toString())
            Log.i("MSG", conn.responseMessage)

            conn.disconnect()
        } catch (e:Exception) {
            Log.e("Service info", e.toString())
        }
    }

    /**
     * Get post data and start post data to hawk server
     *
     * @param intent
     */
    override fun onHandleIntent(intent: Intent?) {
        var extras = intent?.extras
        var exceptionInfoJSON = extras?.getString("exceptionInfoJSON")
        postException(exceptionInfoJSON.toString())
    }


}