package model

import play.api.libs.functional.syntax._
import play.api.libs.json._

import java.time.LocalDate;

case class Student
(
  id: Option[String],
  applicationNumber: Option[String],
  firstName: String,
  surname: String,
  dob: LocalDate,
  address: Address,
  primaryGuardianName: String,
  secondaryGuardianName: Option[String] = None,
  primaryTelephoneNum: String,
  secondaryTelephoneNum: Option[String] = None,
  activities: Option[List[Activity]] = None)


object Student {
  /**
   * Mapping to read/write a PostResource out as a JSON value.
   */
  implicit val studentFormat: OFormat[Student] = Json.format[Student]

  implicit val locationReads: Reads[Student] = (
    (JsPath \ "id").readNullable[String] and
      (JsPath \ "applicationNumber").readNullable[String] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "surname").read[String] and
      (JsPath \ "dob").read[LocalDate] and
      (JsPath \ "address").read[Address] and
      (JsPath \ "primaryGuardianName").read[String] and
      (JsPath \ "secondaryGuardianName").readNullable[String] and
      (JsPath \ "primaryTelephoneNum").read[String] and
      (JsPath \ "secondaryTelephoneNum").readNullable[String] and
      (JsPath \ "activities").readNullable[List[Activity]]
    )(Student.apply _)
}
