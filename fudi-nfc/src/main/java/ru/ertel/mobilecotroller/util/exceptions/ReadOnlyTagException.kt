package ru.ertel.mobilecotroller.util.exceptions

class ReadOnlyTagException : TagNotWritableException {
    constructor() {}
    constructor(message: String?) : super(message) {}
}