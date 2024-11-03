package com.cmex.mycar.screen

import android.app.Application
import com.cmex.mycar.data.MainDb


class MainAppMyCar:Application() {
    val db by lazy { MainDb.getDataBase(this) }
}