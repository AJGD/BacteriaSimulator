package main.scala


//everything with this trait can change Bacteria's behaviour when sent to Bacteria.
trait Mutation {
  def mutate(b: Bacteria)
}

//here are the classes that currently implement Mutation.
case class Photosynthesis() extends Mutation {
  override def mutate(b: Bacteria): Unit = {
    b.id = "Photosynthetic" + b.id
    println(b.id + " shouts: I SHINE WITH THE POWER OF THE SUN")
  }
}

case class Sterilization() extends Mutation {
  override def mutate(b: Bacteria): Unit = {
    b.id = "Sterilized" + b.id
    b.cloneChance = 0.1
  }
}

case class PreservationOverFertility() extends Mutation {
  override def mutate(b: Bacteria): Unit = {
    b.cloneChance -= 0.2
    b.antibioticResistance += 0.2
  }
}
