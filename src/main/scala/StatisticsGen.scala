package main.scala
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import scala.collection.mutable.MutableList

class StatisticsGen() extends Actor {
    val data: MutableList[Report] = MutableList();
  def receive = {
    case report: Report => {
      data += report
    }
    case "Summary" => {
      for(report <- data) {
          println(s"\n${report.survivals} was bacteria max population.\n\n")
          report.map.foreach {case (key, value) => println(s" > ${value} evolved to ${key.name()}.\n")}
          println(s"After antibiotic ${report.deaths} bacteria died.\n")
      }

        println(s"\n\n###On average:\n\n" +
            s"${data.foldLeft(0)((retVal: Int, report: Report) => retVal + report.survivals).toDouble / data.size} was bacteria max population.\n\n")

        (data.foldLeft(new Report(0, 0, collection.mutable.Map()))
            ((retVal: Report, report: Report) => {
                report.map
                .foreach {case (key: Mutation, value: Integer) =>
                    if(retVal.map contains key) retVal.map update (key, retVal.map(key) + value)
                    else retVal.map += (key -> value)
                }
                retVal
            })
        ).map
        .foreach {case (key: Mutation, value: Integer) => println(s" > ${value.toDouble / data.size} evolved to ${key.name()}.\n")}

        println(s"After antibiotic ${data.foldLeft(0)((retVal: Int, report: Report) => retVal + report.deaths).toDouble / data.size} bacteria died")

    }
    case _ => println("The heck?")
  }
}
