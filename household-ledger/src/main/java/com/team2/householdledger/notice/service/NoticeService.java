package com.team2.householdledger.notice.service;

import com.team2.householdledger.notice.dto.NoticeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    /**
     * 내 알림 조회
     */
    public List<NoticeDTO> getMyNotices(Long userNo) {
        // 테이블을 사용하지 않으므로 빈 리스트 반환
        return Collections.emptyList();
    }

    /**
     * 알림 읽음 처리
     */
    public void readNotice(Long noticeNo) {
        // 기능 미사용
    }

    /**
     * (시스템 내부용) 알림 발송
     */
    public void sendNotice(Long userNo, String message) {
        // 기능 미사용 (로그 출력 등으로 대체 가능)
        System.out.println("[알림 발송] userNo=" + userNo + ", msg=" + message);
    }
}
