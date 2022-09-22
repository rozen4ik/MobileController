package ru.ertel.mobilecotroller.util.async

import android.content.Intent
import ru.ertel.mobilecotroller.gear.interfaces.OpCallback
import ru.ertel.mobilecotroller.gear.interfaces.TaskCallback
import ru.ertel.mobilecotroller.util.interfaces.NfcWriteUtility

class WriteCallbackNfc : Nfc {
    /**
     * Instantiates a new WriteCallbackNfc.
     *
     * @param taskCallback the async ui callback
     */
    constructor(taskCallback: TaskCallback?) : super(taskCallback) {}

    /**
     * Instantiates a new WriteCallbackNfc.
     *
     * @param taskCallback           the async ui callback
     * @param opCallback the async operation callback
     */
    constructor(taskCallback: TaskCallback?, opCallback: OpCallback) : super(
        taskCallback,
        opCallback
    ) {
    }

    /**
     * Instantiates a new WriteCallbackNfc.
     *
     * @param taskCallback           the async ui callback
     * @param opCallback the async operation callback
     * @param nfcWriteUtility        the nfc write utility
     */
    constructor(
        taskCallback: TaskCallback?,
        opCallback: OpCallback,
        nfcWriteUtility: NfcWriteUtility
    ) : super(taskCallback, opCallback, nfcWriteUtility) {
    }

    override fun executeWriteOperation(intent: Intent?, vararg args: Any?) {
        super.executeWriteOperation()
    }
}