package com.justai.jaicf.template.scenario

import com.justai.jaicf.channel.yandexalice.model.AliceEvent
import com.justai.jaicf.channel.yandexalice.alice
import com.justai.jaicf.model.scenario.Scenario

/*
POST http://localhost:8080/
Accept: *//*
Cache-Control: no-cache

{"request": {
    "command": "да",
    "nlu": {
    "tokens": [
    "start"
    ],
    "entities": [],
    "intents": {}
},
    "original_utterance": "start",
    "type": "SimpleUtterance",
    "markup": {}
},
    "meta": {
    "locale": "ru-RU",
    "timezone": "UTC",
    "client_id": "ru.yandex.searchplugin/7.16 (none none; android 4.4.2)",
    "interfaces": {
    "screen": {}
}
},
    "session": {
    "new": false,
    "message_id": 1,
    "session_id": "d7abd38a-f375bea5-35a931ac-9354ab18",
    "skill_id": "066fcda6-639f14d0-e3f2deae-1bdcb323",
    "user_id": "06fd82cf-4474afa3-1d5a8c57-05ff0df5",
    "application": {
    "application_id": "9746A97B24D943A0C0524F38DDFBD0CF3060B2F5FD81777912AE90FAEE2A6D00"
}
},
    "version": "1.0"
}

Привет, Алиса!
-- Привет
Хочу сделать перевод по номеру телефона
-- Вас приветствует Сервис быстрых платежей. Могу помочь сделать перевод по номеру телефона или рассказать, что я умею.
Переведи 5 тысяч рублей Маме
-- На вашем счете не достаточно средств для перевода такой суммы
А сколько у меня там остаток?
-- Остаток на вашем счете состевляет 1 283 рубля 33 копейки
Тогда переведи маме 1000 рублей
-- Просьба подтвердить перевод 1000р на номер 8-951-370-36-47
Подтверждаю
-- Выполняю перевод, вскоре вам предет уведомление о платеже. Могу еще чем-то помочь?
Расскажи кто тебя создал
-- Я всего лишь тестовый пример. Но написали меня не тестовые, а самые настоящие профессиональные программисты из компании Диасофт. Они умеют писать очень крутые вещи


*/
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
                    buttons("Перевод", "Что умеешь?", "Кто тебя создал?")
                }
            }
        }
        
        state("balance") {
            activators {
                regex(".*остаток.*")
                regex(".*баланс.*")
            }
            action {
                reactions.say("Остаток на вашем счете составляет тысяча двести восемьдесят три рубля тридцать три копейки")
            }
        }
        
        state("yes") {
            activators {
                regex("да .*")
                regex("да")
                regex(".*сделать перевод.*")
            }
            action {
                reactions.say("Если хотите сделать перевод просто скажите получателя и сумму")
            }
        }
        
        
        
        state("payFail") {
            activators {
                regex(".*тысяч .*")
                regex(".*5000.*")
            }
            action {
                reactions.say("На вашем счете не достаточно средств для перевода такой суммы")
            }
        }
        
        

        state("pay") {
            activators {
                regex(".*тысячу.*")
                regex(".*1000.*")
            }
            action {
                reactions.say("Просьба подтвердить перевод одной тысячи рублей на номер 8 951 370 36 47")
            }
        }
        
        state("approve") {
            activators {
                regex(".*подтверждаю.*")
                regex(".*верно.*")
            }
            action {
                reactions.say("Выполняю перевод, вскоре вам предет уведомление о переводе. Могу еще чем-то помочь?")
            }
        }

        state("no") {
            activators {
                regex(".*(нет|не хочу|отстань|хватит|до свидания|передумал).*")
            }
            action {
                reactions.say("До свидания")
                reactions.alice?.endSession()
            }
        }

        state("skils") {
            activators {
                regex(".*(умеешь|навык|еще).*")
            }
            action {
                reactions.say("Я тестовый пример и умею очень мало")
            }
        }

        state("developer") {
            activators {
                regex(".*(создал|разраб).*")
            }
            action {
                reactions.say("Я всего лишь тестовый пример. Но написали меня не тестовые, а " +
                        "самые настоящие профессиональные программисты из компании Диасофт. Они умеют писать очень крутые вещи")
            }
        }

        fallback {
            reactions.say("Я вас не поняла. Хотите сделать перевод?")
            reactions.buttons("Да", "Нет")
        }
    }
}