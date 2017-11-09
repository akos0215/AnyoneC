package org.arnoldc

import org.arnoldc.ast.RootNode


class LanguageGenerator extends ClassLoader {

  def generate(language: String, sourceCode: String, filename: String): (Array[Byte], RootNode) = {
    val parser =  Class.forName("org.arnoldc.languages."+language+"Parser").newInstance.asInstanceOf[{ def parse(expression: String): RootNode  }]
    //val parser = new ArnoldParser
    val rootNode = parser.parse(sourceCode)
    (rootNode.generateByteCode(filename), rootNode)
  }
}
