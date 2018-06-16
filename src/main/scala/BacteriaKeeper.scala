package main.scala
import akka.actor.{Actor, ActorRef, Props, PoisonPill}

// BacteriaKeeper is used to keep track of active bacteria.
// if you have an ActorRef to an instance of BacteriaKeeper, you can easily send a message to all Bacteria sending SendAll(yourMessage) to said instance.
//bacteriaKeeper is responsible for keeping track of all bacteria considered active. Bacteria's responses when cloning or dying all contain appropriate messages to this actor.

trait BacteriaKeeperMessage
case class AddNewBacteria(b:ActorRef) extends BacteriaKeeperMessage
case class RemoveBacteria(b:ActorRef) extends BacteriaKeeperMessage
case class SendAll(m:Any) extends BacteriaKeeperMessage

class BacteriaKeeper( var allBacteria: Set[ActorRef] = Set()) extends Actor{
    var photosynthesis: Integer = 0
    var sterilization: Integer = 0
    var preservationOverFertility: Integer = 0

    allBacteria = allBacteria + context.system.actorOf(Props(new Bacteria(id = "Alice")))

    var report1: Report = null


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
        case "photo" => {
            photosynthesis = photosynthesis + 1
        }
        case "steril" => {
            sterilization = sterilization + 1
        }
        case "preserv" => {
            preservationOverFertility = preservationOverFertility + 1
        }
        case "report" => {
            if(report1 == null){
                report1 = new Report(allBacteria.size, 0, photosynthesis,
                                        sterilization, preservationOverFertility)
            } else {
                report1.deaths = report1.survivals - allBacteria.size
                context.system.actorSelection("/user/Stat") ! report1

                for(bacteria <- allBacteria)
                    bacteria ! PoisonPill
                self ! PoisonPill
            }
        }
        case _ => {
            println("That's awkward. You shouldn't be here...")
        }
    }
}
