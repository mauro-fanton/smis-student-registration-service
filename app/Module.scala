import com.google.inject.AbstractModule
import module.ActorSystemInitializer
import play.api.Logger
import play.api.libs.concurrent.AkkaGuiceSupport

class Module extends AbstractModule with AkkaGuiceSupport {

  private val logger = Logger(getClass)

  override def configure = {
    bind(classOf[ActorSystemInitializer]).asEagerSingleton()
  }
}
