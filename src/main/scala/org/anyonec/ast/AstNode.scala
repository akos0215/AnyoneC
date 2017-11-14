package org.anyonec.ast

import org.objectweb.asm.{MethodVisitor, ClassWriter}
import org.objectweb.asm.Opcodes._
import org.objectweb.asm.commons.{GeneratorAdapter, Method}
import org.anyonec.SymbolTable

abstract class AstNode {
  def generate(mv: MethodVisitor, symbolTable: SymbolTable)
}
