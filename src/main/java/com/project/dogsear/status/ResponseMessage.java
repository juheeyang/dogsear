package com.project.dogsear.status;

public class ResponseMessage {
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String FAIL_CREATED_USER = "회원 가입 실패";

    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";

    public static final String LOGIN_FAIL_MISMATCH_INFO = "이메일과 비밀번호가 일치하지 않습니다.";

    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";

    public static final String FAIL_CREATED_USER_SIGNED_EMAIL = "이미 가입되어 있는 유저입니다.";
    public static final String FAIL_CREATED_USER_INCORRECT_EMAIL_FORMAT = "잘못된 이메일 형식입니다.";
    public static final String FAIL_CREATED_USER_INCORRECT_PASSWORD_CHECK = "비밀번호 확인이 일치하지 않습니다.";
    public static final String FAIL_INCORRECT_PASSWORD_FORMAT = "비밀번호 형식이 올바르지 않습니다. (영문 숫자를 포함하여 10자 이상 20자 이내)";

    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String FAIL_UPDATE_USER = "회원 정보 수정 실패";

    public static final String FAIL_UPDATE_USER_NULL_NICKNAME = "닉네임이 입력되지 않았습니다.";
    public static final String FAIL_UPDATE_USER_IMAGE_NOT_SELECTED = "이미지가 선택되지 않았습니다.";

    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String FAUL_DELETE_USER = "회원 탈퇴 실패";

    public static final String DELETE_USER_IMAGE = "회원 이미지를 삭제했습니다.";
    public static final String FAIL_DELETE_USER_IMAGE = "회원 이미지를 삭제하지 못했습니다.";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String DB_ERROR = "데이터베이스 에러";


    public static final String SEARCH_SUCCESS = "검색 성공";

    public static final String SEARCH_FAIL = "최근 검색 기록 읽기 실패";
    public static final String READ_SEARCH = "최근 검색 기록 읽기 성공";


    public static final String READ_QUOTE = "인용구 조회 성공";
    public static final String FAIL_READ_QUOTE_UNAUTHORIZED = "인용구 조회 실패. 해당 권한이 없습니다.";
    public static final String CREATED_QUOTE = "인용구가 등록되었습니다.";
    public static final String FAIL_CREATED_QUOTE = "인용구 등록이 실패했습니다.";
    public static final String CORRECT_QUOTE="인용구가 수정되었습니다.";
    public static final String FAIL_CORRECT_QUOTE = "인용구 수정 실패";
    public static final String QUOTE_DELETE_SUCCESS = "인용구 삭제 성공";

    public static final String SEND_EMAIL="메일 전송 성공";
    public static final String FAIL_SEND_EMAIL="가입되어 있지 않은 이메일 입니다.";

    public static final String CREATED_HASHTAG = "해시태그 작성 성공";
    public static final String HASH_CREATE_FAIL = "해시태그 작성 실패";
    public static final String HASH_FOUND_RESULT= "해시태그 검색 결과";
    public static final String SEARCH_DELETE_SUCCESS="검색 내역 삭제 성공";
    public static final String SEARCH_DELETE_FAIL="검색 내역 삭제 실패";
    public static final String SEARCH_FAIL_NOT_FOUND= "검색 결과가 없습니다.";

    public static final String HASH_FOUND_RESULT_SUCCESS = "해시태그 검색 내역 저장 성공";
    public static final String HASH_FOUND_RESULT_EXSIT = "이미 존재하는 해시태그: 검색 내역 업데이트";
    public static final String HASH_FOUND_RESULT_NOT_FOUND = "검색한 해시태그가 존재하지 않습니다.";
    public static final String NO_HASH_TEXT = "해시태그 텍스트 공백";
    public static final String HASH_BOOKMARK_SET = "해시태그 북마크 세팅 완료";
    public static final String READ_HASHTAG="모든 해시태그 불러오기 결과";
    public static final String READ_HASHTAG_TOP3="사용 상위3 해시태그 불러오기 결과";
    public static final String HASH_FOUND_NO_RESULT ="검색 결과 없음";
    public static final String HASH_EDITED ="해시태그 수정 성공";
    public static final String HASH_EDIT_USED_NAME ="이미 등록된 해시태그 입니다.";
    public static final String HASH_DELETE_SUCCESS = "해시태그 삭제 성공";
    public static final String HASH_HITS_FOUND_SUCCESS = "가장 많이 조회한 태그 조회 성공";
    public static final String HASH_HITS_FOUND_NULL = "가장 많이 조회한 태그 없음";
    public static final String HASH_BOOKMARK_FOUND_SUCCESS = "즐겨찾는 해시태그 조회 성공";
    public static final String HASH_BOOKMARK_FOUND_NULL = "즐겨찾는 해시태그 없음";
    public static final String HASHTAG_TO_QUOTE="인용구 가져오기 성공";
    public static final String FAIL_TO_FIND_HASHTAG="존재하지 않는 해시태그 입니다.";
    public static final String HASH_QUOTE_READ_SUCCESS="인용구 리스트 불러오기 성공";
    public static final String QUOTE_DATE_RETURN_SUCCESS = "날짜 조회 성공";
    public static final String FAIL_NICKNAME_NULL = "닉네임을 입력해주세요";
    public static final String FAIL_EMAIL_NULL = "이메일을 입력해주세요";
    public static final String OLD_PASSWORD_INCORRECT = "현재 비밀번호가 일치하지 않습니다.";
    public static final String HASH_RECENTLY_USED_ROAD_SUCCESS = "최근 사용한 해시태그 불러오기 결과";

    public static final String SUCCESS_EDIT_IMAGE = "이미지가 변경되었습니다.";

    public static final String FAIL_CREATE_QUOTE_QUOTE="인용구가 비었습니다.";
    public static final String FAIL_CREATE_QUOTE_TITLE="제목이 비었습니다.";
    public static final String FAIL_CREATE_QUOTE_WRITER="저자가 비었습니다.";

    public static final String EMPTY_QUOTE="작성된 인용구가 존재하지 않습니다.";

    public static final String FAIL_CHECK_USER="유저 정보를 확인 할 수 없습니다.";
    public static final String NOT_EXIST_QUOTE="존재하지 않는 인용구입니다.";
    public static final String FAIL_EDIT_QUOTE_UNAUTHORIZED="인용구 수정 실패. 해당 권한이 없습니다.";
    public static final String FAIL_HASH_TEXT_NULL = "해시태그가 비었습니다.";
}