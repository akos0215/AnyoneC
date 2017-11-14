package org.anyonec

import java.io.{File, FileNotFoundException}
import java.util.Properties

import scala.util.Properties
import org.anyonec.ast._
import org.parboiled.errors.{ErrorUtils, ParsingException}
import org.parboiled.scala._

import scala.collection.mutable
import scala.io.Source


class AnyoneParser extends Parser {
  def keywords= List(  "ParseError",
  "DeclareInt",
  "SetInitialValue",
  "BeginMain",
  "PlusOperator",
  "MinusOperator",
  "MultiplicationOperator",
  "DivisionOperator",
  "EndMain",
  "Print",
  "Read",
  "AssignVariable",
  "SetValue",
  "EndAssignVariable",
  "False",
  "True",
  "EqualTo",
  "GreaterThan",
  "Or",
  "And",
  "If",
  "Else",
  "EndIf",
  "While",
  "EndWhile",
  "DeclareMethod",
  "MethodArguments",
  "Return",
  "EndMethodDeclaration",
  "CallMethod",
  "NonVoidMethod",
  "AssignVariableFromMethodCall",
  "Modulo"
  )
  //def reservedWords: mutable.HashMap[String, String] = mutable.HashMap()
  private val language = new Properties()

  // check repository

  val EOL = zeroOrMore("\t" | "\r" | " ") ~ "\n" ~ zeroOrMore("\t" | "\r" | " " | "\n")
  val WhiteSpace = oneOrMore(" " | "\t")

  def getReservedWord(keyWord: String): String = {

    language.getProperty(keyWord,"")
  }

  def Root: Rule1[RootNode] = rule {
    oneOrMore(AbstractMethod) ~ EOI ~~> RootNode
  }

  def AbstractMethod: Rule1[AbstractMethodNode] = rule {
    (MainMethod | Method) ~ optional(EOL)
  }

  def MainMethod: Rule1[AbstractMethodNode] = rule {
    getReservedWord("BeginMain") ~ EOL ~ zeroOrMore(Statement) ~ getReservedWord("EndMain") ~~> MainMethodNode
  }

  def Method: Rule1[AbstractMethodNode] = rule {
    getReservedWord("DeclareMethod") ~ WhiteSpace ~ VariableName ~> (s => s) ~ EOL ~
      zeroOrMore((getReservedWord("MethodArguments") ~ WhiteSpace ~ Variable ~ EOL)) ~
      (getReservedWord("NonVoidMethod") | "") ~> ((m: String) => m == getReservedWord("NonVoidMethod")) ~ optional(EOL) ~
      zeroOrMore(Statement) ~ getReservedWord("EndMethodDeclaration") ~~> MethodNode
  }

  def Statement: Rule1[StatementNode] = rule {
    DeclareIntStatement | PrintStatement |
      AssignVariableStatement | ConditionStatement |
      WhileStatement | CallMethodStatement | ReturnStatement | CallReadMethodStatement
  }

  def CallMethodStatement: Rule1[StatementNode] = rule {
    (getReservedWord("AssignVariableFromMethodCall") ~ WhiteSpace ~ VariableName ~> (v => v) ~ EOL | "" ~> (v => v)) ~
    getReservedWord("CallMethod") ~ WhiteSpace ~ VariableName ~> (v => v) ~
      zeroOrMore(WhiteSpace ~ Operand) ~ EOL ~~> CallMethodNode
  }

  def CallReadMethodStatement: Rule1[StatementNode] = rule {
    (getReservedWord("AssignVariableFromMethodCall") ~ WhiteSpace ~ VariableName ~> (v => v) ~ EOL | "" ~> (v => v)) ~
      getReservedWord("CallMethod") ~ EOL ~ getReservedWord("Read") ~ EOL ~~> CallReadMethodNode
  }

  def ConditionStatement: Rule1[ConditionNode] = rule {
    getReservedWord("If") ~ WhiteSpace ~ Operand ~ EOL ~ zeroOrMore(Statement) ~
      (getReservedWord("Else") ~ EOL ~ zeroOrMore(Statement) ~~> ConditionNode
        | zeroOrMore(Statement) ~~> ConditionNode) ~ getReservedWord("EndIf") ~ EOL

  }

  def WhileStatement: Rule1[WhileNode] = rule {
    getReservedWord("While") ~ WhiteSpace ~ Operand ~ EOL ~ zeroOrMore(Statement) ~ getReservedWord("EndWhile") ~ EOL ~~> WhileNode
  }

  def PrintStatement: Rule1[PrintNode] = rule {
    getReservedWord("Print") ~ WhiteSpace ~ (Operand ~~> PrintNode | "\"" ~ String ~ "\"" ~~> PrintNode) ~ EOL
  }

  def DeclareIntStatement: Rule1[DeclareIntNode] = rule {
    getReservedWord("DeclareInt") ~ WhiteSpace ~ VariableName ~> (s => s) ~ EOL ~ getReservedWord("SetInitialValue") ~ WhiteSpace ~ Operand ~~> DeclareIntNode ~ EOL
  }

