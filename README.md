domain
======
![](https://img.shields.io/github/v/release/hmrc/domain)

Micro-library for typing and validating UK tax identifiers.

## Change History
v13.0 - 01 Aug 2025
- added additional Test Generators; now full coverage
- use the test jar to obtain the generators
- additional README clarifications

v12.0 - 15 April 2025
- remove support for scala 2.13
- remove support for play 2.8 and play 2.9
- update library dependencies

v11.0 - 17 Mar 2025
- update library dependencies

v10.0 - 17 Jun 2024
- add Scala 3.0 support. Remove Scala 2.12 support, noting Scala 2.13 is still supported.
- update library dependencies

v9.0 - 05 Dec 2023
- add support for Play 2.9 & Play 3.0, noting Play 2.8 is still supported
- update Scala 2.13 version
- update library dependencies
- add 'Change History' section to README

## Identifier Types

Types are provided for many common tax identifiers, such as:

* [Employment Reference (EmpRef)](domain-play-30/src/main/scala/uk/gov/hmrc/domain/EmpRef.scala)
* [National Insurance Number (NINO)](domain-play-30/src/main/scala/uk/gov/hmrc/domain/Nino.scala)
* Unique Taxpayer References (UTR) - [Self Assessment](domain-play-30/src/main/scala/uk/gov/hmrc/domain/SaUtr.scala), 
[Corporation Tax](domain-play-30/src/main/scala/uk/gov/hmrc/domain/CtUtr.scala), [Annual Tax on Enveloped Dwellings](domain-play-30/src/main/scala/uk/gov/hmrc/domain/AtedUtr.scala),
[Pension Scheme Administrator ID](domain-play-30/src/main/scala/uk/gov/hmrc/domain/PsaId.scala),
[Pension Scheme Practitioner ID](domain-play-30/src/main/scala/uk/gov/hmrc/domain/PspId.scala),
[Alcohol Wholesale Registration Scheme](domain-play-30/src/main/scala/uk/gov/hmrc/domain/AwrsUtr.scala),
* [Unique Agent Reference (UAR)](domain-play-30/src/main/scala/uk/gov/hmrc/domain/Uar.scala)
* [VAT Registration Number (VRN)](domain-play-30/src/main/scala/uk/gov/hmrc/domain/Vrn.scala)
* [Economic Operators Registration and Identification (EORI)](domain-play-30/src/main/scala/uk/gov/hmrc/domain/Eori.scala)
* Agents - [Code](domain-play-30/src/main/scala/uk/gov/hmrc/domain/AgentCode.scala), 
[UserId](domain-play-30/src/main/scala/uk/gov/hmrc/domain/AgentUserId.scala), 
[PAYE Reference](domain-play-30/src/main/scala/uk/gov/hmrc/domain/PayeAgentReference.scala),
[Agent Business](domain-play-30/src/main/scala/uk/gov/hmrc/domain/AgentBusinessUtr.scala)

## JSON handling

`Reads` and `Writes` have been provided for Play's JSON library for all identifiers, and format validation is present for 
some. JSON objects with multiple tax identifiers as properties can be serialized or deserialized to a 
[TaxIds](domain-play-30/src/main/scala/uk/gov/hmrc/domain/taxIds.scala):

```scala
import play.api.libs.json._
import uk.gov.hmrc.domain._

implicit val format: Format[TaxIds] = TaxIds.format(TaxIds.defaultSerialisableIds: _*)

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

## Test Generator
You must install the Test library to access the Test Generators. See further info in the [Installing section](#Installing)

### List of Generators
- `AtedUtrGenerator` - Rules for the AtedUtr: https://design.tax.service.gov.uk/hmrc-design-patterns/unique-taxpayer-reference/
- `EoriGenerator` - Rules for the EORI: https://design.tax.service.gov.uk/hmrc-design-patterns/eori-numbers/
- `NinoGenerator` - Rules for the Nino: https://en.wikipedia.org/wiki/National_Insurance_number#Format
- `SaUtrGenerator` - Rules for the SaUtr: https://design.tax.service.gov.uk/hmrc-design-patterns/unique-taxpayer-reference/
- `VrnGenerator` - Rules for the VRN: https://design.tax.service.gov.uk/hmrc-design-patterns/vat-registration-number/

### How to Use the Test Generators
```scala
val atedUtr = AtedUtrGenerator().nextAtedUtr.utr
val nino = NinoGenerator().nextNino.nino
val seed = 123456
val saUtr = SaUtrGenerator(seed).nextSaUtr.utr
```

#### An Example Test
```scala
"test example" in {
  val seed: Int = 734895
  val ninoGenerator: NinoGenerator = NinoGenerator(seed)
  val saUtrGenerator: SaUtrGenerator = SaUtrGenerator(seed)
  val nino: Nino = ninoGenerator.nextNino
  val saUtr: SaUtr = saUtrGenerator.nextSaUtr

  Nino.isValid(nino.nino)                           shouldBe true
  SelfAssessmentReferenceChecker.isValid(saUtr.utr) shouldBe true
}
```

### Migrating from v12 to v13
1. Install the test library. See further info in the [Installing section](#Installing)
2. `Generator` class has been broken into individual Named Generators
```scala
val generator = new Generator()
generator.nextNino
generator.atedUtrBatch(5)
generator.nextAtedUtr
```
is now
```scala
val ninoGenerator = NinoGenerator()
ninoGenerator.nextNino
val atedUtrGenerator = AtedUtrGenerator()
atedUtrGenerator.atedUtrBatch(5)
atedUtrGenerator.nextAtedUtr
``` 

## Installing
Add the following to your SBT build:
```scala
libraryDependencies += "uk.gov.hmrc" %% "domain-play-[PLAY VERSION]" % "[LIB VERSION]"
```
or for the test library
```scala
libraryDependencies += "uk.gov.hmrc" %% "domain-test-play-[PLAY VERSION]" % "[LIB VERSION]" % Test
```
### For example
```scala
libraryDependencies += "uk.gov.hmrc" %% "domain-play-30" % "13.0.0"
```
or for the test library
```scala
libraryDependencies += "uk.gov.hmrc" %% "domain-test-play-30" % "13.0.0" % Test
```

## for Devs
### Running tests
```shell
sbt clean coverage test coverageReport
```

### Publishing artifacts locally
```shell
sbt publishLocal
```

## License ##
 
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

