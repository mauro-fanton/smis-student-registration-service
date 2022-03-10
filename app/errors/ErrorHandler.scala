package errors

import play.api.Logger
import play.api.mvc.{Result, Results}

object ErrorHandler {

  val handleException: PartialFunction[Exception, Result] = {
    case notFoundEx: NotFoundException => Results.NotFound(notFoundEx.getMessage)
    case e: Exception => Results.InternalServerError(Option(e.getMessage).getOrElse("Unknown Error"))
  }

  def handleExceptionWithLogs(e: Exception, l: Logger, msg: String = ""): Result = {
    if(msg.isEmpty) l.info(e.getMessage, e) else l.info(msg, e)
    handleException(e)
  }
}
