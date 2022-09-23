package ru.ertel.mobilecontroller.util.async

import android.content.Intent
import android.nfc.FormatException
import ru.ertel.mobilecontroller.gear.interfaces.OpCallback
import ru.ertel.mobilecontroller.gear.interfaces.TaskCallback
import ru.ertel.mobilecontroller.util.exceptions.InsufficientCapacityException
import ru.ertel.mobilecontroller.util.exceptions.ReadOnlyTagException
import ru.ertel.mobilecontroller.util.exceptions.TagNotPresentException
import ru.ertel.mobilecontroller.util.interfaces.NfcWriteUtility

class WritePhoneNfc : Nfc {
    /**
     * Instantiates a new WritePhoneNfc.
     *
     * @param taskCallback the async ui callback
     */
    constructor(taskCallback: TaskCallback?) : super(taskCallback) {}

    /**
     * Instantiates a new WritePhoneNfc.
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
     * Instantiates a new WritePhoneNfc.
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
        if (checkStringArguments(args.javaClass) || args.size != 1 || intent == null) {
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
                writeTelToTagFromIntent((args[0] as String), intent)
            } ?: false) as Boolean
        }
        super.executeWriteOperation()
    }
}