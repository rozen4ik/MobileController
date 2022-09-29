package ru.ertel.mobilecontroller.app.data

class DataSourceCard {

    private var packagesArray = HashMap<String, String>()
    private lateinit var packages: Array<String>
    private var balance = ""
    private var numberCard = ""

    fun setMessageInfoPackage(message: String) {
        if (getAnswerLicense(message)) {
            numberCard = getNumberCard(message)
            balance = getBalance(message)
            if (getValidMessage(message)) {
                packages = getPackages(message).split("/>").toTypedArray()
                var count = 0
                while (packages.size-1 > count) {
                    if (packages[count].contains("<package id=", ignoreCase = true)) {
                        packagesArray[getId(packages[count])] = getName(packages[count])
                        count++
                    }
                }
            } else {
                packagesArray["Данные не"] = "найдены"
            }
        } else {
            packagesArray["Пиратская"] = "копия"
        }
    }

    fun getValueNumberCard(): String {
        return numberCard
    }

    fun getValueBalance(): String {
        return balance
    }

    fun getPackageArray(): HashMap<String, String> {
        return packagesArray
    }

    private fun getAnswerLicense(message: String): Boolean {
        // Проверка на лицензию, указывается номер лицензии контура,
        // при попытке использовать приложение на другом сервер,
        // поступит сообщение о использовании пиратской версии
        return message.contains("<attribute name=\"license\"  value=\"1232\" />")
    }

    private fun getValidMessage(message: String): Boolean {
        return message.contains("<package id", ignoreCase = true)
    }

    private fun getNumberCard(message: String): String {
        var result = message.substringAfter("<identifier value=\"")
        result = result.substringBefore("\"")
        return result
    }

    private fun getBalance(message: String): String {
        var result = "Нет баланса"
        if (message.contains("<currency name=\"RUB\"", ignoreCase = true)) {
            result = message.substringAfter("<currency name=\"RUB\"  comment=\"Российский рубль\"  value=\"")
            result = result.substringBefore("\"")
        }
        return result
    }

    private fun getPackages(message: String): String {
        var result = message.substringAfter("<package")
        result = result.substringBeforeLast("</identifier>")
        result = "<package$result"
        return result
    }

    private fun getId(message: String): String {
        var result = message.substringAfter("<package id=\"")
        result = result.substringBefore("\"  state=")
        return result
    }

    private fun getName(message: String): String {
        var result = message.substringAfter("description=\"")
        result = result.substringBefore("\"  used_count")
        return result
    }
}