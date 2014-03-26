package com.traderapist.nba

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(InjuryController)
@Mock([Injury, Player])
class InjuryControllerSpec extends Specification {

    Player player

    def oneDay = 1000*60*60*24

    def setup() {
        player = new Player(name: "Dan", position: "PG", rgPosition: "PG", height: 69, weight: 170, url: "traderapist.com")
        player.id = "macleda01"
        player.save(validate: true)
    }

    def cleanup() {
        player = null
    }

    void "test redirect to list"() {
        when:
        controller.index()

        then:
        response.redirectedUrl == "/injury/list"
    }

    void "test retrieve all injuries"() {
        when:
        def currentInjury = new Injury(player: player,
                injuryDate: new Date(System.currentTimeMillis() - oneDay),
                returnDate: new Date(System.currentTimeMillis() + oneDay),
                details: "test current")
        def pastInjury = new Injury(player: player,
                injuryDate: new Date(System.currentTimeMillis() - oneDay*3),
                returnDate: new Date(System.currentTimeMillis() - oneDay*2),
                details: "test past")
        currentInjury.save()
        pastInjury.save()

        controller.list()

        then:
        view == '/injury/list'

        model.injuries.size() == 2

        model.injuries[0].player == player
        model.injuries[0].injuryDate.equals(currentInjury.injuryDate)
        model.injuries[0].returnDate.equals(currentInjury.returnDate)
        model.injuries[0].details.equals("test current")

        model.injuries[1].player == player
        model.injuries[1].injuryDate.equals(pastInjury.injuryDate)
        model.injuries[1].returnDate.equals(pastInjury.returnDate)
        model.injuries[1].details.equals("test past")

        model.players.size() == 1
        model.players[0] == player
    }

    void "test retrieve current injuries"() {
        when:
        params.current = true
        def currentInjury = new Injury(player: player,
                                    injuryDate: new Date(System.currentTimeMillis() - oneDay),
                                    returnDate: new Date(System.currentTimeMillis() + oneDay),
                                    details: "test current")
        def pastInjury = new Injury(player: player,
                                    injuryDate: new Date(System.currentTimeMillis() - oneDay*3),
                                    returnDate: new Date(System.currentTimeMillis() - oneDay*2),
                                    details: "test past")
        currentInjury.save()
        pastInjury.save()

        controller.list()

        then:
        view == '/injury/list'

        model.injuries.size() == 1

        model.injuries[0].player == player
        model.injuries[0].injuryDate.equals(currentInjury.injuryDate)
        model.injuries[0].returnDate.equals(currentInjury.returnDate)
        model.injuries[0].details.equals("test current")

        model.players.size() == 1
        model.players[0] == player
    }

    void "test retrieve current injuries - none available"() {
        when:
        params.current = true
        def pastInjury = new Injury(player: player,
                injuryDate: new Date(System.currentTimeMillis() - oneDay*3),
                returnDate: new Date(System.currentTimeMillis() - oneDay*2),
                details: "test past")
        pastInjury.save()

        controller.list()

        then:
        view == "/injury/list"

        model.injuries.size() == 0

        model.players.size() == 1
        model.players[0] == player
    }

    void "test set return date to be today"() {
        when:
        def cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        def yesterday = new Date(cal.timeInMillis)
        cal.add(Calendar.DATE, 2)
        def tomorrow = new Date(cal.timeInMillis)

        def injury = new Injury(player: player,
                injuryDate: new Date(System.currentTimeMillis() - oneDay*3),
                returnDate: new Date(System.currentTimeMillis() + oneDay),
                details: "injury ending tomorrow")
        injury.save()
        params.action = InjuryController.ACTION_RETURN_DATE_TODAY
        params.id = injury.id

        controller.save()

        then:
        response.redirectedUrl == '/injury/list'
        def modifiedInjury = Injury.get(injury.id)
        modifiedInjury.injuryDate.equals(injury.injuryDate) &&
                modifiedInjury.returnDate.before(tomorrow) && modifiedInjury.returnDate.after(yesterday)
    }

    void "test set return date to be today - bad id"() {
        when:
        def injury = new Injury(player: player,
                injuryDate: new Date(System.currentTimeMillis() - oneDay*3),
                returnDate: new Date(System.currentTimeMillis() + oneDay),
                details: "injury ending tomorrow")
        injury.save()
        params.action = InjuryController.ACTION_RETURN_DATE_TODAY
        params.id = 999

        controller.save()

        then:
        response.redirectedUrl == '/injury/list'
        flash.message == InjuryController.INVALID_INJURY_ID_ERROR
        def modifiedInjury = Injury.get(injury.id)
        modifiedInjury.injuryDate.equals(injury.injuryDate) &&
                modifiedInjury.returnDate.equals(injury.returnDate)
    }
}
