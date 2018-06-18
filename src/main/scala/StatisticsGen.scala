package main.scala
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import scala.collection.mutable.MutableList

// keeps old Reports for the sake of calculating averages.
class StatisticsGen() extends Actor {
    val data: MutableList[Report] = MutableList();
  def receive = {
    case report: Report => {
      data += report
      report.print()
    }
    case "Summary" => {
      //data.foreach(_.print())

        println(s"\n\n###On average:\n\n" +
            s"${data.foldLeft(0)((retVal: Int, report: Report) => retVal + report.survivals).toDouble / data.size} was bacteria max population.\n")

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
        .foreach {case (key: Mutation, value: Integer) => println(s" > ${value.toDouble / data.size} evolved to ${key.name()}.")}

        println(s"After antibiotic ${data.foldLeft(0)((retVal: Int, report: Report) => retVal + report.deaths).toDouble / data.size} bacteria died")

    }
    case _ => println("The heck?")
  }
}
