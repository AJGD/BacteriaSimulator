package main.scala

import akka.actor.{Actor, ActorSystem, Props}

//fun fact: both plural and singular of bacteria is "bacteria". One bacteria. Two bacteria. etc.

//everything with this trait can change Bacteria's behaviour when sent to Bacteria.
trait Mutation {
  def mutate(b: Bacteria)
}

//here are the classes that currently implement Mutation.
case class Photosynthesis() extends Mutation {
  override def mutate(b: Bacteria): Unit = {
    b.id = "Photosynthetic" + b.id
    println(b.id + " shouts: I SHINE WITH THE POWER OF THE SUN")
  }
}

case class Sterilization() extends Mutation {
  override def mutate(b: Bacteria): Unit = {
    b.id = "Sterilized" + b.id
    b.cloneChance = 0.1
  }
}

//this is just a little helper that provides random numbers. May provide random Mutations etc.
object RandomGenerator {
  val r: scala.util.Random = scala.util.Random

  def randomNum(): Double = r.nextDouble()
}

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

object BacteriaSimulatorApp extends App {
  //this creates the system in which the Bacteria run.
  val system = ActorSystem("BacteriaEnvironment")
  //this adds the first Bacteria - this one is called Alice.
  val firstBacteria = system.actorOf(Props(new Bacteria("Alice")))
  //system.actorSelection("/user/*") ! message  - means, in this context, sending the message to all Bacteria.
  //the code below just demonstrates how interesting it gets once there is more Bacteria...
  //TODO: a more compact way to describe the Bacteria that run around?
  //TODO: DEFINITELY there should be a way to count them and display, like, "3 Photosynthetic Bacteria, 4 Sterilized Bacteria" etc
  for (_ <- 1 to 3) {
    system.actorSelection("/user/*") ! "clone yourself"
    Thread.sleep(300)
  }
  system.actorSelection("/user/*") ! Photosynthesis()
  Thread.sleep(300)
  for (_ <- 1 to 2) {
    system.actorSelection("/user/*") ! "clone yourself"
    Thread.sleep(300)
  }
  system.actorSelection("/user/*") ! Sterilization()
  Thread.sleep(300)
  for (_ <- 1 to 2) {
    system.actorSelection("/user/*") ! "clone yourself"
    Thread.sleep(300)
  }
  println("Currently existing Bacteria:")
  system.actorSelection("/user/*") ! "your name?"
  println("")
}
