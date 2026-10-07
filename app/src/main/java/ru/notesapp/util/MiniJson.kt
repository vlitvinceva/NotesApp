package ru.notesapp.util

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible



fun Any.toJson(): String {
    val kc = this::class
    require(kc.findAnnotation<MiniSerializable>() != null) {
        "Класс ${kc.simpleName} не помечен @MiniSerializable"
    }

    val sb = StringBuilder("{")
    val props = kc.declaredMemberProperties

    props.forEachIndexed { index, prop ->
        if (index > 0) sb.append(", ")

        val jsonName = prop.findAnnotation<MiniSerializable>()
            ?.name
            ?.takeIf { it.isNotEmpty() }
            ?: prop.name

        sb.append("\"").append(jsonName).append("\": ")

        prop.isAccessible = true
        val value = prop.getter.call(this)
        sb.append(serialize(value))
    }

    sb.append("}")
    return sb.toString()
}

private fun serialize(value: Any?): String = when (value) {
    null -> "null"
    is String -> "\"${value.escape()}\""
    is Number, is Boolean -> value.toString()
    is Enum<*> -> "\"${value.name}\""
    else -> {
        // Рекурсивная сериализация, если вложенный объект помечен @MiniSerializable
        if (value::class.findAnnotation<MiniSerializable>() != null) {
            value.toJson()
        } else {
            "\"$value\""
        }
    }
}

private fun String.escape(): String =
    replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\t", "\\t")

fun <T : Any> fromJson(json: String, klass: KClass<T>): T {
    val map = parseFlatJson(json)
    val ctor = klass.primaryConstructor
        ?: error("У класса ${klass.simpleName} нет primaryConstructor")

    val params: Map<kotlin.reflect.KParameter, Any?> = ctor.parameters
        .mapNotNull { param ->
            val jsonName = param.findAnnotation<MiniSerializable>()
                ?.name
                ?.takeIf { it.isNotEmpty() }
                ?: param.name

            val raw = map[jsonName]

            // Опциональный параметр отсутствует в JSON — не передаём
            if (raw == null && param.isOptional) return@mapNotNull null

            val value: Any? = when (param.type.classifier) {
                String::class -> raw
                Long::class -> raw?.toLongOrNull()
                Int::class -> raw?.toIntOrNull()
                Double::class -> raw?.toDoubleOrNull()
                Boolean::class -> raw?.toBooleanStrictOrNull()
                else -> raw
            }

            param to value
        }
        .toMap()

    ctor.isAccessible = true
    return ctor.callBy(params)
}

private fun parseFlatJson(json: String): Map<String, String?> {
    val body = json.trim().removeSurrounding("{", "}")
    val map = mutableMapOf<String, String?>()
    var i = 0

    while (i < body.length) {
        val keyStart = body.indexOf('"', i)
        if (keyStart < 0) break

        val keyEnd = body.indexOf('"', keyStart + 1)
        val key = body.substring(keyStart + 1, keyEnd)

        val colon = body.indexOf(':', keyEnd)
        var valueStart = colon + 1
        while (valueStart < body.length && body[valueStart] == ' ') valueStart++

        val value: String?
        if (body[valueStart] == '"') {
            val end = body.indexOf('"', valueStart + 1)
            value = body.substring(valueStart + 1, end)
            i = end + 1
        } else {
            var end = valueStart
            while (end < body.length && body[end] != ',' && body[end] != '}') end++
            val raw = body.substring(valueStart, end).trim()
            value = if (raw == "null") null else raw
            i = end + 1
        }

        map[key] = value
    }

    return map
}