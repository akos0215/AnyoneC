# AnyoneC

It is a Programming language generator based on ArnoldC.
## Motivation

I really love ArnoldC, the programming language which is based on Arnold Schwarzenegger's one-liners. 
Nevertheless I realized that it can be generalized to utilize anyone's one-liners,
e.g. poets, writers, actors, politics etc. And do it without recompiling the code.
 

## HelloWorld.arnoldc

	IT'S SHOWTIME
	TALK TO THE HAND "hello world"
	YOU HAVE BEEN TERMINATED

## HelloWorld.brucewillisc

    WELCOME TO THE PARTY, PAL!
    YIPPEE KI-YAY MOTHERFUCKER "hello world"
    NOW I KNOW HOW A TV DINNER FEELS.

## Quick Start

	wget https://akos0215.github.io/AnyoneC.jar
	echo -e "IT'S SHOWTIME\nTALK TO THE HAND \"hello world\"\nYOU HAVE BEEN TERMINATED" > hello.arnoldc
	java -jar AnyoneC.jar -language ArnoldC -run hello.arnoldc
	
To create some "audible" output you can try the -declaim option:

	java -jar AnyoneC.jar -language Arnold -declaim hello.arnoldc

## Brief overview of the keywords of ArnoldC dialect

Check the [wiki](http://github.com/lhartikk/ArnoldC/wiki/ArnoldC) for more details

[False](http://www.youtube.com/watch?v=_wk-jT9rn-8) `I LIED`

[True](http://www.youtube.com/watch?v=CtNb1dnEaSQ) `NO PROBLEMO`

[If](http://www.youtube.com/watch?v=MiB7GLyvvJQ) `BECAUSE I'M GOING TO SAY PLEASE`

[Else](http://www.youtube.com/watch?v=c4psKYpfnYs) `BULLSHIT`

[EndIf](http://youtu.be/uGstM8QMCjQ?t=1m23s) `YOU HAVE NO RESPECT FOR LOGIC`

[While](http://www.youtube.com/watch?v=wDztrw_0N8M) `STICK AROUND`

[EndWhile](http://www.youtube.com/watch?v=R39e30FL37U) `CHILL`

PlusOperator `GET UP`

[MinusOperator](http://www.youtube.com/watch?v=7Ox0Ehq-FRQ) `GET DOWN`

[MultiplicationOperator](http://www.youtube.com/watch?v=lf3Kyv_iaNs) `YOU'RE FIRED`

[DivisionOperator](http://www.youtube.com/watch?v=9VHtuqXZQeo) `HE HAD TO SPLIT`

[ModuloOperator](http://www.youtube.com/watch?v=ybJWKZB0Erk&feature=youtu.be&t=6m59s)  `I LET HIM GO`

[EqualTo](http://www.youtube.com/watch?v=A1-wUV0-_JY) `YOU ARE NOT YOU YOU ARE ME`

[GreaterThan](http://www.youtube.com/watch?v=19R2fDXCzcM) `LET OFF SOME STEAM BENNET`

[Or](http://www.youtube.com/watch?v=RYtQMhnBtTw) `CONSIDER THAT A DIVORCE`

[And](http://www.youtube.com/watch?v=ZQ_Q2b_aXjk) `KNOCK KNOCK`

[DeclareMethod](http://www.youtube.com/watch?v=uCwrOpnyXeo) `LISTEN TO ME VERY CAREFULLY`

[NonVoidMethod](http://www.youtube.com/watch?v=WANa9Oku-JM) `GIVE THESE PEOPLE AIR`

[MethodArguments](http://www.youtube.com/watch?v=FWmH9ylqYYQ) `I NEED YOUR CLOTHES YOUR BOOTS AND YOUR MOTORCYCLE`

[Return](http://www.youtube.com/watch?v=-YEG9DgRHhA) `I'LL BE BACK`

[EndMethodDeclaration](http://www.youtube.com/watch?v=Hhm7aWp8gvc) `HASTA LA VISTA, BABY`

[CallMethod](http://www.youtube.com/watch?v=HGhP3p6lI3U) `DO IT NOW`

[AssignVariableFromMethodCall](http://www.youtube.com/watch?v=HkkibBYm2WI) `GET YOUR ASS TO MARS`

[DeclareInt](http://www.youtube.com/watch?v=PZwwqjcEDUQ) `HEY CHRISTMAS TREE`

[SetInitialValue](http://www.youtube.com/watch?v=lwqzA6F7nws) `YOU SET US UP`

[BeginMain](http://www.youtube.com/watch?v=TKTL2EDTFSo) `IT'S SHOWTIME`

[EndMain](http://www.youtube.com/watch?v=iy_BBBGBpqA) `YOU HAVE BEEN TERMINATED`

[Print](http://www.youtube.com/watch?v=dQ6m8ztEzfA) `TALK TO THE HAND`

[ReadInteger](https://www.youtube.com/watch?v=1mC9eOqsyTg) `I WANT TO ASK YOU A BUNCH OF QUESTIONS AND I WANT TO HAVE THEM ANSWERED IMMEDIATELY`

[AssignVariable](http://www.youtube.com/watch?v=-9-Te-DPbSE) `GET TO THE CHOPPER`

[SetValue](http://www.youtube.com/watch?v=RrPXRkJ_P90) `HERE IS MY INVITATION`

[EndAssignVariable](http://www.youtube.com/watch?v=rk9WHasIZk0) `ENOUGH TALK`

[ParseError](http://www.youtube.com/watch?v=oGcRTJK43OM) `WHAT THE FUCK DID I DO WRONG`

## Add other languages as you wish

If you miss a celebrity as a programming language, you just need to fill in the language file and
put it into the jar, or if you would share it, please feel free to send it to me via mail, or just have a pull request on github.
