package model

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Address(
  houseNumber: Int,
  streetName: String,
  city: String,
  county: Option[String] = None,
  postCode: String)

object Address {

  implicit val addressFormat: OFormat[Address] = Json.format[Address]

  implicit val read: Reads[Address] = (
  (JsPath \ "houseNumber").read[Int] and
    (JsPath \ "streetName").read[String] and
    (JsPath \ "city").read[String] and
    (JsPath \ "county").readNullable[String] and
    (JsPath \ "postCode").read[String]
  )(Address.apply _)
}

