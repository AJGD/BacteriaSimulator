//A:
//antybiotyk ~ zmiany w bakteriach -> A

//testy jednostkowe -> A


//W:
//przekaźnik -> W

//sensowny output -> W

//wielokrotne wykonanie -> W

//Sugestia: klasa BacteriaKeeper chyba może się przydać w kwestii outputowania zbiorowych informacji o bakteriach bo zawiera zbiór ich wszystkich ^^


package main.scala

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

//fun fact: both plural and singular of bacteria is "bacteria". One bacteria. Two bacteria. etc.



object BacteriaSimulatorApp extends App {
  //this creates the system in which the Bacteria run.
  val system = ActorSystem("BacteriaEnvironment")
  //this adds the first Bacteria - this one is called Alice.
  val bacteriaKeeper = system.actorOf(Props(new BacteriaKeeper), "BacteriaKeeper")
  val firstBacteria = system.actorOf(Props(new Bacteria(id = "Alice")))
  //TODO: a more compact way to describe the Bacteria that run around?
  //TODO: DEFINITELY there should be a way to count them and display, like, "3 Photosynthetic Bacteria, 4 Sterilized Bacteria" etc
  bacteriaKeeper ! AddNewBacteria(firstBacteria)
  //bacteriaKeeper is responsible for keeping track of all bacteria considered active. Bacteria's responses when cloning or dying all contain appropriate messages to this actor.

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
  Thread.sleep(100)
  for (_ <- 1 to 3) {
    sendToAllBacteria("clone yourself")
    Thread.sleep(100)
  }
  println("Before antibiotics:")
  sendToAllBacteria("your name?")
  println("")
  sendToAllBacteria(Spectinomycin())
  Thread.sleep(100)
  println("After antibiotics:")
  sendToAllBacteria("your name?")
  println("")
  system.terminate()
}
