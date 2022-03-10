package routers

import controllers.StudentRegistrationController
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter

import javax.inject.Inject
import play.api.routing.sird._

class StudentRouter @Inject()(controller: StudentRegistrationController)
  extends SimpleRouter {

  val prefix = "/smis"

  override def routes: Routes = {
    case GET(p"/student/$id") => controller.student(id)
    case GET(p"/students") => controller.students()
    case POST(p"/register/student") =>  controller.register()
  }
}
