//A:
//antybiotyk ~ zmiany w bakteriach -> A

//testy jednostkowe -> A


//W:
//przekaźnik -> W

//sensowny output -> W

//wielokrotne wykonanie -> W





package main.scala

import akka.actor.{Actor, ActorSystem, Props}

//fun fact: both plural and singular of bacteria is "bacteria". One bacteria. Two bacteria. etc.



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
