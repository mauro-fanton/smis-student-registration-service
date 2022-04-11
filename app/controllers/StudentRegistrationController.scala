package controllers

import errors.ErrorHandler.handleExceptionWithLogs
import model.Student
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StudentRegistrationController @Inject()
(cc: StudentControllerComponents)
(implicit ec: ExecutionContext)
extends StudentBaseController(cc) {


  private val logger = Logger(getClass)

  def student(id: String): Action[AnyContent] = StudentAction.async {
    implicit request =>
      logger.info(s"Getting student with id: $id")
      process[String](studentService.lookUp, id)
  }

  def students(): Action[AnyContent] = StudentAction.async {
    implicit request =>
      logger.trace("Getting all Registered Student")

      (
        for {
          students <- studentService.getAll
        } yield Ok(Json.toJson(students))
        ).recover {
        case e:Exception => handleExceptionWithLogs(e, logger, s"Error on retrieving all the students")
      }
  }

  def register: Action[AnyContent] = StudentAction.async {
    implicit request =>
      val studentInfo = request.body.asJson
      logger.info("registering new Student...")

      studentInfo.map(s => process(studentService.addStudentDto, s.as[Student])).getOrElse(Future{BadRequest("Could not register student")})
  }

  private def process[A](f: A => Future[Option[Student]], o: A) = {
    (
      for {
        student <- f(o)
      } yield Ok(Json.toJson(student))
      ).recover {
      case e: Exception => handleExceptionWithLogs(e, logger)
    }
  }
}
