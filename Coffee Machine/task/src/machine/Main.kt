package machine

import kotlin.system.exitProcess

val ESPRESSO = CoffeeMachine.Resources(250, 0, 16, 1, 4)
val LATTE = CoffeeMachine.Resources(350, 75, 20, 1, 7)
val CAPPUCCINO = CoffeeMachine.Resources(200, 100, 12, 1, 6)

class CoffeeMachine {

    enum class State(val output: String) {
        MainMenu("Write action (buy, fill, take, remaining, exit):"),
        ChooseCoffee("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:"),
        FillWater("Write how many ml of water you want to add:"),
        FillMilk("Write how many ml of milk you want to add:"),
        FillBeans("Write how many grams of coffee beans you want to add:"),
        FillCups("Write how many disposable cups you want to add:"),
    }

    var state = State.MainMenu
        set(value) {
            field = value
            println(field.output)
        }

    init {
        println(state.output)
    }

    class Resources(
        var water: Int = 0,
        var milk: Int = 0,
        var beans: Int = 0,
        var cups: Int = 0,
        var money: Int = 0,
    )

    val machine = Resources(400, 540, 120, 9, 550)

    fun printRemaining() = println(
    """
    The coffee machine has:
    ${ machine.water } ml of water
    ${ machine.milk } ml of milk
    ${ machine.beans } g of coffee beans
    ${ machine.cups } disposable cups
    $${ machine.money } of money
    
    """.trimIndent())

    fun request(coffee: Resources) {
        when {
            machine.water - coffee.water < 0 -> println("Sorry, not enough water!")
            machine.milk - coffee.milk < 0 -> println("Sorry, not enough milk!")
            machine.beans - coffee.beans < 0 -> println("Sorry, not enough coffee beans!")
            machine.cups == 0 -> println("Sorry, not enough cups!")
            else -> {
                println("I have enough resources, making you a coffee!")
                machine.water -= coffee.water; machine.milk -= coffee.milk; machine.beans -= coffee.beans;
                machine.money += coffee.money; machine.cups--
            }
        }
        println()
        state = State.MainMenu
    }

    fun take() {
        println("I gave you $${ machine.money }\n")
        machine.money = 0
    }

    fun execute(command: String) {
        when (state) {
            State.MainMenu -> {
                println()
                when (command) {
                    "buy" -> state = State.ChooseCoffee
                    "fill" -> state = State.FillWater
                    "take" -> { take(); state = State.MainMenu }
                    "exit" -> exitProcess(0)
                    "remaining" -> { printRemaining(); state = State.MainMenu }
                }
            }
            State.ChooseCoffee -> {
                when (command) {
                    "1" -> request(ESPRESSO)
                    "2" -> request(LATTE)
                    "3" -> request(CAPPUCCINO)
                    "back" -> { println(); state = State.MainMenu }
                }
            }
            State.FillWater -> { machine.water += command.toInt(); state = State.FillMilk }
            State.FillMilk -> { machine.milk += command.toInt(); state = State.FillBeans }
            State.FillBeans -> { machine.beans += command.toInt(); state = State.FillCups }
            State.FillCups -> { machine.cups += command.toInt(); println(); state = State.MainMenu }
        }

    }
}

fun main() {
    val coffeeMachine = CoffeeMachine()
    while (true) {
        val command = readln()
        coffeeMachine.execute(command)
    }
}
