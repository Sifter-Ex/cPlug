Applet (Cobalt Strike Arsenal Edition)
------
This package contains the source code for Cobalt Strike's Signed Java Applet and Smart Java
Applet attacks. 

This source code is made available for the following purposes:

1. If an anti-virus vendor is flagging the Cobalt Strike applet attacks, you have access to the
   source code and build scripts to modify this attack to meet your needs. The included applet.cna
   script lets you instruct Cobalt Strike to use your applets over the built-in ones.

2. The Signed Java Applet attack uses a self-signed certificate. You are *encouraged* to purchase
   a code-signing certificate and modify build.sh to sign the generated applet. This will make the
   signed applet attack far more effective.

Recompile
---------
This package is setup to build on Linux. You will need the following:

- Java Developer Kit 1.7: http://www.java.com

To recompile everything:

$ ./build.sh

The build.sh script will recompile main.dll if the tools and dependencies necessary to do so are
present. To recompile main.dll, you need the following:

- Minimal GNU for Windows Cross-Compiler - apt-get install mingw-w64
- Java JNI headers for Windows
	-- Install the Java Developer Kit on Windows
	-- Copy the include/* folder from the JDK on Windows to include/* in this package.
	   For example, C:\Program Files\Java\jdkXXXXXXXX\include should go to applet/include/*

           If you grabbed the right files, your include folder will look like this:

		~/applet$ find include/
		include/
		include/jvmti.h
		include/win32
		include/win32/jawt_md.h
		include/win32/jni_md.h
		include/jni.h
		include/jdwpTransport.h
		include/jawt.h
		include/classfile_constants.h

Integration
-----------
To use your new applet archives, load applet.cna into Cobalt Strike. This script contains a
Cortana filter to tell Cobalt Strike to use your applet over the built-in option. 

Go to Cobalt Strike -> Scripts, press Load.

Modifications
-------
You're encouraged to make modifications to this code and use these modifications in your
engagements. Do not redistribute this source code. It is not open source. It is provided as a 
benefit to licensed Cobalt Strike users.

License
-------
This code is subject to the end user license agreement for Cobalt Strike. The complete
license agreement is at:

https://www.cobaltstrike.com/license
