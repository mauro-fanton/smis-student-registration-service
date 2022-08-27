package dto

import config.EventJacksonSerializer

case class State(history: List[String] = Nil) extends  EventJacksonSerializer {
  def updateState(applicationNumber: String): State = {
    copy(applicationNumber :: history)

  }

  def contains(elem: String): Boolean = {
    history.contains(elem)
    //this.history.contains(elem)
  }

  def print(): Unit  = {
    println("==================================")
    history.foreach(println(_))
    println("================================")
  }
}
