package space.bunniesin.crescent.models.routes

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import space.bunniesin.crescent.models.api.User
import space.bunniesin.crescent.models.api.channels.Channel
import kotlin.reflect.typeOf

val UserNavType = object : NavType<User?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): User? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): User? {
        if (value == "null") return null
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: User?) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun serializeAsValue(value: User?): String {
        return if (value == null) "null" else Uri.encode(Json.encodeToString(value))
    }
}

val ChannelNavType = object : NavType<Channel>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Channel? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): Channel {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: Channel) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun serializeAsValue(value: Channel): String {
        return Uri.encode(Json.encodeToString(value))
    }
}

val GroupNavType = object : NavType<Channel.Group>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Channel.Group? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): Channel.Group {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: Channel.Group) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun serializeAsValue(value: Channel.Group): String {
        return Uri.encode(Json.encodeToString(value))
    }
}

val CustomNavTypes = mapOf(
    typeOf<User?>() to UserNavType,
    typeOf<Channel>() to ChannelNavType,
    typeOf<Channel.Group>() to GroupNavType
)
