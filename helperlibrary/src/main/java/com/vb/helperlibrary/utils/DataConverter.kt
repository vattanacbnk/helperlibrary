package com.vb.helperlibrary.utils

import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONObject

class DataConverter {
    companion object {
        fun convertJsonStringToObject(jsonObject: JSONObject, clazz: Class<*>): Any {
            try {
                val mJson = JsonParser.parseString(jsonObject.toString())
                return Gson().fromJson(mJson, clazz)
            } catch (e: Exception) {
                throw Exception("error convert json string to object, maybe your data is wrong format")
            }
        }

        fun convertJsonStringToObject(mJson: String, clazz: Class<*>): Any {
            try {
                return Gson().fromJson(mJson, clazz)
            } catch (e: Exception) {
                throw Exception("error convert json string to object, maybe your data is wrong format")
            }

        }

        fun objectItemToJsonStringConvert(data: Any): String {
            try {
                return Gson().toJson(data)
            } catch (e: Exception) {
                throw Exception("error convert object to json, maybe your data is wrong format")
            }
        }
    }
}