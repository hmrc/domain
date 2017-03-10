domain
======
[![Build Status](https://travis-ci.org/hmrc/domain.svg)](https://travis-ci.org/hmrc/domain) [ ![Download](https://api.bintray.com/packages/hmrc/releases/domain/images/download.svg) ](https://bintray.com/hmrc/releases/domain/_latestVersion)

Micro-library for typing and validating UK tax identifiers.

#### Identifier Types

Types are provided for many common tax identifiers, such as:

* [Employment Reference (EmpRef)](src/main/scala/uk/gov/hmrc/domain/EmpRef.scala)
* [National Insurance Number (NINO)](src/main/scala/uk/gov/hmrc/domain/Nino.scala)
* Unique Taxpayer References (UTR) - [Self Assessment](src/main/scala/uk/gov/hmrc/domain/SaUtr.scala), 
[Corporation Tax](src/main/scala/uk/gov/hmrc/domain/CtUtr.scala), [Annual Tax on Enveloped Dwellings](src/main/scala/uk/gov/hmrc/domain/AtedUtr.scala),
[Pension Scheme Administrator ID](src/main/scala/uk/gov/hmrc/domain/PsaId.scala),
[Pension Scheme Practitioner ID](src/main/scala/uk/gov/hmrc/domain/PspId.scala),
[Alcohol Wholesale Registration Scheme](src/main/scala/uk/gov/hmrc/domain/AwrsUtr.scala),
* [Unique Agent Reference (UAR)](src/main/scala/uk/gov/hmrc/domain/Uar.scala)
* [VAT Registration Number (VRN)](src/main/scala/uk/gov/hmrc/domain/Vrn.scala)
* Agents - [Code](src/main/scala/uk/gov/hmrc/domain/AgentCode.scala), 
[UserId](src/main/scala/uk/gov/hmrc/domain/AgentUserId.scala), 
[PAYE Reference](src/main/scala/uk/gov/hmrc/domain/PayeAgentReference.scala),
[Agent Business](src/main/scala/uk/gov/hmrc/domain/AgentBusinessUtr.scala)

#### JSON handling

`Reads` and `Writes` have been provided for Play's JSON library for all identifiers, and format validation is present for 
some. JSON objects with multiple tax identifiers as properties can be serialized or deserialized to a 
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
resolvers += Resolver.bintrayRepo("hmrc", "releases")

libraryDependencies += "uk.gov.hmrc" %% "domain" % "[INSERT VERSION]"
```

Use versions >=2.11.0 for Java 7 and Play json 2.3.x

## License ##
 
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
