import akka.actor.{ActorSystem, Props}
import akka.testkit.{ ImplicitSender, TestProbe, TestKit }
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import main.scala.{Bacteria, Spectinomycin}

class BacteriaSpec() extends TestKit(ActorSystem("BacteriaSpec")) with ImplicitSender
    with WordSpecLike with Matchers with BeforeAndAfterAll {

    override def afterAll {
      TestKit.shutdownActorSystem(system)
      }

      import main.scala.RandomGenerator.randomMutation

      "A Bacteria with zero antibiotic resistance" must {
        "die when exposed to spectinomycin" in {
          val probe = TestProbe()
          val bacteria = system.actorOf(Props(new Bacteria(id = "bacteria", antibioticResistance = 0.0)))
          probe watch bacteria
          bacteria ! Spectinomycin()
          probe.expectTerminated(bacteria)
        }
      }
  }
