package errors

object ErrorMessages {

  sealed trait ErrorMessage {
    def errorMsg: String
  };

  final case class NotFoundMsg[A](obj: A) extends ErrorMessage {
    override def errorMsg: String = ???
  }

  final case class GetAllErrMsg[A](obj: A) extends ErrorMessage
  {
    override def errorMsg: String = ???
  }

  final case class AddRecordErrorMsg[A](obj: A) extends  ErrorMessage {
    override def errorMsg: String = ???
  }


}
