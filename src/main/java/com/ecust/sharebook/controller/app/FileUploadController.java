package com.ecust.sharebook.controller.app;



import com.ecust.sharebook.pojo.BookCircleInf;
import com.ecust.sharebook.service.TBookCircleService;

import com.ecust.sharebook.utils.common.upImage.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private  TBookCircleService tBookCircleService;





   /**
     * @param file 上传的文件
     * @return
     */
    @ResponseBody
    @RequestMapping("/fileUpload")
    public String upload(@RequestParam Map<String, Object> params, @RequestParam("file")MultipartFile file ){
        //1定义要上传文件 的存放路径
        String localPath="/opt/lampp/htdocs/magic/BookCircle/";   //CenOS7 服务器上路径
        //String localPath="D:/img/";   //win10本地测试
        //2获得文件名字
        String fileName=file.getOriginalFilename();
        //2上传失败提示
        String warning="";
        FileUtils fileUtils=new FileUtils();
        if(fileUtils.upload(file, localPath, fileName)){
            //上传成功,图片url更新数据库图书圈表

            String picULR=fileUtils.picURL;
            //String bookCircleId = (String) request.getParameter("bookCircleId");
            String bookCircleId=params.get("bookCircleId").toString();
            System.out.println("here  picURL="+picULR);
            System.out.println("here bookcircleid:"+bookCircleId);

            BookCircleInf bookCircleInf=new BookCircleInf();
            bookCircleInf.setCirclePicPath(picULR);
            bookCircleInf.setBookCircleId(Integer.parseInt(bookCircleId));
            tBookCircleService.updatePicPath(bookCircleInf);
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


