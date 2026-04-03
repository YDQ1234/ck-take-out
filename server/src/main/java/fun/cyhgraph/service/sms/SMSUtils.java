package fun.cyhgraph.service.sms;

import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.teautil.models.RuntimeOptions;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

/**
 * 短信发送工具类
 */
@Component
public class SMSUtils {

	@Autowired
	private AliyunSMSProperties smsProperties;

	@Resource
	private Client aliyunSmsClient;

	/*@Value("${aliyun.sms.accessKeyId}")
	private String accessKeyId;
	@Value("${aliyun.sms.accessKeySecret}")
	private String accessKeySecret;
	@Value("${aliyun.sms.signName}")
	private String signName;
	@Value("${aliyun.sms.templateCode}")
	private String templateCode;*/

	// 手机号正则（简单校验）
	private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
	//public void sendMessage(String signName,String phoneNumbers, String param){
	public void sendMessage(String phone, String code) throws Exception{
		// 1. 校验手机号
		Assert.isTrue(PHONE_PATTERN.matcher(phone).matches(), "手机号格式错误！");
		// 2. 校验验证码（假设6位数字）
		Assert.isTrue(code.matches("^\\d{6}$"), "验证码必须为6位数字！");

		// 3. 构建短信请求参数（模板参数必须含code和min）
		SendSmsVerifyCodeRequest request = new SendSmsVerifyCodeRequest()
				.setSignName(smsProperties.getSignName()) // 签名
				.setTemplateCode(smsProperties.getTemplateCode()) // 模板CODE
				.setPhoneNumber(phone) // 手机号
				// 模板参数：JSON格式，必须包含"code"（验证码）和"min"（有效期）
				.setTemplateParam("{\"code\":\"" + code + "\",\"min\":\"" + smsProperties.getCodeExpireMinutes() + "\"}");

		// 4. 配置运行时选项（超时时间可自定义）
		RuntimeOptions runtime = new RuntimeOptions()
				.setConnectTimeout(smsProperties.getConnectTimeout())
				.setReadTimeout(smsProperties.getReadTimeout());

		// 5. 调用接口发送短信（捕获异常可自定义处理）
		try {
			aliyunSmsClient.sendSmsVerifyCodeWithOptions(request, runtime);
			System.out.println("短信发送成功！手机号：" + phone + "，验证码：" + code);
		} catch (Exception e) {
			System.err.println("短信发送失败！原因：" + e.getMessage());
			throw new Exception("短信发送失败，请稍后重试", e);
		}
	}
}
