package com.base.hamoud.chronictrack

import android.app.Application
import com.base.hamoud.chronictrack.data.TokesDatabase
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber


class ChronicTrackerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)
        forceInitDatabase()
        initLogger()
    }

    /**
     * Initialize Logger with Timber integration for better and prettier logs.
     *
     * @see <a href="https://github.com/orhanobut/logger">Logger</a>
     * @see <a href="https://github.com/JakeWharton/timber">Timber</a>
     */
    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)// (Optional) Whether to show thread info or not. Default true
            .methodCount(1)// (Optional) How many method line to show. Default 2
            .tag("PRTY_LOG")// (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })

        Timber.plant(object : Timber.DebugTree() {

            override fun createStackElementTag(element: StackTraceElement): String? {
                // clickable link in logs
                return String.format(
                    "(%s:%s)#%s",
                    element.fileName,
                    element.lineNumber,
                    element.methodName
                )
            }

            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                // log with Logger
                Logger.log(priority, tag, message, t)
            }
        })
    }

    /**
     * Force the Room database to create it's tables.
     */
    private fun forceInitDatabase() {
        GlobalScope.launch(Dispatchers.IO) {
            TokesDatabase.getInstance(applicationContext).beginTransaction()
            TokesDatabase.getInstance(applicationContext).endTransaction()
        }
    }

}