package fun.cyhgraph.service.sms;




import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
@EnableConfigurationProperties(AliyunSMSProperties.class) // 启用配置属性
public class AliyunSMSConfiguration {

    @Resource
    private AliyunSMSProperties smsProperties;

    /**
     * 创建短信客户端（单例，避免重复创建）
     */
    @Bean
    @ConditionalOnMissingBean // 防止重复注入
    public Client aliyunSmsClient() throws Exception {
        // 1. 校验必填参数（避免空指针）
        if (smsProperties.getEndpoint() == null
                || smsProperties.getSignName() == null
                || smsProperties.getTemplateCode() == null) {
            throw new IllegalArgumentException("阿里云短信认证：endpoint、signName、templateCode不能为空！");
        }

        // 2. 初始化配置（超时时间用默认或配置值）
        Config config = new Config()
                .setAccessKeyId(smsProperties.getAccessKeyId())
                .setAccessKeySecret(smsProperties.getAccessKeySecret())
                .setRegionId("cn-hangzhou")
                .setEndpoint(smsProperties.getEndpoint())
                .setConnectTimeout(smsProperties.getConnectTimeout())
                .setReadTimeout(smsProperties.getReadTimeout());

        // 3. 配置Credentials（自动读取凭据链：文件→环境变量）
        //com.aliyun.credentials.Client credential = new com.aliyun.credentials.Client();
        //config.setCredential(credential);

        // 4. 创建并返回客户端
        return new Client(config);
    }
}
