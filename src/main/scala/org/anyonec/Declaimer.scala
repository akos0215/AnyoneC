package org.anyonec

import java.util.Locale
import javax.speech.Central
import javax.speech.synthesis.Synthesizer
import javax.speech.synthesis.SynthesizerModeDesc
import javax.speech.synthesis.Voice

import com.sun.speech.freetts.audio.AudioPlayer
import com.sun.speech.freetts.audio.SingleFileAudioPlayer
import javax.sound.sampled.AudioFileFormat.Type

import org.anyonec.ast._

object Declaimer {

  val p = new AnyoneParser

  def declaim(root: RootNode, outputFile: String, language: String): Unit = {
    p.loadLanguage(language)
    SpeechUtils.init("kevin16", outputFile)
    declaim(root)
    SpeechUtils.terminate()
  }

  def say(text: String) = SpeechUtils.doSpeak(text + "!\n")

  def declaim(node: AstNode): Unit = node match {
    case RootNode(methods) => methods.map(m => declaim(m))
    case MainMethodNode(stmts) =>
      say(p.getReservedWord("BeginMain"))
      stmts foreach declaim
      say(p.getReservedWord("EndMain"))
    case MethodNode(name, args, ret, stmts) =>
      say(s"${p.getReservedWord("DeclareMethod")} $name")
      args.foreach(a => say(s"${p.getReservedWord("MethodArguments")} ${a.variableName}"))
      if (ret) say(p.getReservedWord("NonVoidMethod"));
      stmts.map(s => declaim(s))
      say(p.getReservedWord("EndMethodDeclaration"))

    case AssignVariableNode(name, expr) =>
      say(s"${p.getReservedWord("AssignVariable")} $name")
      say(p.getReservedWord("SetValue"))
      declaim(expr)
      say(p.getReservedWord("EndAssignVariable"))
    case PrintNode(what) =>
      say(p.getReservedWord("Print"))
      declaim(what)
    case DeclareIntNode(name, value) =>
      say(s"${p.getReservedWord("DeclareInt")} $name")
      say(s"${p.getReservedWord("SetInitialValue")} $value")
    case ConditionNode(condition, ifStmts, elseStmts) =>
      say(p.getReservedWord("If"))
      declaim(condition)
      ifStmts foreach declaim
      if (elseStmts.nonEmpty) {
        say(p.getReservedWord("Else"))
        elseStmts foreach declaim
      }
      say(p.getReservedWord("EndIf"))
    case WhileNode(condition, stmts) =>
      say(p.getReservedWord("While"))
      declaim(condition)
      stmts foreach declaim
      say(p.getReservedWord("EndWhile"))
    case CallMethodNode(variable, name, args) =>
      if (variable.nonEmpty) {
        say(s"${p.getReservedWord("AssignVariableFromMethodCall")} $variable")
      }
      say(s"${p.getReservedWord("CallMethod")} $name")
      args foreach declaim
    case CallReadMethodNode(variable) =>
      say(s"${p.getReservedWord("Read")} $variable")
    case ReturnNode(expr) =>
      say(p.getReservedWord("Return"))
      expr.map { x => declaim(x) }

    case AndNode(expr1, expr2) =>
      declaim(expr1)
      say(p.getReservedWord("And"))
      declaim(expr2)
    case OrNode(expr1, expr2) =>
      declaim(expr1)
      say(p.getReservedWord("Or"))
      declaim(expr2)
    case PlusExpressionNode(expr1, expr2) =>
      declaim(expr1)
      say(p.getReservedWord("PlusOperator"))
      declaim(expr2)
    case MinusExpressionNode(expr1, expr2) =>
      declaim(expr1)
      say(p.getReservedWord("MinusOperator"))
      declaim(expr2)
    case DivisionExpressionNode(expr1, expr2) =>
      declaim(expr1)
      say(p.getReservedWord("DivisionOperator"))
      declaim(expr2)
    case MultiplicationExpressionNode(expr1, expr2) =>
      declaim(expr1)
      say(p.getReservedWord("MultiplicationOperator"))
      declaim(expr2)
    case ModuloExpressionNode(expr1, expr2) =>
      declaim(expr1)
      say(p.getReservedWord("Modulo"))
      declaim(expr2)
    case GreaterThanNode(expr1, expr2) =>
      declaim(expr1)
      say(p.getReservedWord("GreaterThan"))
      declaim(expr2)
    case EqualToNode(expr1, expr2) =>
      declaim(expr1)
      say(p.getReservedWord("EqualTo"))
      declaim(expr2)

    case NumberNode(num)    => say(num.toString)
    case StringNode(str)    => say(str)
    case VariableNode(name) => say(name)

    case other              => say(s"${p.getReservedWord("ParseError")} $other")
  }

}

object SpeechUtils {
  System.setProperty("freetts.voices",
    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory")

  val desc = new SynthesizerModeDesc(Locale.US)

  Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral")

  val synthesizer = Central.createSynthesizer(desc)
  synthesizer.allocate()
  synthesizer.resume()

  val smd = synthesizer.getEngineModeDesc().asInstanceOf[SynthesizerModeDesc]
  val voices = smd.getVoices()

  var voice: Voice = null

  def init(voiceName: String, outputFile: String) {
    for (v <- voices) {
      if (v.getName().equals(voiceName)) {
        voice = v
        val audioPlayer = new SingleFileAudioPlayer(outputFile, Type.WAVE);
        voice.asInstanceOf[com.sun.speech.freetts.jsapi.FreeTTSVoice].getVoice.setAudioPlayer(audioPlayer)
        synthesizer.getSynthesizerProperties().setVoice(voice);
        return
      }
    }
  }

  def terminate() = {
    synthesizer.deallocate()
  }

  def doSpeak(speakText: String) {
    //println(speakText)
    synthesizer.speakPlainText(speakText, null);
    synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
  }
}  