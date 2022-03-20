package actors

import akka.actor.ActorLogging
import akka.event.LoggingReceive
import akka.persistence.{PersistentActor, Recovery, SnapshotOffer}
import config.EventJacksonSerializer
import dto.State
import model.Student

import java.util.UUID

object StudentActor {

  val Id = s"student-actor-${UUID.randomUUID().toString}"
  val Name = "student-actor"
  //Command
  case class RegisterStudentCommand(student: Student) extends Command

  //Response
  case class StudentRegistrationSuccess(user: Student)
  case class StudentRegistrationFailed(cause: Throwable)
  case class StudentRetrievalSuccess(user: Student)
  case object StudentRetrievalFailure

  //Event
  case class StudentRegisteredEvent(student: Student) extends TaggedEvent with EventJacksonSerializer

}

/**
 * Event sourced actor to persist student events to event log
 *
 */
class StudentActor(override val persistenceId: String) extends PersistentActor with ActorLogging{

  import StudentActor._

  var state = State()

  override def receiveCommand: Receive = LoggingReceive {
    case RegisterStudentCommand(student) =>
      log.info("Persisting Student {}", student)
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
