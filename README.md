domain
======
[![Build Status](https://travis-ci.org/hmrc/domain.svg)](https://travis-ci.org/hmrc/domain) [ ![Download](https://api.bintray.com/packages/hmrc/releases/domain/images/download.svg) ](https://bintray.com/hmrc/releases/domain/_latestVersion)

Micro-library for typing and validating UK tax identifiers.

#### Change History
v12.0 - 15 April 25
- remove support for scala 2.13
- remove support for play 2.8 and play 2.9
- update library dependencies

v11.0 - 17 Mar 25
- update library dependencies

v10.0 - 17 Jun 24
- add Scala 3.0 support. Remove Scala 2.12 support, noting Scala 2.13 is still supported.
- update library dependencies

v9.0 - 05 Dec 23
- add support for Play 2.9 & Play 3.0, noting Play 2.8 is still supported
- update Scala 2.13 version
- update library dependencies
- add 'Change History' section to README

v8.3.0 - 23 May 22
- update broken sbt build

v8.1.0 - 06 May 22
- update library dependencies

v8.0.0 - 14 Mar 22
- add Scala 2.13 support

v7.0.0 - 21 Jan 22
- drop Play 2.6 and Play 2.7 support

v6.0.0 - 24 Jun 21
- Scala 2.12 only and add Play 2.8 support

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

implicit val format = TaxIds.format(TaxIds.defaultSerialisableIds: _*)

val input =
  """{
    |"nino": "NM439088A",
    |"sautr": "some-sa-utr"
        }""".stripMargin
val taxIds = Json.parse(input).as[TaxIds]

taxIds.nino // Some(NM439088A)
taxIds.saUtr // Some(some-sa-utr)
taxIds.ctUtr // None
```

### Installing
Add the following to your SBT build:
```scala
libraryDependencies += "uk.gov.hmrc" %% "domain-play-[PLAY VERSION]" % "[LIB VERSION]"
```
for example
```scala
libraryDependencies += "uk.gov.hmrc" %% "domain-play-30" % "12.0.0"
```

#### Before version 9.x.x
Add the following to your SBT build:
```scala
libraryDependencies += "uk.gov.hmrc" %% "domain" % "[LIB VERSION]-play-[PLAY VERSION]"
```
for example
```scala
libraryDependencies += "uk.gov.hmrc" %% "domain" % "8.3.0-play-28"
```

## License ##
 
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

