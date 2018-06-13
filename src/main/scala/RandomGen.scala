package main.scala


//this is just a little helper that provides random numbers. May provide random Mutations etc.
object RandomGenerator {
  val r: scala.util.Random = scala.util.Random

  def randomNum(): Double = r.nextDouble()
}
