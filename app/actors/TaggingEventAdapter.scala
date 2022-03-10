package actors

import actors.StudentActor.StudentRegisteredEvent
import akka.persistence.journal.{Tagged, WriteEventAdapter}
import constants.EventsTags

class TaggingEventAdapter extends WriteEventAdapter {

  override def toJournal(event: Any): Any = event match {
    case e: StudentRegisteredEvent => Tagged(event, Set(EventsTags.STUDENT_REGISTERED_EVENT))
    case _ => event
  }

  override def manifest(event: Any): String = ""

}
