package com.online.meeting.app

import android.app.Application
import com.tunes.library.wrapper.network.TSHttpController

/**
 * 主App
 * @author ding
 * Created by Ding on 2018/8/16.
 */
class PMApplication : Application() {

    companion object {

        var instance : PMApplication? = null
    }

    override fun onCreate() {
        super.onCreate()

        instance = this@PMApplication
        TSHttpController.INSTANCE.setAppContext(this)
    }
}