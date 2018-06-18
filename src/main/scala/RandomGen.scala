package main.scala


//this is just a little helper that provides random numbers and Mutations.
object RandomGenerator {
  val r: scala.util.Random = scala.util.Random

  def randomNum(): Double = r.nextDouble()

  def randomMutation(): Mutation = {
    val n = 4
    val result = n*randomNum()
    if(result<1.0) return Photosynthesis()
    if(result<2.0) return Sterilization()
    if(result<3.0) return PreservationOverFertility()
    return FertilityOverPreservation()
  }
}
