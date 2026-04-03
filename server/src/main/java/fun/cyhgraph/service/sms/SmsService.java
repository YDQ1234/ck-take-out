package fun.cyhgraph.service.sms;

public interface SmsService {
    boolean sendMsg(String phone, String code);
}
