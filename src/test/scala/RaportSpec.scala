package test.scala

import org.scalatest._
//import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import main.scala.{ Report, Photosynthesis, Sterilization, PreservationOverFertility, FertilityOverPreservation}

class ReportSpec() extends FlatSpec with Matchers {
    "A Raport" should "create with one or increment by one value assigned to mutation" in {
        val report = new Report(0, 0, collection.mutable.Map())
        for(i <- 1 to 10) {
            report.oneMore(Photosynthesis())
            report.oneMore(Sterilization())
            report.oneMore(PreservationOverFertility())
            report.oneMore(FertilityOverPreservation())

            report.map(Photosynthesis()) should be  (i)
            report.map(Sterilization()) should be (i)
            report.map(PreservationOverFertility()) should be (i)
            report.map(FertilityOverPreservation()) should be (i)
        }
    }

    it should "throw NoSuchElementException if an element isn't found" in {
        val emptyReport = new Report(0, 0, collection.mutable.Map())
        a [NoSuchElementException] should be thrownBy {
            emptyReport.map(Photosynthesis())
            emptyReport.map(Sterilization())
            emptyReport.map(PreservationOverFertility())
            emptyReport.map(FertilityOverPreservation())
        }
    }
 }
