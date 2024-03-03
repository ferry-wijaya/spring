package cc.kfy.blitzmart.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import static com.twilio.rest.api.v2010.account.Message.*;

public class SmsUtils {
    public static final String FROM_NUMBER="+13868558538";
    public static final String TO_NUMBER_WA = "whatsapp:+6282260888858";
    public static final String FROM_NUMBER_WA = "whatsapp:+14155238886";
    public static final String SID_KEY="AC24ed48f463f471e265a3bfa0cf4a7e67";
    public static final String SID_KEY_WA ="AC8e6923969d0ba164affa50aa82632d23";
    public static final String TOKEN_KEY="a9cd6ddc2ee7bb8e5556f8e3a7362b4e";
    public static final String TOKEN_KEY_WA ="e57f953ffc53fa0ab38b8827bf7b95ab";

    public static void sendSMS(String to, String messageBody) {

        // // Via sms
        //Twilio.init(SID_KEY, TOKEN_KEY);
        //Message message = creator(new PhoneNumber("+" + to ), new PhoneNumber(FROM_NUMBER), messageBody).create();
        // // Via Whatsapp
        Twilio.init(SID_KEY_WA, TOKEN_KEY_WA);
        Message message = creator(new PhoneNumber(TO_NUMBER_WA), new PhoneNumber(FROM_NUMBER_WA), messageBody).create();
        System.out.println(message);
    }
}
