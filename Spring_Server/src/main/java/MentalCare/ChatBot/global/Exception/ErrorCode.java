package MentalCare.ChatBot.global.Exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // TODO :Member Error - 6xx
    /* 1. 회원 가입 과정*/
    ALREADY_EXIST_MEMBER(601, "이미 존재하는 사용자입니다."),
    NOT_FOUND_MEMBER(602,"존재하지 않는 사용자 입니다."),
    EMAIL_ALREADY_EXISTS(603, "이미 존재하는 이메일입니다."),
    INVALID_USERNAME(604, "잘못된 사용자 이름입니다."),
    INVALID_EMAIL(605, "잘못된 이메일 형식입니다."),
    PASSWORD_TOO_WEAK(606, "비밀번호가 너무 약합니다."),

    /* 2. 로그인 과정*/
    INVALID_CREDENTIALS(611, "잘못된 사용자 이름 또는 비밀번호입니다."),
    FAILED_TO_LOGIN(612,"로그인 인증 실패"),

    /* 3. 개인 정보 조회/수정 과정*/
    EMAIL_NOT_FOUND(621, "해당 이메일을 가진 사용자를 찾을 수 없습니다."),

    /* 4. JWT 관련 오류*/
    TOKEN_EXPIRED(631, "토큰이 만료되었습니다."),
    INVALID_TOKEN(632, "유효하지 않은 토큰입니다."),
    TOKEN_NOT_PROVIDED(633, "토큰이 제공되지 않았습니다."),
    TOKEN_MUST_FILLED(634,"토큰은 널값이나 비어서는 안됩니다"),


    // TODO : Diary Error - 7xx
    /* 1. 일기 작성 및 저장 과정*/
    EMPTY_DIARY_CONTENT(701, "일기 내용이 비어있습니다."),
    DIARY_SAVE_FAILED(702, "일기 저장에 실패했습니다."),

    /* 2. AI 처리 및 일기 요약 저장 과정*/
    AI_SUMMARY_FAILED(711, "AI 일기 요약에 실패했습니다."),
    AI_EMOTION_ANALYSIS_FAILED(712, "AI 일기 감정 분석에 실패했습니다."),
    SUMMARY_SAVE_FAILED(713, "일기 요약을 저장하는 데 실패했습니다."),

    /* 3. 4컷 만화 생성 및 저장 과정*/
    COMIC_GENERATION_FAILED(721, "4컷 만화 생성에 실패했습니다."),
    COMIC_SAVE_FAILED(722, "4컷 만화를 저장하는 데 실패했습니다."),
    INSTAGRAM_SHARE_FAILED(723, "인스타그램 공유에 실패했습니다."),

    /* 4. 달력에서 일기 및 만화 조회 과정*/
    DIARY_NOT_FOUND_FOR_DATE(731, "해당 날짜에 일기가 없습니다."),
    COMIC_NOT_FOUND(732, "해당 날짜의 만화가 없습니다."),

    /* 5. 데이터베이스 관련 */
    DATABASE_CONNECTION_FAILED(741, "데이터베이스 연결에 실패했습니다."),
    DATA_INTEGRITY_VIOLATION(742, "데이터 무결성 위반이 발생했습니다."),

    /* 6. 기타 오류 */
    REQUEST_TIMEOUT(751, "요청 시간이 초과되었습니다."),
    EXTERNAL_API_CALL_FAILED(752, "외부 API 호출에 실패했습니다."),

    // TODO : Chatting Error - 8xx
    /* 1. 채팅 과정 */
    CHAT_INPUT_EMPTY(801, "채팅 입력이 비어 있습니다."),
    AI_RESPONSE_ERROR(802, "AI 응답 처리 중 오류가 발생했습니다."),

    /* 2. 대화 종료 과정 */
    CONVERSATION_NOT_FOUND(811, "종료할 대화가 존재하지 않습니다."),
    REPORT_GENERATION_FAILED(812, "AI 레포트 생성에 실패했습니다."),

    // TODO : Admin Error - 9xx
    /* 1. 모든 사용자 정보 조회 */
    USER_NOT_FOUND(901, "사용자를 찾을 수 없습니다."),
    USER_DELETION_FAILED(902, "사용자 삭제에 실패했습니다."),
    UNAUTHORIZED_USER_ACCESS(903, "해당 사용자에 대한 접근 권한이 없습니다."),

    /* 2. 감정 통계 조회 */
    STATISTICS_RETRIEVAL_FAILED(911, "감정 통계 조회에 실패했습니다."),
    EMPTY_STATISTICS_DATA(912, "통계 데이터가 존재하지 않습니다."),

    /* 3. AI 레포트 조회 */
    REPORT_NOT_AVAILABLE(921, "AI 레포트가 존재하지 않습니다."),
    REPORT_ACCESS_DENIED(922, "해당 레포트에 접근할 권한이 없습니다.");

    private final int errorCode;
    private final String message;
}
