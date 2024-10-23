package franxx.code.artifacts.system;

public class Result<T> {

  private Boolean flag; // true mean success, false mean not success
  private Integer code; // status code .. e.g., 2--
  private String message; // response message
  private T data; // response payload

  public Result() {}

  public Result(Boolean flag, Integer code, String message) {
    this.flag = flag;
    this.code = code;
    this.message = message;
  }

  public Result(Boolean flag, Integer code, String message, T data) {
    this.flag = flag;
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public Boolean getFlag() {
    return flag;
  }

  public void setFlag(Boolean flag) {
    this.flag = flag;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "Result{" +
        "flag=" + flag +
        ", code=" + code +
        ", message='" + message + '\'' +
        ", data=" + data +
        '}';
  }
}
