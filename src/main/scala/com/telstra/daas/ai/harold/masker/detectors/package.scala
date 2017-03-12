package com.telstra.daas.ai.harold.masker

import scala.io.Source

/**
  * Created by markmo on 12/03/2017.
  */
package object detectors {

  val stopWords = Source.fromResource("stop-word-list.txt").getLines.toSet

  def tokenize(text: String): List[String] =
    text.split("\\s+")
      .map(_.toLowerCase)
      .filterNot(stopWords.contains)
      .toList

}
