package com.tutorial.kafka.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public static byte[] file2byte(File file){
        byte[] buffer = null;
        try{
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }

    public static File byte2File(byte[] buf, String filePath, String fileName){
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try{
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()){
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            if (bos != null){
                try{
                    bos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (fos != null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static File multipartFile2File(MultipartFile multipartFile){
        File file = null;
        if (multipartFile != null){
            try {
                String[] fileName = multipartFile.getOriginalFilename().split("\\.");
                file=File.createTempFile(fileName[0], "." + fileName[1]);
                multipartFile.transferTo(file);
                file.deleteOnExit();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return file;
    }
}
