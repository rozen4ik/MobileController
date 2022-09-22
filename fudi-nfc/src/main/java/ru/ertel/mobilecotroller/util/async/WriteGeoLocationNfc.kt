package ru.ertel.mobilecotroller.util.async

import android.content.Intent
import android.nfc.FormatException
import ru.ertel.mobilecotroller.gear.interfaces.OpCallback
import ru.ertel.mobilecotroller.gear.interfaces.TaskCallback
import ru.ertel.mobilecotroller.util.exceptions.InsufficientCapacityException
import ru.ertel.mobilecotroller.util.exceptions.ReadOnlyTagException
import ru.ertel.mobilecotroller.util.exceptions.TagNotPresentException
import ru.ertel.mobilecotroller.util.interfaces.NfcWriteUtility

class WriteGeoLocationNfc : Nfc {
    /**
     * Instantiates a new WriteGeoLocationNfc.
     *
     * @param taskCallback the async ui callback
     */
    constructor(taskCallback: TaskCallback?) : super(taskCallback) {}

    /**
     * Instantiates a new WriteGeoLocationNfc.
     *
     * @param taskCallback the async ui callback
     * @param opCallback the async operation callback
     */
    constructor(taskCallback: TaskCallback?, opCallback: OpCallback) : super(
        taskCallback,
        opCallback
    ) {
    }

    /**
     * Instantiates a new WriteGeoLocationNfc.
     *
     * @param taskCallback the async ui callback
     * @param opCallback the async operation callback
     * @param nfcWriteUtility the nfc write utility
     */
    constructor(
        taskCallback: TaskCallback?,
        opCallback: OpCallback,
        nfcWriteUtility: NfcWriteUtility
    ) : super(taskCallback, opCallback, nfcWriteUtility) {
    }

    override fun executeWriteOperation(intent: Intent?, vararg args: Any?) {
        if (checkDoubleArguments(args.javaClass) || args.size != 2 || intent == null) {
            throw UnsupportedOperationException("Invalid arguments")
        }
        asyncOperationCallback = object : OpCallback {
            @Throws(
                ReadOnlyTagException::class,
                InsufficientCapacityException::class,
                TagNotPresentException::class,
                FormatException::class
            )
            override fun performWrite(writeUtility: NfcWriteUtility?) = (writeUtility?.apply {
                    writeGeolocationToTagFromIntent(args[0] as Double, args[1] as Double, intent)
                } ?: false) as Boolean
        }
        super.executeWriteOperation()
    }
}