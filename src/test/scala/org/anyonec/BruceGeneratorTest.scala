package org.anyonec

import org.scalatest._

abstract  class BruceGeneratorTest extends FlatSpec with Matchers {

  val languageGenerator = new LanguageGenerator
  val byteCodeExecutor = new ByteCodeExecutor
  var className = "Hello"

  def getOutput(bruceCode: String): String = {
    val (bytecode, root) = languageGenerator.generate("BruceWillis", bruceCode, className)
    byteCodeExecutor.getOutput(bytecode, className)
  }

}