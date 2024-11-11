package com.bilibili.enumeration;

// HTTP状态码枚举类
public enum HttpStatusEnum {
    CONTINUE(100, "Continue"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    PROCESSING(102, "Processing"),
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    NO_CONTENT(204, "No Content"),
    RESET_CONTENT(205, "Reset Content"),
    PARTIAL_CONTENT(206, "Partial Content"),
    MULTIPLE_CHOICES(300, "Multiple Choices"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    MOVED_TEMPORARILY(302, "Moved Temporarily"),
    FOUND(302, "Found"),
    SEE_OTHER(303, "See Other"),
    NOT_MODIFIED(304, "Not Modified"),
    USE_OTHER(305, "Use Other"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, "Permanent Redirect"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    TIME_OUT(408, "Time Out"),
    CONFLICT(409, "Conflict"),
    GONE(410, "Gone"),
    LENGTH_REQUIRED(411, "Length Required"),
    CONTENT_TYPE_ERROR(412, "Content Type Error"),
    CONTENT_LENGTH_ERROR(413, "Content Length Error"),
    URI_TOO_LONG(414, "Uri Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    RANGE_NOT_ACCEPTABLE(416, "Range Not Acceptable"),
    EXPECTATION_FAILED(417, "Expectation Failed"),
    IMMEDIATE_UPLOAD_REQUIRED(418, "Immediate Upload Required"),
    CONNECTION_TIMEOUT(419, "Connection Timeout"),
    PRECONDITION_FAILED(420, "Precondition Failed"),
    HEADERS_TOO_LARGE(421, "Headers Too Large"),
    UNAVAILABLE_FOR_SERVICE(422, "Unavailable For Service"),
    LOCKED(423, "Locked"),
    METHOD_FAILURE(424, "Method Failure"),
    SLOW_DOWN(425, "Slow Down"),
    UPLOAD_TIMEOUT(426, "Upload Timeout"),
    PASSWORD_CHANGE_REQUIRED(427, "Password Change Required"),
    OTHER_ERROR(428, "Other Error"),
    TOO_MANY_REQUESTS(429, "Too Many Requests"),
    CONNECTION_REFUSED(430, "Connection Refused"),
    CONNECTION_RESET(431, "Connection Reset"),
    CONNECTION_ERROR(432, "Connection Error"),
    BAD_GATEWAY(433, "Bad Gateway"),
    SERVICE_UNAVAILABLE(434, "Service Unavailable"),
    NOT_IMPLEMENTED(435, "Not Implemented"),
    FORBIDDEN_ERROR(436, "Forbidden Error"),
    CONNECTION_CLOSED(437, "Connection Closed"),
    ERROR(438, "Error"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_SUPPORTED(501, "Not Supported"),
    BAD_IMPLEMENTATION(502, "Bad Implementation"),
    OVERLOAD_ERROR(503, "Overload Error"),
    CONNECTION_LOST(504, "Connection Lost"),
    ERROR_505(505, "Error 505"),
    ERROR_506(506, "Error 506"),
    ERROR_507(507, "Error 507"),
    ERROR_508(508, "Error 508"),
    ERROR_509(509, "Error 509"),
    ERROR_510(509, "Error 510");

    private final int code;
    private final String message;

    HttpStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
