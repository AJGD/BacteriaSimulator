package main.scala

class Report(var survivals: Integer, var deaths: Integer, var map: collection.mutable.Map[Mutation, Integer]) {
    def oneMore(m: Mutation) = {
        if(map contains m){
            map update (m, map(m) + 1)
        } else {
            map += (m -> 1)
        }
    }
}
