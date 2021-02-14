package com.justai.jaicf.template.scenario

import com.justai.jaicf.context.ActionContext
import com.justai.jaicf.model.scenario.Scenario

object PayScenario: Scenario() {
    var sumPay = ""
    var phoneNumber = ""
    
    init {
        state("phone", modal = true) {

            state("phone_number") {
                activators {
                    regex("\\d+.*")
                }

                action {
                    phoneNumber = (context.client["last_reply"] as? String).toString()
                    reactions.say("Записала - $phoneNumber")
                    reactions.goBack()
                }
            }

            fallback {
                reactions.sayRandom("Не поняла номер, повторите, пожалуйста", "Повторите еще раз")
            }
        }

        state("sum", modal = true) {

            state("sum_value") {
                activators {
                    regex("\\d+.*")
                }

                action {
                    sumPay = (context.client["last_reply"] as? String).toString()
                    reactions.say("Записала - $sumPay")
                    reactions.goBack()
                }
            }

            fallback {
                reactions.sayRandom("Не поняла сумму, повторите, пожалуйста", "Повторите еще раз")
            }
        }
    }
}

fun ActionContext.phone(message: String, callback: String) : String {
    reactions.say(message)
    reactions.changeState("/phone", callback)
    return PayScenario.phoneNumber
}

fun ActionContext.sum(message: String, callback: String) : String {
    reactions.say(message)
    reactions.changeState("/sum", callback)
    return PayScenario.sumPay
}