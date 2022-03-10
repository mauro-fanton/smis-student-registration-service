package actors

import actors.StudentActor.StudentRegisteredEvent
import akka.persistence.typed.PersistenceId
import akka.persistence.{PersistentActor, Recovery, SnapshotOffer}
import config.EventJacksonSerializer
import dto.State
import model.Student

object StudentActor {

  //Command
  case class RegisterStudentCommand(student: Student) extends Command
  case object GetStudent

  //Response
  case class StudentRegistrationSuccess(user: Student)
  case class StudentRegistrationFailed(cause: Throwable)
  case class StudentRetrievalSuccess(user: Student)
  case object StudentRetrievalFailure

  //Event
  case class  StudentRegisteredEvent(student: Student) extends TaggedEvent with EventJacksonSerializer

}

//  case class State(history: List[String] = Nil) extends  EventJacksonSerializer {
//    def updateState(applicationNumber: Option[String]): State = {
//      applicationNumber match {
//      case Some(a) => copy(a :: history)
//      case None => copy(history)
//    }
//  }
//  def size: Int = history.length
//  override def toString: String = history.reverse.toString
//}
/**
 * Event sourced actor to persist student events to event log
 *
 */
class StudentActor(override val persistenceId: String) extends PersistentActor {

  import StudentActor._

  var state = State()

  override def receiveCommand: Receive = {
    case RegisterStudentCommand(student) =>
      persist(StudentRegisteredEvent(student)) {
        event =>
          state = updateState(event)
          sender() ! StudentRegistrationSuccess(student)
      }
  }

  override def recovery = Recovery.none
  override def receiveRecover: Receive = {
   // case evt: StudentRegisteredEvent => updateState(evt)
    case SnapshotOffer(_, snapshot: State) => state = snapshot
  }

  private def updateState(evt: StudentRegisteredEvent): State = {
    state.updateState(evt.student.applicationNumber)
  }
}
