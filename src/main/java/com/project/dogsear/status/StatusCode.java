package com.project.dogsear.status;

// 정보 미입력 -> 600번대
// 정보 불일치 -> 700번대
// 중복 데이터 -> 800번대
// 찾을 수 없는 데이터 요청 -> 500번대
// 그 외의 실행 실패 경우 -> 900번대

public class StatusCode {
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int NO_CONTENT = 204;
    public static final int BAD_REQUEST =  400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int DB_ERROR = 600;
    public static final int FAIL_CREATED_USER_INCORRECT_EMAIL_FORMAT = 700;
    public static final int LOGIN_FAIL_MISMATCH_INFO = 701;
    public static final int FAIL_NICKNAME_NULL = 601;
    public static final int FAIL_EMAIL_NULL = 602;
    public static final int FAIL_CREATED_USER_INCORRECT_PASSWORD_CHECK = 702;
    public static final int FAIL_INCORRECT_PASSWORD_FORMAT = 703;
    public static final int FAIL_CREATED_USER_SIGNED_EMAIL = 801;
    public static final int FAIL_UPDATE_USER_NULL_NICKNAME = 603;
    public static final int OLD_PASSWORD_INCORRECT = 704;
    public static final int FAIL_UPDATE_USER = 604;
    public static final int FAIL_UPDATE_USER_IMAGE_NOT_SELECTED = 605;
    public static final int FAIL_DELETE_USER_IMAGE = 901;
    public static final int FAIL_DELETE_USER = 902;
    public static final int FAIL_CREATE_QUOTE_QUOTE = 606;
    public static final int FAIL_CREATE_QUOTE_TITLE = 607;
    public static final int FAIL_CREATE_QUOTE_WRITER = 608;
    public static final int NOT_EXIST_QUOTE = 505;
    public static final int HASH_EDIT_USED_NAME = 802;
    public static final int HASH_CREATE_FAIL = 903;
    public static final int FAIL_TO_FIND_HASHTAG = 506;
    public static final int FAIL_SEND_EMAIL = 705;
    public static final int HASH_FOUND_RESULT_NOT_FOUND = 507;
    public static final int FAIL_HASH_TEXT_NULL = 609;
}