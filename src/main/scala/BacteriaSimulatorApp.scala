package main.scala

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

//fun fact: both plural and singular of bacteria is "bacteria". One bacteria. Two bacteria. etc.

object BacteriaSimulatorApp extends App {
    var simulations: Integer = 1
    if(args.size > 0) {
        //some naive args parsing
        try {
            simulations = args(0).toInt
        } catch {
            case nfe: NumberFormatException =>
                println("Wrong arguments format.\n Making 1 sim, 1 cycle.")
            case e: Exception =>
                println("Unexpected args exception. Proceeding with defaults.")
        }
    }

    //this creates the system in which the Bacteria run.
    val system = ActorSystem("BacteriaEnvironment")
    val stat = system.actorOf(Props(new StatisticsGen()), "Stat")
    for(i <- 1 to simulations) {
        println(s"Simulation $i")
      system.actorOf(Props(new OrdersGenerator()), s"OrderGenerator$i")
      Thread.sleep(5000)
  }
    scala.io.StdIn.readLine()
    system.terminate()
}
