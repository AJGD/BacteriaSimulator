package main.scala

class Report(var survivals: Integer = 0, var deaths: Integer = -1,
                var map: collection.mutable.Map[Mutation, Integer] = collection.mutable.Map()) {
    def oneMore(m: Mutation) = {
        if(map contains m){
            map update (m, map(m) + 1)
        } else {
            map += (m -> 1)
        }
    }

    def print() = {
        println(s"\n${survivals} was bacteria max population.\n")
        map.foreach {case (key, value) => println(s" > ${value} evolved to ${key.name()}.")}
        println(s"\nAfter antibiotic ${deaths} bacteria died.\n")
    }
}
