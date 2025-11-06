package com.ai.api.board.notice.service;

import com.ai.api.board.domain.Notice;
import com.ai.api.board.notice.dto.NoticeReqDTO;
import com.ai.api.board.notice.repository.NoticeRepository;
import com.ai.api.resource.service.AttachmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AttachmentService attachmentService;

    public List<Notice> getAllNotices(Pageable pageable) {
        List<Notice> notices = noticeRepository.findAll(pageable).getContent();
        return notices;
    }

    public Notice getDetailNotice(Long id) {
        noticeRepository.incrementViewCount(id); // 중복 조회 여부 무시

        return noticeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid notice"));

    }

    public List<Notice> searchNotices(String keyword, Pageable pageable) {
        List<Notice> searchNotices = noticeRepository.findByTitle(keyword, pageable).getContent();
        return searchNotices;
    }

    public Notice saveNotice(NoticeReqDTO noticeReq) {
        Notice notice = Notice.builder()
            .title(noticeReq.getTitle())
            .content(noticeReq.getContent())
            .category(noticeReq.getCategory())
            .isNotice(noticeReq.isNotice())
            .build();

        Notice saveNotice = noticeRepository.save(notice);

        if(noticeReq.getAttachments() != null) {
            attachmentService.savePostAttachments(null, noticeReq.getAttachments(), saveNotice);
        }

        log.info("Saved notice : {}", saveNotice);

        return saveNotice;
    }

    public Notice updateNotice(Long id, NoticeReqDTO noticeReq) {
        Notice notice = noticeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid notice"));

        notice.setTitle(noticeReq.getTitle());
        notice.setContent(noticeReq.getContent());
        notice.setCategory(noticeReq.getCategory());
        notice.setNotice(noticeReq.isNotice());

        Notice updateNotice = noticeRepository.save(notice);

        if(noticeReq.getAttachments() != null) {
            attachmentService.deletePostAttachments(notice);
            attachmentService.savePostAttachments(null, noticeReq.getAttachments(), updateNotice);
        }

        return updateNotice;
    }

    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid notice"));

        if(notice.getPostAttachments() != null){
            attachmentService.deletePostAttachments(notice);
        }
        noticeRepository.deleteById(id);
    }

}
