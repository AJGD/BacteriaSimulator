package main.scala
import akka.actor.{Actor, ActorRef, ActorSystem, Props, PoisonPill}
import scala.collection.mutable.MutableList

class StatisticsGen() extends Actor {
    val data: MutableList[Report] = MutableList();
  def receive = {
    case report: Report => {
      data += report
    }
    case "Summary" => {
      for(report <- data) {
          println(s"${report.survivals} was bacteria max population.\n\n" +
                  s" > ${report.photosynthesis} evolved to photosynthesis.\n" +
                  s" > ${report.sterilization} evolved to sterilization.\n" +
                  s" > ${report.preservationOverFertility} evolved to preservationOverFertility.\n")
         println(s"After antibiotic ${report.deaths} bacteria died.")
      }
      println(s"\n\n###On avarage:\n\n" +
              s"${data.foldLeft(0)((retVal: Int, report: Report) => retVal + report.survivals).toDouble / data.size} was bacteria max population.\n\n" +
              s" > ${data.foldLeft(0)((retVal: Int, report: Report) => retVal + report.photosynthesis).toDouble / data.size} evolved to photosynthesis.\n" +
              s" > ${data.foldLeft(0)((retVal: Int, report: Report) => retVal + report.sterilization).toDouble / data.size} evolved to sterilization.\n" +
              s" > ${data.foldLeft(0)((retVal: Int, report: Report) => retVal + report.preservationOverFertility).toDouble / data.size} evolved to preservationOverFertility.\n")
      println(s"After antibiotic ${data.foldLeft(0)((retVal: Int, report: Report) => retVal + report.deaths).toDouble / data.size} bacteria died")
    }
    case _ => println("The heck?")
  }
}
