package com.justai.jaicf.template.scenario

import com.justai.jaicf.channel.yandexalice.model.AliceEvent
import com.justai.jaicf.channel.yandexalice.alice
import com.justai.jaicf.model.scenario.Scenario

object MainScenario: Scenario(
    dependencies = listOf(RecordScenario)
) {
    init {
        state("main") {
            activators {
                event(AliceEvent.START)
            }

            action {
                reactions.run {
                    say("Вас приветствует Сервис быстрых платежей. Могу помочь сделать перевод по номеру телефона или рассказать, что я умею")
                    buttons("Перевод", "Что умеешь?")
                    alice?.image(
                        "https://i.imgur.com/SUSGpqG.jpg",
                        "Сервис быстрых платежей",
                        "Хотите сообщить ваши показания счетчиков?")
                }
            }
        }

        state("pay") {
            activators {
                regex("Перевод|перевести")
            }

            action {
                phone("Кому будем переводить?", "sum")
            }

            state("sum") {

                action {
                    val sum = sum("Какую сумму?", "done")
                }

                state("done") {
                    action {
                        reactions.say("Выполняю перевод")
                        reactions.alice?.endSession()
                    }
                }
            }
        }

        state("no") {
            activators {
                regex("нет|не хочу|отстань|хватит|до свидания|передумал")
            }

            action {
                reactions.say("До свидания")
                reactions.alice?.endSession()
            }
        }

        state("skils") {
            activators {
                regex("умеешь|можешь|навык|еще|расскажи")
            }

            action {
                reactions.say("Пока очень мало, но я учусь")
                reactions.alice?.goBack()
            }
        }

        fallback {
            reactions.say("Я вас не поняла. Хотите сделать перевод?")
            reactions.buttons("Да", "Нет")
        }
    }
}