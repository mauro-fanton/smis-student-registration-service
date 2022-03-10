package module

import actors.StudentManager
import akka.actor.{ActorSystem, Props}
import play.api.Logger

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ActorSystemInitializer @Inject()(system: ActorSystem)(implicit ex: ExecutionContext ){

  private val LOGGER = Logger(getClass)

  LOGGER.info("Initializing Actor system")

  val userManagerProps = Props(
    new StudentManager(StudentManager.ID)
  )

  system.actorOf(userManagerProps, StudentManager.NAME)
}
