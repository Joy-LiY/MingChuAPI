package com.example.mingchuapi.enums;

public enum AndmuCode {
	SUCCESS("000000", "成功"),
	SERVER_INTERNAL_ERROR("000001", "服务器内部错误"),
	REQUEST_FORMAT_ILLEGAL_ERROR("000002", "请求内容格式不合法"),
	REQUEST_PARAM_ILLEGAL_ERROR("000003", "请求参数不合法"),
	SIGNATURE_VERIFY_FAILED_ERROR("000004", "签名验证失败"),
	ILLEGAL_ACCESS_ERROR("000005", "非法访问"),
	KEY_NOT_EXIST_ERROR("11501", "key不存在"),
	SIGNATURE_INVALID_ERROR("11502", "签名无效"),
	TOKEN_INVALID_ERROR("11503", "token无效"),
	TOKEN_EXPIRATION_ERROR("11504", "token过期"),
	URL_ENCODING_ERROR("12054", "url编码异常"),
	INSUFFICIENT_PERMISSION_ERROR("13001", "权限不足");

	private String ecode;

	private String emsg;

	AndmuCode(String ecode, String emsg) {
		this.ecode = ecode;
		this.emsg = emsg;
	}

	public String getEcode() {
		return ecode;
	}

	public String getEmsg() {
		return emsg;
	}

	public static AndmuCode statOf(String ecode) {
		for (AndmuCode state : values())
			if (state.getEcode().equals(ecode))
				return state;
		return null;
	}
}
