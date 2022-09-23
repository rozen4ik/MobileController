package ru.ertel.mobilecontroller.util.exceptions

class TagNotPresentException : Exception {
    constructor() : super("Intent does not contain a tag") {}
    constructor(message: String?) : super(message) {}
    constructor(e: Exception) {
        stackTrace = e.stackTrace
    }
}