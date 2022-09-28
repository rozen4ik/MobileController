package ru.ertel.mobilecontroller.app.data

import ru.ertel.mobilecontroller.app.model.Card

class DataSourceCard {

    private var packagesArray = HashMap<String, String>()
    private lateinit var packages: Array<String>

    private val card = Card(
        "",
        "",
    )


    fun setMessageInfoCard(message: String) {
        if (getAnswerLicense(message)) {
            if (getValidMessage(message)) {
                packages = getPackages(message).split("/>").toTypedArray()
                var count = 0
                while (packages.size-1 > count) {
                    if (packages[count].contains("<package id=", ignoreCase = true)) {
                        packagesArray.put(getId(packages[count]), getName(packages[count]))
                        count++
                    }
                }
            } else {
                packagesArray.put("Данные не", "найдены")
            }
        } else {
            packagesArray.put("Пиратская", "копия")
        }
    }

    fun getPackageArray(): HashMap<String, String> {
        return packagesArray
    }

    private fun getAnswerLicense(message: String): Boolean {
        // Проверка на лицензию, указывается номер лицензии контура,
        // при попытке использовать приложение на другом сервер,
        // поступит сообщение о использовании пиратской версии
        return message.contains("<attribute name=\"license\"  value=\"1203\" />")
    }

    private fun getValidMessage(message: String): Boolean {
        return message.contains("<package id", ignoreCase = true)
    }

    private fun getPackages(message: String): String {
        var result = message.substringAfter("<package")
        result = result.substringBeforeLast("/>")
        result = "<package$result/>"
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