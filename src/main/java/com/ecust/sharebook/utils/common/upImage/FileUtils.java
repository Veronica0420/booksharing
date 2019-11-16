package com.ecust.sharebook.utils.common.upImage;


import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import java.util.logging.Logger;
/**
 *
 * @author
 *
 */
public class FileUtils {

    /**
     *
     * @param file 文件
     * @param path   文件存放路径
     * @param fileName 原文件名
     * @return
     */

    private static String name = FileUtils.class.getName();
    private static Logger log = Logger.getLogger(name);
    // 生成新的文件名
    public static String picURL = "http://101.37.173.235/magic/BookCircle/";
    public static boolean upload(MultipartFile file, String path, String fileName){

         log.info("oldFileNam:"+fileName);
         //使用新文件名
        String newName=FileNameUtils.getFileName(fileName);
        String realPath = path + "/" +newName;
        log.info("newName:"+newName);
        picURL=picURL.concat(newName);
        log.info("picURL:"+picURL);

        //使用原文件名
        // String realPath = path + "/" + fileName;

        File dest = new File(realPath);

        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);

            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

}


