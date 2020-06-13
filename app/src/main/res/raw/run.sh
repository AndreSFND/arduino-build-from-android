export PATH=$1/arduino/avr/bin:$PATH;
cd $1/arduino;

mkdir Blink;

mv arduino.mk Blink/Makefile;
cd Blink;

touch Blink.ino;
echo "void setup() {	pinMode(13, OUTPUT); } void loop() { digitalWrite(13, LOW); delay(1000); digitalWrite(13, HIGH); delay(400); }" > Blink.ino;

export BOARD=uno;
make;
make upload;

mkdir $2/arduinoOutput
cp Blink.hex $2/arduinoOutput/Blink.hex