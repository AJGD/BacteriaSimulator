package main.scala
import akka.actor.{Actor, ActorRef, Props, PoisonPill}

// this class actually runs the simulation, cloning and mutating Bacteria in cycles.
// at the end of each iteration of the "big" cycle an Antibiotic is administered ("cycles" times).
// inside the "big" cycle, the "clone-mutate" cycle repeats ("cloneMutateCycles" times).
// before mutating, all Bacteria receive "cloneRate" messages to clone.
class OrdersGenerator(val cloneRate:Int = 3, val cloneMutateCycles:Int = 3, val cycles:Int = 10) extends Actor {
    def receive() = {
        case _ => println("???")
    }
    val bacteriaKeeper = context.system.actorOf(Props(new BacteriaKeeper()), "BacteriaKeeper")

    import RandomGenerator.randomMutation

    def sendToAllBacteria(message:Any):Unit = {
        bacteriaKeeper ! SendAll(message)
        Thread.sleep(50)
    }

    for(_ <- 1 to cycles) {
        for(_ <- 1 to cloneMutateCycles) {
            for(_ <- 1 to cloneRate) sendToAllBacteria("clone yourself")
            sendToAllBacteria(randomMutation())
        }

        sendToAllBacteria("your name?")
        Thread.sleep(100)

        bacteriaKeeper ! "report"

        sendToAllBacteria(Spectinomycin())
        Thread.sleep(100)

        bacteriaKeeper ! "report"
        Thread.sleep(100)
        bacteriaKeeper ! "reset report"
        Thread.sleep(100)
    }
    bacteriaKeeper ! "Summary"
    sendToAllBacteria(PoisonPill)

    Thread.sleep(100)

    self ! PoisonPill
}
