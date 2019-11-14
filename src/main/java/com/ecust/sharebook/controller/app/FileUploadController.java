package com.ecust.sharebook.controller.app;



import com.ecust.sharebook.utils.common.upImage.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 未完成
 */

@Controller
@RequestMapping("/small")

@ConfigurationProperties(prefix = "web")

public class FileUploadController {


    private final ResourceLoader resourceLoader;

    @Autowired
    public FileUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    private String path;





   /**
     * @param file 上传的文件
     * @return
     */
    @ResponseBody
    @RequestMapping("/fileUpload")
    public String upload(@RequestParam("file")MultipartFile file ){
        //1定义要上传文件 的存放路径
        String localPath="/users/zl/Desktop/temp";
        //2获得文件名字
        String fileName=file.getOriginalFilename();
        //2上传失败提示
        String warning="";
        if(FileUtils.upload(file, localPath, fileName)){
            //上传成功
            warning="上传成功";

        }else{
            warning="上传失败";
        }
        System.out.println(warning);
        return "上传成功";
    }





    /**
     * 显示图片
     * @param fileName 文件名
     * @return
     */

    @RequestMapping("show")
    public ResponseEntity  show(String fileName){


        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + path + fileName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }
}


