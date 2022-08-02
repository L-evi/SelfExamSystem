package com.rewrite.selfexamsystem.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description :  文件上传工具类
 * @since 2022/7/25 20:34
 */

public class FileUtil {
    /**
     * 存储的相对路径
     */
    private static final String saveFilePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files" + File.separator;

    /**
     * @param file:需要存储的文件
     * @param username:存到username的用户文件夹中
     * @return Map：返回是否接收文件成功的提醒
     * @description : 对于传进来的文件，使用用户名作为其文件夹进行存储到本地中
     * @author Levi
     * @since 2022/7/27 10:53
     */
    public static Map<String, Object> singleFileUpload(MultipartFile file, String username) {
        Map<String, Object> res = new HashMap<>();
//        如果文件为空，则返回错误
        if (file.isEmpty()) {
            res.put("msg", "fail");
            res.put("des", "文件为空");
            return res;
        }
//        获取文件后缀名
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
//        使用username作为上级文件夹
        String filename = System.currentTimeMillis() + "." + fileExtension;
        File getFile = new File(saveFilePath + username + File.separator + filename);
//        判断文件夹是否存在，不存在则创建
        if (!getFile.getParentFile().exists()) {
            getFile.mkdirs();
        }
//       将文件存储下来
        try {
            file.transferTo(getFile);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("msg", "fail");
            res.put("des", "文件上传失败");
            return res;
        }
//        获取文件路径
        String filePath = getFile.getPath();
//        返回结果
        res.put("msg", "success");
        res.put("des", "文件上传成功");
        res.put("filepath", filePath);
        return res;
    }

    /**
     * @param files:    多个文件
     * @param username: 用户名
     * @return 返回存储文件的结果
     * @description : 将传入的文件通过循环全部存储到username的文件夹下
     * @author Levi
     * @since 2022/7/27 16:33
     */
    public static Map<String, Object> MultipleFilesUpload(MultipartFile[] files, String username) {
        Map<String, Object> res = new HashMap<>();
        String filespath = "";
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                res.put("msg", "fail");
                res.put("des", "存在文件为空，已删除所有的文件，请重新上传");
//                删除文件
                deleteFile(filespath);
                return res;
            }
//            获取文件后缀名
            String fileExtention = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
//            使用username作为上级文件夹
            String filename = System.currentTimeMillis() + "." + fileExtention;
            File getFile = new File(saveFilePath + username + File.separator + filename);
//            判断文件夹是否存在
            if (!getFile.getParentFile().exists()) {
                getFile.getParentFile().mkdirs();
            }
            try {
                file.transferTo(getFile);
//                存储相对路径
                filespath += (saveFilePath + username + File.separator + filename) + "|";
            } catch (Exception e) {
                e.printStackTrace();
                res.put("msg", "fail");
                res.put("des", "文件上传失败，已删除所有的文件，请重新上传");
//                删除文件
                deleteFile(filespath);
                return res;
            }
        }
//        上传成功的提示信息
        res.put("msg", "success");
        res.put("des", "文件上传成功");
        res.put("filepath", filespath);
        System.out.println(filespath);
        return res;
    }

    /**
     * @param filespath: 文件路径
     * @return 返回删除是否成功，true：成功，false：失败
     * @description : 通过传入的文件路径（根据“|”分割），删除对应路径的文件，并且返回结果
     * @author Levi
     * @since 2022/7/27 16:11
     */
    public static Boolean deleteFile(String filespath) {
        String filepathArr[] = filespath.split("\\|");
        for (String filepath : filepathArr) {
            File file = new File(filepath);
            if (file.exists()) {
                if (!file.delete()) {
                    return false;
                }
            }
        }
        return true;
    }
}
