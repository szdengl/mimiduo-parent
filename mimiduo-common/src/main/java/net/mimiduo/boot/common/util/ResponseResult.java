package net.mimiduo.boot.common.util;

/***
 * restAPI服务返回结果集
 * 
 * @author darren.deng
 *
 */
public class ResponseResult {

	protected boolean result;// 成功为true，失败为false
	protected String message = "";
	protected Object data;
	protected Integer code = 0;// 它自定义的状态码

	public ResponseResult() {
		super();
	}

	public static ResponseResult success() {
		return new ResponseResult(true);
	}

	public static ResponseResult failure() {
		return new ResponseResult(false);
	}

	public static ResponseResult success(String message) {
		return new ResponseResult(true, message);
	}

	public static ResponseResult failure(String message) {
		return new ResponseResult(false, message);
	}

	public ResponseResult(boolean success) {
		this(success, "");
	}

	public ResponseResult(boolean success, String message) {
		this(success, message, "");
	}

	public ResponseResult(boolean success, String message, Object data) {
		this.result = success;
		this.message = message;
		this.data = data;
		this.code = success ? 0 : -1;
	}

	public ResponseResult(boolean success, int code, String message) {
		this.result = success;
		this.message = message;
		this.code = code;
	}

	public ResponseResult(int code, String message, Object data) {
		this.message = message;
		this.data = data;
		this.code = code;
	}

	public ResponseResult(boolean success, int code, String message, Object data) {
		this.message = message;
		this.data = data;
		this.code = code;
		this.result = success;
	}

	public ResponseResult(int code, String message) {
		this.message = message;
		this.code = code;
	}

	public ResponseResult data(Object data) {
		this.data = data;
		return this;
	}

	public ResponseResult code(Integer code) {
		this.code = code;
		return this;
	}

	public ResponseResult result(boolean result) {
		this.result = result;
		return this;
	}

	public ResponseResult message(String message) {
		this.message = message;
		return this;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}


}
