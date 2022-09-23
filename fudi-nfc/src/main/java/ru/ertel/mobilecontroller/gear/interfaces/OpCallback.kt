package ru.ertel.mobilecontroller.gear.interfaces

import android.nfc.FormatException
import ru.ertel.mobilecontroller.util.exceptions.InsufficientCapacityException
import ru.ertel.mobilecontroller.util.exceptions.ReadOnlyTagException
import ru.ertel.mobilecontroller.util.exceptions.TagNotPresentException
import ru.ertel.mobilecontroller.util.interfaces.NfcWriteUtility

interface OpCallback {
    /**
     * Executed on background thread, do NOT perform any UI logic here !
     * @return
     */
    @Throws(
        ReadOnlyTagException::class,
        InsufficientCapacityException::class,
        TagNotPresentException::class,
        FormatException::class
    )
    fun performWrite(writeUtility: NfcWriteUtility?): Boolean
}