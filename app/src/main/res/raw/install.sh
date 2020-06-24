cd $1/arduino;

chmod 777 ./busybox;
ls -l;

./busybox gzip -d ./avr.tar.gz;
./busybox tar -xvf ./avr.tar;
rm ./avr.tar;
mv local avr

./busybox gzip -d ./libraries.tar.gz;
./busybox tar -xvf ./libraries.tar;
rm ./libraries.tar;

rm ./busybox;