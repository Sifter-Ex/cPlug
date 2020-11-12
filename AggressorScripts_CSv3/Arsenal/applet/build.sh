rm -rf bin
rm -rf dist
mkdir bin
mkdir dist

# make our output look nice...
function print_good () {
    echo -e "\x1B[01;32m[+]\x1B[0m $1"
}

function print_error () {
    echo -e "\x1B[01;31m[-]\x1B[0m $1"
}

function print_info () {
    echo -e "\x1B[01;34m[*]\x1B[0m $1"
}

# check some dependencies
if [ $(command -v javac) ]; then
	true
else
	print_error "javac is not present. Install the JDK"
	exit 1
fi

# compile our Java 1.7 code
print_info "Compiling Java 1.7 specific applet code"
javac -source 1.7 -target 1.7 -g:none -d bin src-java-17/*.java src-java/*.java

# compile our applet
print_info "Compiling Java 1.6 specific applet code"
javac -source 1.6 -target 1.6 -g:none -d bin -cp bin src-java/*.java

# check for a cross-compiler
if [ $(command -v x86_64-w64-mingw32-gcc) ]; then
	# make sure you copied over the necessary include files... this is important!!!
	if [ -e include/win32 ]; then
		print_good "You have a x86_64 mingw--I will recompile main.dll"
		print_info "Recompiling main64.dll"

		# create a header for our JNI library
		javah -classpath bin -jni -o src/injector.h Main

		# create our 64-bit DLL
		x86_64-w64-mingw32-gcc -m64 -c src/*.c -l jni -I include -I include/win32 -Wall -D_JNI_IMPLEMENTATION_ -D_IS64_ -Wl,--kill-at -shared
		x86_64-w64-mingw32-dllwrap -m64 --def src/injector.def injector.o start_thread.o -o main_temp.dll
		strip main_temp.dll -o main64.dll

		# copy main.dll to bin
		cp main64.dll bin

		print_info "Recompiling main.dll"

		# create our 32-bit DLL
		i686-w64-mingw32-gcc -c src/*.c -l jni -I include -I include/win32 -Wall -D_JNI_IMPLEMENTATION_ -Wl,--kill-at -shared
		i686-w64-mingw32-dllwrap --def src/injector.def injector.o start_thread.o -o main_temp.dll
		strip main_temp.dll -o main.dll

		# copy main.dll to bin
		cp main.dll bin

		# cleanup
		rm -f injector.o
		rm -f start_thread.o
		rm -f main_temp.dll
	else
		print_error "Hey! you have a cross-compiler, but no Win32 JNI include files.. see README.txt for help"
		cp main.dll bin
		cp main64.dll bin
	fi
else
	print_error "No cross-compiler detected. Using precompiled DLLs"
	# this file really needs to be in bin
	cp main.dll bin
	cp main64.dll bin
fi

print_info "Building JAR files"
# create our JAR files
cd bin
jar -cvfm ../dist/applet_unsigned.jar ../manifest.txt Main.class main.dll main64.dll Base64.class Java.class
jar -cvf ../dist/applet_rhino.jar Main.class Rhino.class main.dll main64.dll Base64.class JavaApplet.class Exec.class BeanHelper.class Bean.class BeanProvider.class AppIcon*.class
cd ..

print_info "Signing the Applet"
# sign the unsigned applet
keytool -keystore keystore.bin -storepass 123456 -keypass 123456 -genkey -keyalg RSA -alias mykey -dname "CN=Trusted Publisher,OU=Unknown,O=Unknown,L=Unknown,S=Unknown,C=Unknown"
jarsigner -keystore keystore.bin -storepass 123456 -keypass 123456 -signedjar dist/applet_signed.jar dist/applet_unsigned.jar mykey

# cleanup
rm -f keystore.bin
rm -f dist/applet_unsigned.jar

print_good "I'm done. The built applet attacks are in the dist/ folder"