  def AssignVariableStatement: Rule1[AssignVariableNode] = rule {
    getReservedWord("AssignVariable") ~ WhiteSpace ~ VariableName ~> (s => s) ~ EOL ~ Expression ~ getReservedWord("EndAssignVariable") ~ EOL ~~> AssignVariableNode
  }

  def ReturnStatement: Rule1[StatementNode] = rule {
    getReservedWord("Return") ~ ((WhiteSpace ~ Operand ~~> (o => ReturnNode(Some(o)))) | "" ~> (s => ReturnNode(None))) ~ EOL
  }

  def Operand: Rule1[OperandNode] = rule {
    Number | Variable | Boolean
  }

  def Expression: Rule1[AstNode] = rule {
    SetValueExpression ~
      (zeroOrMore(ArithmeticOperation | LogicalOperation))
  }

  def LogicalOperation: ReductionRule1[AstNode, AstNode] = rule {
    getReservedWord("Or") ~ WhiteSpace ~ Operand ~ EOL ~~> OrNode |
      getReservedWord("And") ~ WhiteSpace ~ Operand ~ EOL ~~> AndNode |
      getReservedWord("EqualTo") ~ WhiteSpace ~ Operand ~ EOL ~~> EqualToNode |
      getReservedWord("GreaterThan") ~ WhiteSpace ~ Operand ~ EOL ~~> GreaterThanNode

  }

  def RelationalExpression: ReductionRule1[AstNode, AstNode] = {
    EqualToExpression ~~> EqualToNode |
      GreaterThanExpression ~~> GreaterThanNode
  }


  def EqualToExpression: Rule1[OperandNode] = {
    getReservedWord("EqualTo") ~ WhiteSpace ~ Operand ~ EOL
  }

  def GreaterThanExpression: Rule1[OperandNode] = {
    getReservedWord("GreaterThan") ~ WhiteSpace ~ Operand ~ EOL
  }

  def ArithmeticOperation: ReductionRule1[AstNode, AstNode] = rule {
    PlusExpression ~~> PlusExpressionNode |
      MinusExpression ~~> MinusExpressionNode |
      MultiplicationExpression ~~> MultiplicationExpressionNode |
      DivisionExpression ~~> DivisionExpressionNode |
      ModuloExpression ~~> ModuloExpressionNode
  }

  def SetValueExpression: Rule1[OperandNode] = rule {
    getReservedWord("SetValue") ~ WhiteSpace ~ Operand ~ EOL
  }


  def PlusExpression: Rule1[AstNode] = rule {
    getReservedWord("PlusOperator") ~ WhiteSpace ~ Operand ~ EOL
  }

  def MinusExpression: Rule1[AstNode] = rule {
    getReservedWord("MinusOperator") ~ WhiteSpace ~ Operand ~ EOL
  }

  def MultiplicationExpression: Rule1[AstNode] = rule {
    getReservedWord("MultiplicationOperator") ~ WhiteSpace ~ Operand ~ EOL
  }

  def DivisionExpression: Rule1[AstNode] = rule {
    getReservedWord("DivisionOperator") ~ WhiteSpace ~ Operand ~ EOL
  }

  def ModuloExpression: Rule1[AstNode] = rule {
    getReservedWord("Modulo") ~ WhiteSpace ~ Operand ~ EOL
  }

  def Variable: Rule1[VariableNode] = rule {
    VariableName ~> VariableNode
  }

  def VariableName: Rule0 = rule {
    rule("A" - "Z" | "a" - "z") ~ zeroOrMore("A" - "Z" | "a" - "z" | "0" - "9")
  }

  def Number: Rule1[NumberNode] = rule {
    oneOrMore("0" - "9") ~> ((matched: String) => NumberNode(matched.toInt)) |
      "-" ~ oneOrMore("0" - "9") ~> ((matched: String) => NumberNode(-matched.toInt))
  }

  def Boolean: Rule1[NumberNode] = rule {
    "@" ~ getReservedWord("True") ~> (_ => NumberNode(1)) |
      "@" ~ getReservedWord("False") ~> (_ => NumberNode(0))
  }

  def String: Rule1[StringNode] = rule {
    zeroOrMore(rule {
      !anyOf("\"\\") ~ ANY
    }) ~> StringNode
  }

  def loadLanguage(languageName: String) = {
      val reservedWords = getClass.getClassLoader.getResource("org/anyonec/languages/"+languageName + ".language")
      if (reservedWords == null) {
        println("Language file not found, let's check it on github...")
        checkLanguageOnGithub(languageName)
      }
      else
        language.load(reservedWords.openStream())


  }

  def checkLanguageOnGithub(languageName: String) = {
    try
      {
     val html = Source.fromURL("https://raw.githubusercontent.com/akos0215/AnyoneC/master/src/main/scala/org/anyonec/languages/"+ languageName+".language").reader()
     language.load(html)
      }
    catch{
      case ioe: FileNotFoundException => {
        println("Language file not found")
        throw ioe
      }

    }
  }

  def parse(language: String, expression: String): RootNode = {
    loadLanguage(language)
    val parsingResult = ReportingParseRunner(Root).run(expression)
    parsingResult.result match {
      case Some(root) => root
      case None => throw new ParsingException(getReservedWord("ParseError") + ":\n" +
        ErrorUtils.printParseErrors(parsingResult))
    }
  }

}