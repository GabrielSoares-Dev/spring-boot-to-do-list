package spring_boot_to_do_list.spring_boot_to_do_list.application.exceptions;

public class BusinessException extends Exception {
  public BusinessException(String message) {
    super(message);
  }
}
