fun main() {
    println("=== Онлайн-магазин ===")

    print("Введите сумму заказа: ")
    val amount = readLine()?.toDoubleOrNull()
    if (amount == null || amount <= 0) {
        println("Некорректная сумма")
        return
    }

    print("Введите страну (RU, US или EU): ")
    val country = readLine()?.uppercase() ?: "OTHER"

    print("Введите промокод (FREE!, SAVE10, SAVE20 или ничего): ")
    val promo = readLine()?.uppercase() ?: ""

    // Основная функция, использующая все 5 принципов ФП
    val finalPriceFunction = calculateFinalPrice(promo, country)

    val finalAmount = finalPriceFunction(amount)

    println("Цена после применения скидки и налога: ${"%.2f".format(finalAmount)}")
}



// Главная функция — демонстрирует все 5 принципов ФП
fun calculateFinalPrice(
    discountCode: String,
    country: String
): (Double) -> Double {
    // 2. Функции как возвращаемые значения
    val discountFn = getDiscountFunction(discountCode)
    val taxFn = getTaxFunction(country)

    // 4. Композиция функций
    val composedFn = compose(taxFn, discountFn)

    // 3. Функция высшего порядка: возвращает новую функцию расчёта
    return { amount -> composedFn(amount) }
}

// 1. Функции как аргументы + 4. Композиция
fun compose(f: (Double) -> Double, g: (Double) -> Double): (Double) -> Double {
    return { x -> f(g(x)) }
}

// 5. Каррирование
fun curriedDiscount(rate: Double): (Double) -> Double {
    return { amount -> amount * (1 - rate) }
}

// 2. Функции как возвращаемые значения
fun getDiscountFunction(code: String): (Double) -> Double {
    return when (code) {
        "SAVE10" -> curriedDiscount(0.10)  // Каррированная функция
        "SAVE20" -> curriedDiscount(0.20)
        "FREE!" -> curriedDiscount(1.00)
        else -> { amount -> amount }
    }
}

fun getTaxFunction(country: String): (Double) -> Double {
    return when (country) {
        "US" -> { amount -> amount * 1.08 }
        "EU" -> { amount -> amount * 1.20 }
        "RU" -> { amount -> amount * 1.13 }
        else -> { amount -> amount }
    }
}
