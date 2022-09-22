package ru.ertel.mobilecotroller.gear.interfaces

import android.nfc.FormatException
import ru.ertel.mobilecotroller.util.exceptions.InsufficientCapacityException
import ru.ertel.mobilecotroller.util.exceptions.ReadOnlyTagException
import ru.ertel.mobilecotroller.util.exceptions.TagNotPresentException
import ru.ertel.mobilecotroller.util.interfaces.NfcWriteUtility

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