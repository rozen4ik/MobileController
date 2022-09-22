package ru.ertel.mobilecotroller.app.data

import ru.ertel.mobilecotroller.app.model.Card

class DataSourceCard {
    private val card = Card(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )


    fun setMessageInfoCard(message: String) {
        if (getValidMessage(message)) {
            card.condition = getCondition(message)
            card.number = getNumber(message)
            card.ruleOfUse = getRuleOfUse(message)
            card.permittedRates = getPermittedRates(message)
            card.startAction = getStartAction(message)
            card.endAction = getEndAction(message)
            card.balance = getBalance(message)
        }
        else {
            card.number = "Данные не найдены"
            card.number = "Данные не найдены"
            card.ruleOfUse = "Данные не найдены"
            card.permittedRates = "Данные не найдены"
            card.startAction = "Данные не найдены"
            card.endAction = "Данные не найдены"
            card.balance = "Данные не найдены"
        }
    }

    fun getInfoCard(): Card {
        return card
    }

    private fun getValidMessage(message: String): Boolean {
        var result = message.substringAfter("client oid=\"")
        result = result.substringBefore("\"")
        return result != "-"
    }

    private fun getCondition(message: String): String {
        var result = message.substringBefore("\"  valid=\"True\"  >")
        result = result.substringAfter("state=\"")
        return result
    }

    private fun getNumber(message: String): String {
        var result = message.substringAfter("identifier value=")
        result = result.substringBefore("code")
        result = result.replace("\"", "")
        return result
    }

    private fun getRuleOfUse(message: String): String {
        var result = message.substringAfter("current_rule=\"")
        result = result.substringBefore("\"  state")
        return result
    }

    private fun getPermittedRates(message: String): String {
        var result = message.substringAfter("tariff=\"")
        result = result.substringBefore("\"  permanent_rule")
        return result
    }

    private fun getStartAction(message: String): String {
        var result = message.substringAfter("valid_from=\"")
        result = result.substringBefore("\"  valid_to")
        return result
    }

    private fun getEndAction(message: String): String {
        var result = message.substringAfter("valid_to=\"")
        result = result.substringBefore("\"  category")
        return result
    }

    private fun getBalance(message: String): String {
        if (message.contains("<currency name=\"RUB\"", ignoreCase = true)) {
            var result = message.substringAfter("\"Российский рубль\"  value=\"")
            result = result.substringBefore("\" />")
            return "$result(RUB)"
        } else {
            return "Данные не найдены"
        }
    }
}