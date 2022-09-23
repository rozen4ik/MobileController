package ru.ertel.mobilecontroller.util.interfaces

import android.content.Intent

interface NfcToOperation {
    /**
     * Method executed asynchronously, do **NOT** execute any UI logic in here! Funky stuff may occur
     */
    fun executeWriteOperation()

    /**
     * Method executed asynchronously, do **NOT** execute any UI logic in here ! Funky stuff may occur
     * @param intent to be passed to the write utility
     * @param args to be passed to the method
     */
    fun executeWriteOperation(intent: Intent?, vararg args: Any?)
}