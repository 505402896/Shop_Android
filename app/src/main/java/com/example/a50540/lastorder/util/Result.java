package com.example.a50540.lastorder.util;

import org.json.JSONObject;

import java.io.Serializable;

public class Result implements Serializable {
  private int code;
  private Object data;
  private String message;

  public Result() {}

  public Result(int code, Object data, String message) {
    this.code = code;
    this.data = data;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "Result{" +
            "code=" + code +
            ", data=" + data +
            ", message='" + message + '\'' +
            '}';
  }
}
