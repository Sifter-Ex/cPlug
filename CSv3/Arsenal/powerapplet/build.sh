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

print_info "Building JAR files"
# create our JAR files
cd bin
jar -cvfm ../dist/applet_unsigned.jar ../manifest.txt Main*.class Base64.class Java.class
jar -cvf ../dist/applet_rhino.jar Main*.class Rhino.class Base64.class JavaApplet.class Exec.class BeanHelper.class Bean.class BeanProvider.class AppIcon*.class
cd ..

print_info "Signing the Applet"
# sign the unsigned applet
keytool -keystore keystore.bin -storepass 123456 -keypass 123456 -genkey -keyalg RSA -alias mykey -dname "CN=Trusted Publisher,OU=Unknown,O=Unknown,L=Unknown,S=Unknown,C=Unknown"
jarsigner -keystore keystore.bin -storepass 123456 -keypass 123456 -signedjar dist/applet_signed.jar dist/applet_unsigned.jar mykey

# cleanup
rm -f keystore.bin
rm -f dist/applet_unsigned.jar

print_good "I'm done. The built applet attacks are in the dist/ folder"
