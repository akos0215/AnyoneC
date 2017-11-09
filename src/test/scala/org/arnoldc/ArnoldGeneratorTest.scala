package org.arnoldc

import org.scalatest._

abstract  class ArnoldGeneratorTest extends FlatSpec with Matchers {

  val arnoldGenerator = new LanguageGenerator
  val byteCodeExecutor = new ByteCodeExecutor
  var className = "Hello"

  def getOutput(arnoldCode: String): String = {
    val (bytecode, root) = arnoldGenerator.generate("Arnold", arnoldCode, className)
    byteCodeExecutor.getOutput(bytecode, className)
  }

}