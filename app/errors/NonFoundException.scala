package errors

trait CustomException
final case class NotFoundException(message: String) extends Exception(message) with CustomException

