package ru.ertel.mobilecontroller.util.exceptions

class ReadOnlyTagException : TagNotWritableException {
    constructor() {}
    constructor(message: String?) : super(message) {}
}