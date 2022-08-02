package com.rewrite.selfexamsystem.controller.TestController;

import com.rewrite.selfexamsystem.domain.Announcement;
import com.rewrite.selfexamsystem.utils.FileUtil;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description :  测试接收文件
 * @since 2022/7/25 15:49
 */

@RestController
public class TestUploadFiles {
    /**
     * @param files:  前端传回来的多个文件，需要用files命名
     * @param getMap: 从Header中获取token然后能获取username（暂时没用）
     * @return 用响应体包装返回结果
     * @description : 接收前端传回来的文件以及存起来
     * @author Levi
     * @since 2022/7/27 16:37
     */
    @RequestMapping(value = "/test/getFiles", method = RequestMethod.POST)
    public ResponseResult testGetMultipartFile(@RequestParam(value = "files") MultipartFile[] files, @RequestHeader Map<String, Object> getMap, Announcement announcement) {
//        设定文件路径名：使用相对路径，存放用户文件夹下的files文件夹中
        String saveFilePath = System.getProperty("user.dir") + File.separator + "files" + File.separator;
        System.out.println(FileUtil.MultipleFilesUpload(files, "2"));
        System.out.println(announcement);
//        多个文件用循环
/*        for (MultipartFile file : files) {
            Map<String, Object> map = FileUtil.singleFileUpload(file, "2");
            if (map.get("msg").equals("fail")) {
                return new ResponseResult(ResultCode.SERVER_ERROR, map.get("des"));
            } else {
                continue;
            }
//            如果某一个文件为空，则返回文件为空的错误
            if (file.isEmpty()) {
                return new ResponseResult(ResultCode.SUCCESS, "文件为空");
            }
//            上传的时候的文件名，filename是获取文件的后缀名，并且前面加上上传时候的系统时间，避免同名文件相互覆盖
            String originalFilename = file.getOriginalFilename();
            String filename = System.currentTimeMillis() + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            File newFile = new File(saveFilePath + filename);
//            检测文件夹是否存在，不存在则创建
            if (!newFile.getParentFile().exists()) {
                newFile.getParentFile().mkdirs();
            }
//            将文件存储下来
            try {
                file.transferTo(newFile);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult(ResultCode.SUCCESS, "文件上传失败");
            }
//            打印文件存放路径
            Path saveFile = Paths.get(saveFilePath + File.separator + filename);
            System.out.println(saveFile);
            System.out.println(newFile.getPath());
        }*/
        return new ResponseResult(ResultCode.SUCCESS, "文件上传成功");
    }
}
