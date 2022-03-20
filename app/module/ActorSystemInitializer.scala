package module

import actors.{StudentManager, SupervisorActor}
import akka.actor.{ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import akka.pattern.{BackoffOpts, BackoffSupervisor}
import play.api.Logger

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

@Singleton
class ActorSystemInitializer @Inject()(system: ActorSystem)(implicit ex: ExecutionContext ){

  private val LOGGER = Logger(getClass)

  LOGGER.info("Initializing Actor system")

  //  val userManagerProps = Props(
  //    new StudentManager(StudentManager.ID)
  //  )
  val childProps = Props(new StudentManager(StudentManager.ID))

  /* BackoffSupervisor
      Start a child actor again when it fails. You can specify a min and max backof time which
      gives to the actor some time to restart. For example in  case that some resource stop
      working and need to restart with teh actor

      The following Scala snippet shows how to create a backoff supervisor which will start the given
      StudentManager actor after it has crashed because of some exception, in increasing intervals
      of 3, 6, 12, 24 and finally 30 seconds:
   */
  system.actorOf(SupervisorActor.createSupervisorWithOnFailureStrategy(childProps), StudentManager.Name)

//  val userManagerProps = Props(
//    new StudentManager(StudentManager.ID)
//  )
//
//  system.actorOf(userManagerProps, StudentManager.NAME)
}
