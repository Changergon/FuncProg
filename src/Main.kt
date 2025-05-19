fun main() {
    // 1. Функция как аргумент (передаем функцию скидки)
    val order1 = Order(100.0, ::discount10Percent)
    println("Order1 total: ${order1.calculateTotal()}") // 90.0

    // 2. Функция как возвращаемое значение (getTaxCalculator возвращает функцию)
    val taxCalculator = getTaxCalculator("US")
    println("Tax for 100: ${taxCalculator(100.0)}") // 8.0 (8%)

    // 3. Функция высшего порядка (applyDiscountAndTax принимает и возвращает функции)
    val finalPriceCalculator = applyDiscountAndTax(::discount10Percent, taxCalculator)
    println("Final price: ${finalPriceCalculator(100.0)}") // 97.2

    // 4. Композиция функций (объединяем две функции в одну)
    val discountAndTax = compose(taxCalculator, ::discount20Percent)
    println("Discount 20% + tax: ${discountAndTax(100.0)}") // 86.4

    // 5. Каррирование (разбиваем функцию на цепочку вызовов)
    val curriedDiscount = curriedDiscount(0.1) // 10% скидка
    println("Curried discount: ${curriedDiscount(100.0)}") // 90.0
}

// Предметная область: Заказ
data class Order(val amount: Double, val discount: (Double) -> Double) {
    fun calculateTotal(): Double = discount(amount)
}

// 1. Функция как аргумент
fun discount10Percent(amount: Double): Double = amount * 0.9
fun discount20Percent(amount: Double): Double = amount * 0.8

// 2. Функция как возвращаемое значение
fun getTaxCalculator(country: String): (Double) -> Double {
    return when (country) {
        "US" -> { amount -> amount * 1.08 } // 8% tax
        "EU" -> { amount -> amount * 1.20 } // 20% VAT
        else -> { amount -> amount } // no tax
    }
}

// 3. Функция высшего порядка
fun applyDiscountAndTax(
    discount: (Double) -> Double,
    tax: (Double) -> Double
): (Double) -> Double {
    return { amount -> tax(discount(amount)) }
}

// 4. Композиция функций
fun compose(f: (Double) -> Double, g: (Double) -> Double): (Double) -> Double {
    return { x -> f(g(x)) }
}

// 5. Каррирование
fun curriedDiscount(discountRate: Double): (Double) -> Double {
    return { amount -> amount * (1 - discountRate) }
}