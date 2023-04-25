package realsoft.carservice.cargateway.exceptions;

import java.time.Instant;

public class CustomErrorModel  {

  private final String message;
  private final String code;
  private final int status;
  private final long timestamp;

  public CustomErrorModel(String message, String code, int status) {
    this.message = message;
    this.code = code;
    this.status = status;
    this.timestamp = Instant.now().toEpochMilli();
  }

  public String getMessage() {
    return message;
  }

  public String getCode() {
    return code;
  }

  public int getStatus() {
    return status;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
