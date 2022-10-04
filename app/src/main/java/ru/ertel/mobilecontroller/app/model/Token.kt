package ru.ertel.mobilecontroller.app.model

data class Token(
    var id: String,
    var nameToken: String,
    var startDate: String,
    var counterparty: String,
    var endDate: String,
    var techSupport: String,
    var numberOfActivatedDevices: String,
    var numberOfActivations: String,
    var notes: String
)
