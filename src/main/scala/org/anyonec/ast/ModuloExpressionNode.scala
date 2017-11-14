package org.anyonec.ast

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes._
import org.anyonec.SymbolTable


case class ModuloExpressionNode(expression: AstNode ,operand: AstNode ) extends AstNode {
  def generate(mv: MethodVisitor, symbolTable: SymbolTable) {
    expression.generate(mv, symbolTable)
    operand.generate(mv, symbolTable)
    mv.visitInsn(IREM)
  }
}