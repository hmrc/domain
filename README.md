domain
======

Micro-library for typing and validating UK tax identifiers.

### Identifiers

Types are provided for many common tax identifiers, such as:

* [Employment Reference](src/main/scala/uk/gov/hmrc/domain/EmpRef.scala)
* [National Insurance Number](src/main/scala/uk/gov/hmrc/domain/Nino.scala)
* Unique Taxpayer References - [Self Assessment](src/main/scala/uk/gov/hmrc/domain/SaUtr.scala), 
[Corporation Tax](src/main/scala/uk/gov/hmrc/domain/CtUtr.scala)
* [Unique Agent Reference](src/main/scala/uk/gov/hmrc/domain/Uar.scala)
* [Vehicle Registration Number](src/main/scala/uk/gov/hmrc/domain/Vrn.scala)
* Agents - [Code](src/main/scala/uk/gov/hmrc/domain/AgentCode.scala), 
[UserId](src/main/scala/uk/gov/hmrc/domain/AgentUserId.scala), 
[PAYE Reference](src/main/scala/uk/gov/hmrc/domain/PayeAgentReference.scala)

`Reads` and `Writes` have been provided for Play's JSON library for all identifiers, and format validation is present for 
some.

JSON objects with tax identifiers as properties can be serialized or deserialized to a 
[TaxIds](src/main/scala/uk/gov/hmrc/domain/taxIds.scala):

```scala
import play.api.libs.json._
import uk.gov.hmrc.domain._

implicit val format =  TaxIds.format(TaxIds.defaultSerialisableIds :_*)

val input =
        """{
          |"nino": "NM439088A",
          |"sautr": "some-sa-utr"
        }""".stripMargin
val taxIds = Json.parse(input).as[TaxIds]

taxIds.nino  // Some(NM439088A)
taxIds.saUtr // Some(some-sa-utr)
taxIds.ctUtr // None
```

### Installing

Add the following to your SBT build:
```scala
libraryDependencies += "uk.gov.hmrc" % "domain" % "2.0.0"
```
