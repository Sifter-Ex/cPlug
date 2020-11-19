Power Applet (Cobalt Strike Arsenal Edition)
------------
This package contains the source code for an alternative implementation of Cobalt Strike's Signed
and Smart Java Applet attacks. This implementation uses PowerShell to inject the desired Win32
shellcode into memory.

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
