package dto

import config.EventJacksonSerializer

case class State(history: List[String] = Nil) extends  EventJacksonSerializer {
  def updateState(applicationNumber: Option[String]): State = {
    applicationNumber match {
      case Some(a) => copy(a :: history)
      case None => copy(history)
    }
  }

  def contains(elem: Option[String]): Boolean = {
    elem.exists(history.contains(_))
    //this.history.contains(elem)
  }

  def print(): Unit  = {
    println("==================================")
    history.foreach(println(_))
    println("================================")
  }
}
