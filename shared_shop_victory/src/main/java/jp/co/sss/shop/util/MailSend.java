package jp.co.sss.shop.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailSend {
	
	//これ書けば使えます
	
//	@Autowired
//	MailSend sender;
	
	//お試し用
	//sender.send();
	//setToのメールアドレスを変えて使ってください
	
	//使う用
	//sender.send(必要な引数);
	
	//送信日時取得用
	//getSendDate();
	
	@Autowired
	private MailSender mailSender;
	
	public void send(){
		try {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("katsushiro.tis2022@gmail.com");
		msg.setSubject("メールタイトル");
		msg.setText("テスト用のメールです。");
		mailSender.send(msg);
		}catch(MailException e) {
			System.out.println("メールエラーここから");
			e.printStackTrace();
			System.out.println("メールエラーここまで");
		}
	}
	
	public void send(String sendTo, String sendTitle, String sendText) {
		try {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(sendTo);
		msg.setSubject(sendTitle);
		msg.setText(sendText);
		mailSender.send(msg);
		}catch(MailException e) {
			System.out.println("メールエラーここから");
			e.printStackTrace();
			System.out.println("メールエラーここまで");
		}
	}
	
	
	public String getSendDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日('E')'");
        
        return sdf.format(date);
	}

}
