package org.anyonec

import org.scalatest._

abstract class RemoteLanguageTest extends FlatSpec with Matchers {

  val languageGenerator = new LanguageGenerator
  val byteCodeExecutor = new ByteCodeExecutor
  var className = "Hello"

  def getOutput(arnoldCode: String): String = {
    val (bytecode, root) = languageGenerator.generate("NotACeleb", arnoldCode, className)
    byteCodeExecutor.getOutput(bytecode, className)
  }

}