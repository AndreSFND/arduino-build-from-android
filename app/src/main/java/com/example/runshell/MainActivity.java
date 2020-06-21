package com.example.runshell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String diretorio, resultado;
        InputStream arquivo;

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setMovementMethod(new ScrollingMovementMethod());

        String path;
        path = getFilesDir() + File.separator + "RunShell";

        // Cria os diretorios avr e packages
        RunCommand("mkdir "+ path);
        RunCommand("mkdir "+ path + File.separator + "arduino");

        /**
         * Arquivos que sao extraidos
         *
         *      install.sh - Extrai os arquivos principais
         *      run.sh - Compila o arquivo .ino em .hex
         *
         *      arduino/busybox - Disponibiliza funcoes de linux para o android
         *      arduino/avr.tar.gz - Pacote AVR
         *      arduino/libraries.tar.gz - Pacotes Arduino
         **/
        if(!directoryExists(path + File.separator + "arduino" + File.separator + "avr")) {

            // arduino/arduino.mk
            if (!fileExists(path + File.separator + "arduino"  + File.separator + "arduino.mk")) {

                diretorio = path + File.separator + "arduino" + File.separator + "arduino.mk";
                arquivo = getResources().openRawResource(R.raw.arduino);
                copiaArquivo(diretorio, arquivo);

            }

            // arduino/busybox
            if (!fileExists(path + File.separator + "arduino"  + File.separator + "busybox")) {

                diretorio = path + File.separator + "arduino" + File.separator + "busybox";
                arquivo = getResources().openRawResource(R.raw.busybox);
                copiaArquivo(diretorio, arquivo);

            }


            // arduino/avr.tar.gz
            if (!fileExists(path + File.separator + "arduino" + File.separator + "avr.tar.gz")) {

                diretorio = path + File.separator + "arduino" + File.separator + "avr.tar.gz";
                arquivo = getResources().openRawResource(R.raw.avr);
                copiaArquivo(diretorio, arquivo);

            }

            // arduino/libraries.tar.gz
            if (!fileExists(path + File.separator + "arduino" + File.separator + "libraries.tar.gz")) {

                diretorio = path + File.separator + "arduino" + File.separator + "libraries.tar.gz";
                arquivo = getResources().openRawResource(R.raw.libraries);
                copiaArquivo(diretorio, arquivo);

            }

            // install.sh
            diretorio = path + File.separator + "install.sh";
            arquivo = getResources().openRawResource(R.raw.install);
            copiaArquivo(diretorio, arquivo);

            //Torna o install.sh executavel e o executa
            RunCommand("chmod 775 " + diretorio);
            resultado = RunCommand("sh " + diretorio + " " + path);

            tv.setText(resultado);

        }

        // Se ja extraiu os arquivos
        if(directoryExists(path + File.separator + "arduino" + File.separator + "avr")){

            // run.sh
            diretorio = path + File.separator + "run.sh";
            arquivo = getResources().openRawResource(R.raw.run);
            copiaArquivo(diretorio, arquivo);

            //Torna o run.sh executavel e o executa
            RunCommand("chmod 775 " + diretorio);
            resultado = RunCommand("sh " + diretorio + " " + path + " " + Environment.getExternalStorageDirectory());

            tv.setText(resultado);

        }

    }

    void copiaArquivo(String pathToScript, InputStream in) {

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(pathToScript);
            byte[] buff = new byte[1024];
            int read = 0;
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        }
        catch(Exception e){
        }
        finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    boolean fileExists(String arquivo) {

        File f = new File(arquivo);

        return f.exists();

    }

    boolean directoryExists(String diretorio) {

        File f = new File(diretorio);
        return f.isDirectory();

    }

    String RunCommand(String cmd) {

        StringBuffer cmdOut = new StringBuffer();

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);

            InputStreamReader r = new InputStreamReader(process.getInputStream());

            BufferedReader bufReader = new BufferedReader(r);
            char buf[] = new char[4096];

            int nRead = 0;
            while((nRead = bufReader.read(buf)) > 0) {

                cmdOut.append(buf, 0, nRead);

            }
            bufReader.close();

            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return cmdOut.toString();

    }

}
