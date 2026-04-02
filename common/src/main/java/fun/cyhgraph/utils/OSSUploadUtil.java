package fun.cyhgraph.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
public class OSSUploadUtil {

    @Autowired
    private OSS ossClient;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.region}")
    private String region;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    /**
     * 上传图片到 OSS
     * @param file 上传的文件
     * @param folder 文件夹名称（如：employee, dish, setmeal）
     * @return 图片的 OSS 访问 URL
     */
    public String uploadImage(MultipartFile file, String folder) {
        try {
            // 获取文件原始名称
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new RuntimeException("文件名为空");
            }

            // 获取文件扩展名
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 生成唯一的文件名：folder/UUID.ext
            String objectName = folder + "/" + UUID.randomUUID().toString() + fileExtension;

            // 获取文件输入流
            InputStream inputStream = file.getInputStream();

            // 上传文件到 OSS
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            ossClient.putObject(putObjectRequest);

            // 关闭输入流
            inputStream.close();

            // 构建返回的 URL（根据你的 OSS 区域调整）
            // 格式：https://bucketName.region.aliyuncs.com/objectName
            String ossUrl = String.format("https://%s.%s/%s", bucketName, endpoint, objectName);

            log.info("文件上传成功，URL：{}", ossUrl);
            return ossUrl;

        } catch (IOException e) {
            log.error("文件上传失败：", e);
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 删除 OSS 中的文件
     * @param imageUrl 文件的完整 URL 或对象名称
     */
    public void deleteImage(String imageUrl) {
        try {
            // 如果传入的是完整 URL，需要提取对象名称
            String objectName = imageUrl;
            if (imageUrl.contains("aliyuncs.com/")) {
                objectName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                // 如果 URL 包含完整路径，需要保留文件夹
                int lastSlash = imageUrl.lastIndexOf("/");
                int domainEnd = imageUrl.indexOf("aliyuncs.com/") + "aliyuncs.com/".length();
                objectName = imageUrl.substring(domainEnd);
            }

            ossClient.deleteObject(bucketName, objectName);
            log.info("文件删除成功：{}", objectName);
        } catch (Exception e) {
            log.error("文件删除失败：", e);
            throw new RuntimeException("文件删除失败：" + e.getMessage());
        }
    }
}
