package org.anyonec

import org.scalatest._

abstract  class ArnoldGeneratorTest extends FlatSpec with Matchers {

  val languageGenerator = new LanguageGenerator
  val byteCodeExecutor = new ByteCodeExecutor
  var className = "Hello"

  def getOutput(arnoldCode: String): String = {
    val (bytecode, root) = languageGenerator.generate("ArnoldC", arnoldCode, className)
    byteCodeExecutor.getOutput(bytecode, className)
  }

}