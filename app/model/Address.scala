package model

case class Address(
  houseNumber: Int,
  streetName: String,
  city: String,
  county: Option[String] = None,
  postCode: String)

