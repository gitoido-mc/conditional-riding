package lol.gito.conditionalRiding.common.serialization

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.util.*

class OptionalTypeAdapterFactory : TypeAdapterFactory {
    class OptionalTypeAdapter<T>(private val typeAdapter: TypeAdapter<T>) : TypeAdapter<Optional<T>>() {
        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: Optional<T>) {
            when (value.isPresent) {
                true -> typeAdapter.write(out, value.get())
                false -> out.nullValue()
            }
        }

        @Throws(IOException::class)
        override fun read(`in`: JsonReader): Optional<T> = when (`in`.peek() != JsonToken.NULL) {
            true -> Optional.ofNullable(typeAdapter.read(`in`))
            false -> {
                `in`.nextNull()
                Optional.empty()
            }
        } as Optional<T>

    }

    override fun <T> create(
        gson: Gson,
        typeToken: TypeToken<T>
    ): TypeAdapter<T>? {
        if (!Optional::class.java.isAssignableFrom(typeToken.rawType)) return null

        val optionalType = typeToken.type
        val parametrizedType = optionalType as ParameterizedType
        val typeArgs = parametrizedType.actualTypeArguments
        val inner = TypeToken.get(typeArgs.first())
        val innerAdapter = gson.getAdapter(inner)

        return OptionalTypeAdapter(innerAdapter) as TypeAdapter<T>?
    }
}
