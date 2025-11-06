package com.ai.api.board.notice.controller;

import com.ai.api.board.domain.Notice;
import com.ai.api.board.notice.dto.NoticeReqDTO;
import com.ai.api.board.notice.dto.NoticeResDTO;
import com.ai.api.board.notice.service.NoticeService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/notice")
@Slf4j
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<NoticeResDTO>> getNotices(
        @PageableDefault(sort = "id", size = 10) Pageable pageable
    ) {
        log.info("공지사항 게시판 전체 조회");
        List<NoticeResDTO> noticeList = noticeService.getAllNotices(pageable)
            .stream()
            .map(NoticeResDTO::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(noticeList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeResDTO> getDetailNotice(
        @PathVariable Long id
    ){
        log.info("공지사항 게시글 {} 상세 조회", id);
        Notice detailNotice = noticeService.getDetailNotice(id);
        return ResponseEntity.ok(NoticeResDTO.from(detailNotice));
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoticeResDTO>> searchNotices(
        @Param(value = "keyword") String keyword, Pageable pageable
    ){
        log.info("공지사항 검색 내용 조회");
        List<NoticeResDTO> searchNoticeResponse = noticeService.searchNotices(keyword, pageable)
            .stream()
            .map(NoticeResDTO::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok(searchNoticeResponse);
    }

    @PostMapping
    public ResponseEntity<NoticeResDTO> saveNotice(
        @RequestPart("noticeReqDTO") NoticeReqDTO noticeReqDTO,
        @RequestPart("attachments") List<MultipartFile> attachments
    ){
        log.info("공지사항 게시글 저장");
        noticeReqDTO.setAttachments(attachments);
        Notice saveNotice = noticeService.saveNotice(noticeReqDTO);

        return ResponseEntity.ok(NoticeResDTO.from(saveNotice));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticeResDTO> updateNotice(
        @PathVariable Long id,
        @RequestPart("noticeReqDTO") NoticeReqDTO noticeReqDTO,
        @RequestPart("attachments") List<MultipartFile> attachments
    ){
        log.info("공지사항 업데이트");
        noticeReqDTO.setAttachments(attachments);
        Notice updateNotice = noticeService.updateNotice(id, noticeReqDTO);
        return ResponseEntity.ok(NoticeResDTO.from(updateNotice));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NoticeResDTO> deleteNotice(
        @PathVariable Long id
    ){
        log.info("공지사항 게시글 삭제");
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }

}
