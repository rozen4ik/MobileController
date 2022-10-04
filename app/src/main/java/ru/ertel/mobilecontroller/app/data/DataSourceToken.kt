package ru.ertel.mobilecontroller.app.data

import ru.ertel.mobilecontroller.app.model.Token

class DataSourceToken {
    private val token: Token = Token(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )

    private lateinit var answer: String

    fun setToken(message: String) {
        token.id = getId(message)
        token.nameToken = getNameToken(message)
        token.startDate = getStartDate(message)
        token.counterparty = getCounterparty(message)
        token.endDate = getEndDate(message)
        token.techSupport = getTechSupport(message)
        token.numberOfActivatedDevices = getNumberOfActivatedDevices(message)
        token.numberOfActivations = getNumberOfActivations(message)
        token.notes = getNotes(message)
    }

    fun setAnswer(message: String) {
        answer = message
    }

    fun getToken(): Token {
        return token
    }

    fun getAnswer(): String {
        return answer
    }

    private fun getId(message: String): String {
        var result = message.substringAfter("{\"id\":")
        result = result.substringBefore(",")
        return result
    }

    private fun getNameToken(message: String): String {
        var result = message.substringAfter("\"token\":\"")
        result = result.substringBefore("\",")
        return result
    }

    private fun getStartDate(message: String): String {
        var result = message.substringAfter("\"start_date\":\"")
        result = result.substringBefore("\",")
        return result
    }

    private fun getCounterparty(message: String): String {
        var result = message.substringAfter("\"counterparty\":\"")
        result = result.substringBefore("\",").replace("\\", "")
        return result
    }

    private fun getEndDate(message: String): String {
        var result = message.substringAfter("\"end_date\":\"")
        result = result.substringBefore("\",")
        return result
    }

    private fun getTechSupport(message: String): String {
        var result = message.substringAfter("\"tech_support\":\"")
        result = result.substringBefore("\",")
        return result
    }

    private fun getNumberOfActivatedDevices(message: String): String {
        var result = message.substringAfter("\"number_of_activated_devices\":")
        result = result.substringBefore(",")
        return result
    }

    private fun getNumberOfActivations(message: String): String {
        var result = message.substringAfter("\"number_of_activations\":")
        result = result.substringBefore(",")
        return result
    }

    private fun getNotes(message: String): String {
        var result = message.substringAfter("\"notes\":\"")
        result = result.substringBefore("\"}")
        return result
    }
}