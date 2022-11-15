package org.simple.utils;

import cn.hutool.core.io.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ComUtil {

    public static File MultipartToFile(MultipartFile file){
        try {
            File f = null;
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            f = new File(originalFilename);
            FileUtil.writeFromStream(inputStream,f);
            return f;
        } catch (IOException e) {
            return null;
        }
    }

    public static List<File> MultipartToFiles(MultipartFile[] files){
        List<File> list = new ArrayList<>();
        for(int i =0 ;i < files.length ; i ++){
            MultipartFile file = files[i];
            try {
                File f = null;
                InputStream inputStream = file.getInputStream();
                String originalFilename = file.getOriginalFilename();
                f = new File(originalFilename);
                FileUtil.writeFromStream(inputStream,f);
                list.add(f);
            } catch (IOException e) {
                list.add(null);
            }
        }
        return list;
    }


    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024*4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
