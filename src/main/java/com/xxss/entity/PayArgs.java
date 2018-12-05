package com.xxss.entity;
/**
 * 支付请求参数对象
 * @author Administrator
 *
 */
public class PayArgs {
	private String merchant;
	
	private String qrtype;
	
	private String customno;
	
	private int money;
	
	private long sendtime;
	
	private String notifyurl;
	
	private String backurl;
	
	private String risklevel;
	
	private String sign;

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getQrtype() {
		return qrtype;
	}

	public void setQrtype(String qrtype) {
		this.qrtype = qrtype;
	}

	public String getCustomno() {
		return customno;
	}

	public void setCustomno(String customno) {
		this.customno = customno;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public long getSendtime() {
		return sendtime;
	}

	public void setSendtime(long sendtime) {
		this.sendtime = sendtime;
	}

	public String getNotifyurl() {
		return notifyurl;
	}

	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}

	public String getBackurl() {
		return backurl;
	}

	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}

	public String getRisklevel() {
		return risklevel;
	}

	public void setRisklevel(String risklevel) {
		this.risklevel = risklevel;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
	
}
