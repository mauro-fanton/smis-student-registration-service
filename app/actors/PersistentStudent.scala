package actors

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.pattern.StatusReply
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}
import config.EventJacksonSerializer
import model.Student

object PersistentStudent {

  final case class RegisterStudent(student: Student, replyTo: ActorRef[StatusReply[Student]]) extends Command
  final case class StudentRegistered(student: Student) extends Event

  final case class State(history: List[String] = Nil) extends EventJacksonSerializer {
    def isRegistered(appNumber: String): Boolean = history.contains(appNumber)


    def updateItem(applicationNumber: Option[String]): State = {
      applicationNumber match {
        case Some(a) => copy(a :: history)
        case None => copy(history)
      }
    }
  }



  val commandHandler: (String, State, Command) => Effect[Event, State] = { (appNumber, state, command) =>
    command match {
      case RegisterStudent(student, replayTo) =>
        if(state.isRegistered(appNumber)) {
          replayTo ! StatusReply.Error(s"Student $appNumber already registered")
          Effect.none
        }
        else Effect.persist(StudentRegistered(student))
        .thenRun(res => {
          state.updateItem(Option(appNumber))
          replayTo ! StatusReply.Success(student)
        })
    }
  }

  val eventHandler: (State, Event) => State = { (state, event) =>
    event match {
      case StudentRegistered(student) => state.updateItem(student.applicationNumber)
    }
  }

  def apply(applicationNumber: String): Behavior[Command] =
    EventSourcedBehavior[Command, Event, State](
      persistenceId = PersistenceId.ofUniqueId(applicationNumber),
      emptyState = State(Nil),
      (state, cmd) => commandHandler(applicationNumber, state, cmd),
      (state, evt) => eventHandler(state,evt))

}
