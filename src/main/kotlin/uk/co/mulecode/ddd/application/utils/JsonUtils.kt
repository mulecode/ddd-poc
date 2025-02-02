package uk.co.mulecode.ddd.application.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class JsonUtils {
    companion object {

        private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
            findAndRegisterModules()
        }

        @JvmStatic
        fun getObjectMapper(): ObjectMapper {
            return objectMapper
        }

        @JvmStatic
        fun toJson(obj: Any): String {
            return objectMapper.writeValueAsString(obj)
        }

        @JvmStatic
        fun <T> fromJson(json: String, clazz: Class<T>): T {
            return objectMapper.readValue(json, clazz)
        }

        @JvmStatic
        fun fromJsonToMap(json: String): Map<String, String> {
            return objectMapper.readValue(json, object : TypeReference<Map<String, String>>() {})
        }
    }
}
