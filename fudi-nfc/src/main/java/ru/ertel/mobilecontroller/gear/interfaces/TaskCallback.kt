package ru.ertel.mobilecontroller.gear.interfaces

interface TaskCallback {
    /**
     * Used to deliver the result of the operation.
     * @param result of the operation
     */
    fun onReturn(result: Boolean?)

    /**
     * Used to signal current state of the task.
     * @param values containing the state in values[0]
     */
    fun onProgressUpdate(vararg values: Boolean?)

    /**
     * When any error occurs, the error is passed here.
     * @param e the cause of the task to stopping or malfunctioning
     */
    fun onError(e: Exception?)
}