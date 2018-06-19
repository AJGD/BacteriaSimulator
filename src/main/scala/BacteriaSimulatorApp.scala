package main.scala

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

//fun fact: both plural and singular of bacteria is "bacteria". One bacteria. Two bacteria. etc.

object BacteriaSimulatorApp extends App {
    var simulations: Integer = 1
    var cloneRate:Int = 3
    var cloneMutateCycles:Int = 3
    var cycles:Int = 10
    val proceedingWithDefaults = "Unexpected number of arguments. Proceeding with defaults."
    if(args.size == 1) {
        try {
            simulations = args(0).toInt
        } catch {
            case nfe: NumberFormatException =>
                println("Wrong arguments format.\n Making 1 sim, 1 cycle.")
            case e: Exception =>
                println(proceedingWithDefaults)
        }
    } else if(args.size == 4){
        try {
            simulations = args(0).toInt
            cloneRate = args(1).toInt
            cloneMutateCycles = args(2).toInt
            cycles = args(3).toInt
        } catch {
            case nfe: NumberFormatException =>
                println("Wrong arguments format.")
            case e: Exception =>
                println(proceedingWithDefaults)
        }
    } else println(proceedingWithDefaults)

    for(i <- 1 to simulations) {
        println(s"Simulation $i")
        //this creates the system in which the Bacteria run.
        val system = ActorSystem("BacteriaEnvironment")
        val stat = system.actorOf(Props(new StatisticsGen()), "Stat")
        system.actorOf(Props(new OrdersGenerator(cloneRate, cloneMutateCycles, cycles)), s"OrderGenerator$i")

        scala.io.StdIn.readLine()
        system.terminate()
    }
}
