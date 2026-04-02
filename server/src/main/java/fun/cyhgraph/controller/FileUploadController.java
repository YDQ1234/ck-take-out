package fun.cyhgraph.controller;

import fun.cyhgraph.result.Result;
import fun.cyhgraph.utils.OSSUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/admin/file")
public class FileUploadController {

    @Autowired
    private OSSUploadUtil ossUploadUtil;

    /**
     * 上传图片接口
     * @param file 上传的文件
     * @param folder 文件夹名称（employee, dish, setmeal, user 等）
     * @return 返回上传成功后的图片 URL
     */
    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam MultipartFile file,
                                            @RequestParam(defaultValue = "images") String folder) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件为空");
            }

            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error("只支持上传图片文件");
            }

            // 验证文件大小（最大 5MB）
            long maxSize = 5 * 1024 * 1024;
            if (file.getSize() > maxSize) {
                return Result.error("文件大小不能超过 5MB");
            }

            String imageUrl = ossUploadUtil.uploadImage(file, folder);
            return Result.success(imageUrl);

        } catch (Exception e) {
            log.error("文件上传失败：", e);
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
}