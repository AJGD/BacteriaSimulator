package main.scala
import akka.actor.{Actor, ActorRef, ActorSystem, Props, PoisonPill}

//Bacteria is a main class defining how a Bacteria behaves.
//id essentially serves as Bacteria's name, containing its basic mutation/cloning history.
//antibioticResistance, cloneChance and mutationChance describes how suspectible the particular Bacteria is to some events.
//mutations is the set that contains all the mutations the Bacteria possesses.
//Bacteria's name's meaning:
// --> adjectives at the beginning describe the mutations (newer first)
// --> number at the and describe how it was cloned (bacteria called X changes to two copies of itself, X0 and X1)
class Bacteria(var id: String, var antibioticResistance: Double = 0.3,
               var cloneChance: Double = 0.8, var mutationChance: Double = 0.7,
               var mutations: Set[Mutation] = Set()
              ) extends Actor {

  import RandomGenerator.randomNum

  //to mutate a Bacteria, send a Mutation to it. There is no guarantee of success, though (mutationChance!)
  //sending a string "clone yourself" yields a chance of a Bacteria duplicating itself.
  def receive = {
    case m: PreservationOverFertility => {
    if (randomNum() < mutationChance && !(mutations contains m) && !(mutations contains FertilityOverPreservation())) {
        m.mutate(this)
        mutations  = mutations + m
        adjustStats
      }
    }
    case m: FertilityOverPreservation => {
      if (randomNum() < mutationChance && !(mutations contains m) && !(mutations contains PreservationOverFertility())) {
          m.mutate(this)
          mutations  = mutations + m
          adjustStats
        }
    }
    case m: Mutation => {
      if (randomNum() < mutationChance && !(mutations contains m)) {
          m.mutate(this)
          mutations  = mutations + m
          adjustStats
        }
    }
    case "your name?" => {
        mutations.foreach{context.system.actorSelection("/user/BacteriaKeeper") ! _}
    }
    case a: Antibiotic => {
      if(randomNum() > antibioticResistance)
      a.endanger(this)
    }
    case "clone yourself" => {
      if (randomNum() < cloneChance) {
        val newId = id + "1"
        val newCloneChance = if(cloneChance <0.05) 0 else cloneChance - 0.05
        id = id + "0"
        cloneChance =newCloneChance
        context.system.actorSelection("/user/BacteriaKeeper") ! AddNewBacteria(context.system.actorOf(Props(
          new Bacteria(id = newId, cloneChance = newCloneChance))))
      }
    }
    case _ => println("I don't understand that message. I'm just a bacteria.")
  }

  def wither() = {
    context.system.actorSelection("/user/BacteriaKeeper") ! RemoveBacteria(self)
    self ! PoisonPill
  }

  def adjustStats() = {
    if(antibioticResistance<0.0) antibioticResistance = 0.0
    if(antibioticResistance>1.0) antibioticResistance = 1.0
    if(cloneChance<0.0) cloneChance = 0.0
    if(cloneChance>1.0) cloneChance = 1.0
    if(mutationChance<0.0) mutationChance = 0.0
    if(mutationChance>1.0) mutationChance = 1.0
  }
}
