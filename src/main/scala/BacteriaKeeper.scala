package main.scala
import akka.actor.{Actor, ActorRef}

// BacteriaKeeper is used to keep track of active bacteria.
// if you have an ActorRef to an instance of BacteriaKeeper, you can easily send a message to all Bacteria sending SendAll(yourMessage) to said instance.

trait BacteriaKeeperMessage
case class AddNewBacteria(b:ActorRef) extends BacteriaKeeperMessage
case class SendAll(m:Any) extends BacteriaKeeperMessage
case class RemoveBacteria(b:ActorRef) extends BacteriaKeeperMessage

class BacteriaKeeper(var allBacteria: Set[ActorRef] = Set()) extends Actor{
  def receive = {
    case AddNewBacteria(bacteria) => {
      allBacteria = allBacteria + bacteria
    }
    case SendAll(message) => {
      for(bacteria <- allBacteria)
        bacteria ! message
    }
    case RemoveBacteria(bacteria) => {
      allBacteria = allBacteria - bacteria
    }
    case _ => {}
  }
}
