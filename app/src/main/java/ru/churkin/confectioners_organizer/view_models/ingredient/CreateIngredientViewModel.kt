package ru.churkin.confectioners_organizer.view_models.ingredient

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.churkin.confectioners_organizer.date.parseDate
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient
import ru.churkin.confectioners_organizer.repositories.IngredientsRepository
import java.util.*

class CreateIngredientViewModel() : ViewModel() {

    private val repository: IngredientsRepository = IngredientsRepository()

    private val _state: MutableStateFlow<IngredientState> = MutableStateFlow(IngredientState())

    val state: StateFlow<IngredientState>
        get() = _state

    val currentState: IngredientState
        get() = state.value

    suspend fun initState(id: Long?) {
        viewModelScope.launch {
            if (id == null) {
                val localId = repository.createIngredient()
                val ingredient = IngredientState()
                _state.value = currentState.copy(
                    id = localId,
                    title = ingredient.title,
                    availability = ingredient.availability,
                    available = ingredient.available,
                    unitsAvailable = ingredient.unitsAvailable,
                    unitsPrice = ingredient.unitsPrice,
                    _costPrice = ingredient.costPrice.toString(),
                    sellBy = ingredient.sellBy,
                    errors = ingredient.errors
                )

            } else {
                val ingredient = repository.loadIngredient(checkNotNull(id))
                _state.value = currentState.copy(
                    id = ingredient.id,
                    title = ingredient.title,
                    availability = ingredient.availability,
                    available = ingredient.available,
                    unitsAvailable = ingredient.unitsAvailable,
                    unitsPrice = ingredient.unitsPrice,
                    _costPrice = ingredient.costPrice.toString(),
                    sellBy = ingredient.sellBy
                )
            }
        }
    }

    fun updateTitle(title: String) {
        _state.value = currentState.copy(title = title)
    }

    fun updateAvailability(availability: Boolean) {
        _state.value = currentState.copy(availability = availability)
    }

    fun updateAvailable(available: Int) {
        _state.value = currentState.copy(available = available)
    }

    fun updateUnitsAvailable(unitsAvailable: String) {
        _state.value = currentState.copy(unitsAvailable = unitsAvailable)
    }

    fun updateUnitsPrice(unitsPrice: String) {
        _state.value = currentState.copy(unitsPrice = unitsPrice)
    }

    fun updatecostPrice(costPrice: String) {
        Log.e(
            "IngredientViewModel",
            "${isValidatePrice(costPrice)} $costPrice"
        )
        if (!isValidatePrice(costPrice)) {
            _state.value = currentState.copy(errors = mapOf("costPrice" to "invalid price"))
            return
        } else {
            _state.value = currentState.copy(errors = emptyMap())
        }

        _state.value = currentState.copy(_costPrice = costPrice)
    }

    private fun isValidatePrice(price: String) =
        price.isEmpty() || "^\\d*?\\.?\\d{0,2}$".toRegex().matches(price)

    fun updateSellBy(sellBy: String) {
        if (sellBy.isEmpty()) return
        _state.value = currentState.copy(sellBy = sellBy.parseDate())
    }

    fun emptyState() {
        _state.value = IngredientState(id = currentState.id)
    }

    fun addIngredient() {
        val ingredient = currentState.toIngredient()
        viewModelScope.launch {
            repository.insertIngredient(ingredient)
        }
    }

    fun removeIngredient(id: Long) {
        viewModelScope.launch {
            repository.removeIngredient(id = id)
            repository.loadIngredients()
        }
    }
}

data class IngredientState(
    val id: Long = 0,
    val title: String = "",
    val availability: Boolean = false,
    val available: Int = 0,
    val unitsAvailable: String = "ед. изм.",
    val unitsPrice: String = "рубль за ______",
    val _costPrice: String = "",
    val sellBy: Date? = null,
    val errors: Map<String, String> = emptyMap()
) {
    val costPrice: Float
        get() = if (_costPrice.isEmpty()) 0f else _costPrice.toFloat()
}

fun IngredientState.toIngredient()  = Ingredient(id, title, availability, available, unitsAvailable, unitsPrice, costPrice, sellBy)

@Serializer(forClass = DateSerializer::class)
object DateSerializer : KSerializer<Date> {

    override fun serialize(output: Encoder, obj: Date) {
        output.encodeString(obj.time.toString())
    }

    override fun deserialize(input: Decoder): Date {
        return Date(input.decodeString().toLong())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)
}
