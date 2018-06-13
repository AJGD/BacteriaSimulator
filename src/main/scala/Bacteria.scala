package main.scala
import akka.actor.{Actor, ActorSystem, Props}

//Bacteria is a main class defining how a Bacteria behaves.
//id essentially serves as Bacteria's name, containing its basic mutation/cloning history.
//antibioticResistance, cloneChance and mutationChance describes how suspectible the particular Bacteria is to some events.
//they may change with time.
//mutations is the set that contains all the mutations the Bacteria possesses.
//Bacteria's name's meaning:
// --> adjectives at the beginning describe the mutations (newer first)
// --> number at the and describe how it was cloned (bacteria called X changes to two copies of itself, X0 and X1)
class Bacteria(var id: String, var antibioticResistance: Double = 0.1,
               var cloneChance: Double = 0.9, var mutationChance: Double = 0.7,
               var mutations: Set[Mutation] = Set()
              ) extends Actor {

  import RandomGenerator.randomNum

  //to mutate a Bacteria, send a Mutation to it. There is no guarantee of success, though (mutationChance!)
  //sending a string "clone yourself" yields a chance of a Bacteria duplicating itself.
  //asking "your name?" means that the Bacteria prints its id.
  def receive = {
    case m: Mutation => {
      if (randomNum() < mutationChance && !(mutations contains m))
        m.mutate(this)
      mutations  = mutations + m
    }
    case "your name?" => print(id + ", ")
    case "clone yourself" => {
      if (randomNum() < cloneChance) {
        val newId = id + "1"
        val newCloneChance = if(cloneChance <0.1) 0 else cloneChance - 0.1
        id = id + "0"
        cloneChance =newCloneChance
        context.system.actorOf(Props(new Bacteria(newId, cloneChance = newCloneChance)))
      }
    }
    case _ => println("I don't understand that message. I'm just a bacteria.")
  }
}
