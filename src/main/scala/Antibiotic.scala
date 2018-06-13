package main.scala

trait Antibiotic{
  def endanger(b: Bacteria): Unit = {
    b.wither()
  }
}

case class Spectinomycin() extends Antibiotic{
  override def endanger(b: Bacteria): Unit  = {
    super.endanger(b)
  }
}
