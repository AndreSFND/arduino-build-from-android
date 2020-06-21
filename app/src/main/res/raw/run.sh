export PATH=$1/arduino/avr/bin:$PATH;
cd $1/arduino;

# $1 = cache/RunShell
# $2 = externalStorage

mkdir Blink;

mv arduino.mk Blink/Makefile;
cd Blink;

sed -i "1i SHELL:=/system/bin/sh" Makefile
sed -i "1i SERIALDEV:=/dev/ttyUSB0" Makefile
sed -i "1i ARDUINODIR:=$1/arduino" Makefile
sed -i "1i AVRTOOLSPATH:=$1/arduino/avr/bin" Makefile
sed -i "1i TARGET:=$1/arduino/Blink/Blink.ino" Makefile

touch Blink.ino;
echo "void setup() {	pinMode(13, OUTPUT); } void loop() { digitalWrite(13, LOW); delay(1000); digitalWrite(13, HIGH); delay(400); }" > Blink.ino;

export BOARD=uno;
make;
#make upload;

mkdir $2/arduinoOutput
cp Blink.hex $2/arduinoOutput/Blink.hex