package com.clubin.neyber;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by GAURAV on 30-07-2015.
 */
public class FileHandler {

    public void createFile(Context context,List<?> list,String FILENAME)
    {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
            fos.close();
            Log.i("Common", "Question writed");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  List<?> readFile(Context context,String FILENAME)
    {
        List<?> grps=null;

        try {
            FileInputStream inputStream = context.openFileInput(FILENAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            grps= (List<?>) objectInputStream.readObject();
            Log.i("Common","Question reading");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return grps;
    }
}
