package main.scala
import akka.actor.{Actor, ActorRef, Props, PoisonPill}

// BacteriaKeeper is used to keep track of active bacteria.
// if you have an ActorRef to an instance of BacteriaKeeper, you can easily send a message to all Bacteria sending SendAll(yourMessage) to said instance.
//bacteriaKeeper is responsible for keeping track of all bacteria considered active. Bacteria's responses when cloning or dying all contain appropriate messages to this actor.

trait BacteriaKeeperMessage
case class AddNewBacteria(b:ActorRef) extends BacteriaKeeperMessage
case class RemoveBacteria(b:ActorRef) extends BacteriaKeeperMessage
case class SendAll(m:Any) extends BacteriaKeeperMessage

class BacteriaKeeper( var allBacteria: Set[ActorRef] = Set(),
            var report1: Report = new Report()) extends Actor{

    allBacteria = allBacteria + context.system.actorOf(Props(new Bacteria(id = "Alice")))

    def receive = {
        case SendAll(message) => {
            for(bacteria <- allBacteria)
                bacteria ! message
        }
        case AddNewBacteria(bacteria) => {
          allBacteria = allBacteria + bacteria
        }
        case RemoveBacteria(bacteria) => {
          allBacteria = allBacteria - bacteria
        }
        case "report" => {
            if(report1.deaths == -1){
                report1.survivals = allBacteria.size
                report1.deaths = 0
            } else {
                report1.deaths = report1.survivals - allBacteria.size
                context.system.actorSelection("/user/Stat") ! report1
            }
        }
        case "reset report" => {
            report1 = new Report()
        }
        case "Summary" => {
            context.system.actorSelection("/user/Stat") ! "Summary"
        }
        case m: Mutation => {
            report1.oneMore(m)
        }
        case _ => {
            println("That's awkward. You shouldn't be here...")
        }
    }
}
