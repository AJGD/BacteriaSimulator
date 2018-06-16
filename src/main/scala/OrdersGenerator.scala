package main.scala
import akka.actor.{Actor, ActorRef, Props, PoisonPill}

class OrdersGenerator() extends Actor {
    def receive() = {
        case _ => println("???")
    }
    val bacteriaKeeper = context.system.actorOf(Props(new BacteriaKeeper()), "BacteriaKeeper")

    def sendToAllBacteria(message:Any):Unit = {
        bacteriaKeeper ! SendAll(message)
    }

    for (_ <- 1 to 4) {
        sendToAllBacteria("clone yourself")
        Thread.sleep(100)
    }

    sendToAllBacteria(Photosynthesis())
    Thread.sleep(100)

    for (_ <- 1 to 3) {
        sendToAllBacteria("clone yourself")
        Thread.sleep(100)
    }

    for (_ <- 1 to 3) {
        sendToAllBacteria("clone yourself")
        Thread.sleep(100)
    }

    sendToAllBacteria("your name?")
    Thread.sleep(100)

    bacteriaKeeper ! "report"

    sendToAllBacteria(Spectinomycin())
    Thread.sleep(100)

    bacteriaKeeper ! "report"

    self ! PoisonPill
}
