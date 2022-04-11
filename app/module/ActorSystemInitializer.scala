package module

import actors.{StudentManager, SupervisorActor}
import akka.actor.typed.scaladsl.adapter.ClassicActorSystemOps
import akka.actor.{ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import akka.pattern.{BackoffOpts, BackoffSupervisor}
import akka.projection.ProjectionBehavior
import constants.EventsTags
import play.api.Logger
import projections.StudentProjection

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

@Singleton
class ActorSystemInitializer @Inject()(system: ActorSystem)(implicit ex: ExecutionContext ){

  private val LOGGER = Logger(getClass)

  LOGGER.info("Initializing Actor system")

  val childProps = Props(new StudentManager(StudentManager.ID))
  system.actorOf(SupervisorActor.createSupervisorWithOnFailureStrategy(childProps), StudentManager.Name)

  val typedSystem = system.toTyped
  typedSystem.systemActorOf(ProjectionBehavior(StudentProjection.projection(typedSystem, EventsTags.STUDENT_REGISTERED_EVENT)), StudentProjection.ProjectionName)
}
