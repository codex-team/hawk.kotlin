package hawk

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by AksCorp on 12.11.2017.
 */
class HawkExceptionCatcher : Thread.UncaughtExceptionHandler {

    private lateinit var oldHandler: Thread.UncaughtExceptionHandler


    private var isActive = false
    private var HAWK_TOKEN = ""

    private var isPostStackEnable = true

    lateinit var context: Context

    /**
     * @param token - hawk initialization project token
     */
    constructor(context: Context, token: String) {
        HAWK_TOKEN = token
        this.context = context
    }

    /**
     * Set enable stack trace post
     *
     * @param isEnable
     */
    fun setEnableStackPost(isEnable: Boolean) {
        isPostStackEnable = isEnable
    }

    /**
     * Start listen uncaught exceptions
     *
     * @throws Exception
     */
    fun start() {
        try {
            oldHandler = Thread.getDefaultUncaughtExceptionHandler()
        } catch (e: Exception) {
            throw Exception("HawkExceptionCatcher start error. " + e.toString())
        }
        Thread.setDefaultUncaughtExceptionHandler(this)
        isActive = true
    }

    /**
     * Stop listen uncaught exceptions and set uncaught exception handler by default
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun finish() {
        if (!isActive())
            throw Exception("HawkExceptionCatcher not running")
        isActive = false
        Thread.setDefaultUncaughtExceptionHandler(oldHandler)
    }

    /**
     * Return current catcher status
     *
     * @return
     */
    fun isActive(): Boolean = isActive


    /**
     * Action when exception catch
     *
     * @param thread
     * @param throwable
     */
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        startExceptionPostService(formingJsonExceptionInfo(throwable).toString())
        oldHandler.uncaughtException(thread, throwable)
    }


    /**
     * Forming stack trace
     *
     * @param throwable
     * @return
     */
    private fun getStackTrace(throwable: Throwable): String {
        if (!isPostStackEnable)
            return "none"
        var result = ""
        var stackTraceElements = throwable.stackTrace
        for (item in stackTraceElements)
            result += item.toString() + "\n"
        return result
    }


    /**
     * Create json with exception and device information
     *
     * @param throwable
     * @return
     */
    private fun formingJsonExceptionInfo(throwable: Throwable): JSONObject {
        var jsonParam = JSONObject();
        var throwable = throwable?.cause
        try {
            jsonParam.put("token", HAWK_TOKEN);
            jsonParam.put("message", throwable.toString());
            jsonParam.put("stack", getStackTrace(throwable!!));
            jsonParam.put("brand", Build.BRAND);
            jsonParam.put("device", Build.DEVICE);
            jsonParam.put("model", Build.MODEL);
            jsonParam.put("product", Build.PRODUCT);
            jsonParam.put("SDK", Build.VERSION.SDK_INT);
            jsonParam.put("release", Build.VERSION.RELEASE);
        } catch (e: JSONException) {
            e.printStackTrace();
        }
        Log.d("Post json", jsonParam.toString());
        return jsonParam;
    }

    /**
     * Start service with post data
     *
     * @param exceptionInfoJSON
     */
    private fun startExceptionPostService(exceptionInfoJSON: String) {
        try {
            var extras = Bundle();
            extras.putString("exceptionInfoJSON", exceptionInfoJSON)

            var intent = Intent(context, PostExceptionService::class.java)
            intent.putExtras(extras)

            context.startService(intent);
        } catch (e: Exception) {
            Log.e("Hawk catcher", e.toString());
        }
    }

}
