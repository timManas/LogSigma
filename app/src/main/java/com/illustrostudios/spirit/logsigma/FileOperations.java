package com.illustrostudios.spirit.logsigma;

import android.app.Activity;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FileOperations extends Activity
{
    public FileOperations()
    {

    }

    public boolean write(String fileName, String content) throws IOException
    {
        try
        {
            File fpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File file = new File(fpath, fileName);
            System.out.println(file.toString());

            if (!file.exists())
            {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(content);
            bw.newLine();
            bw.close();
            fw.close();

            System.out.println("Success in writing file in memory");
            return true;

        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("FAILED iv n writing file in memory");
            return false;
        }

    }


} // End of class


