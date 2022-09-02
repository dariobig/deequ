/** Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
  *
  * Licensed under the Apache License, Version 2.0 (the "License"). You may not
  * use this file except in compliance with the License. A copy of the License
  * is located at
  *
  * http://aws.amazon.com/apache2.0/
  *
  * or in the "license" file accompanying this file. This file is distributed on
  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  * express or implied. See the License for the specific language governing
  * permissions and limitations under the License.
  */

package com.amazon.deequ.examples

import com.amazon.deequ.examples.ExampleUtils.withSpark
import com.amazon.deequ.suggestions.{ConstraintSuggestionRunner, Rules}

import org.apache.spark.sql.SparkSession
import org.apache.spark
import com.amazon.deequ.suggestions.rules.NumbersRule

case class RowData(
    productName: String,
    totalNumber: Double,
    score: Double,
    status: String,
    valuable: Option[Boolean]
)

private[examples] object ConstraintSuggestionExample extends App {

  withSpark { session =>
    import session.sqlContext.implicits._

    // Lets first generate some example data
    val data = session.sparkContext
      .parallelize(
        Seq(
          RowData("thingA", 13.1, -1.5, "IN_TRANSIT", Some(true)),
          RowData("thingA", 5, -7, "DELAYED", Some(false)),
          RowData("thingB", Double.NaN, 1.0, "DELAYED", None),
          RowData("thingC", Double.NaN, 2.0, "IN_TRANSIT", Some(false)),
          RowData("thingD", 1.0, 1.0, "DELAYED", Some(true)),
          RowData("thingC", 7.0, 7.0, "UNKNOWN", None),
          RowData("thingC", 24, 12, "UNKNOWN", None),
          RowData("thingE", 20, 13, "DELAYED", Some(false)),
          RowData("thingA", 13.0, 14, "IN_TRANSIT", Some(true)),
          RowData("thingA", 5, 5.1, "DELAYED", Some(false)),
          RowData("thingB", Double.NaN, 3.1, "DELAYED", None),
          RowData("thingC", Double.NaN, 2.0, "IN_TRANSIT", Some(false)),
          RowData("thingD", 1.0, 1.0, "DELAYED", Some(true)),
          RowData("thingC", 17.0, 22.12, "UNKNOWN", None),
          RowData("thingC", 22, 12.32, "UNKNOWN", None),
          RowData("thingE", 23, 11.0, "DELAYED", Some(false))
        )
      )
      .toDF()

    println(data.columns)
    println(data.count())
    println(data.show())

    // We ask deequ to compute constraint suggestions for us on the data
    // It will profile the data and than apply a set of rules specified in addConstraintRules()
    // to suggest constraints
    val suggestionResult = ConstraintSuggestionRunner()
      .onData(data)
      .addConstraintRules(Rules.DEFAULT)
      .addConstraintRule(NumbersRule())
      .run()

    // We can now investigate the constraints that deequ suggested. We get a textual description
    // and the corresponding scala code for each suggested constraint
    //
    // Note that the constraint suggestion is based on heuristic rules and assumes that the data it
    // is shown is 'static' and correct, which might often not be the case in the real world.
    // Therefore the suggestions should always be manually reviewed before being applied in real
    // deployments.
    suggestionResult.constraintSuggestions.foreach {
      case (column, suggestions) =>
        suggestions.foreach { suggestion =>
          println(
            s"Constraint suggestion for '$column':\t${suggestion.description}\n" +
              s"The corresponding scala code is ${suggestion.codeForConstraint}\n"
          )
        }
    }

  }
}
