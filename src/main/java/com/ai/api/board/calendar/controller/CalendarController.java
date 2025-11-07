package com.ai.api.board.calendar.controller;

import com.ai.api.board.calendar.dto.CalendarReqDTO;
import com.ai.api.board.calendar.dto.CalendarResDTO;
import com.ai.api.board.calendar.service.CalenderService;
import com.ai.api.board.domain.Calendar;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/calendar")
@Slf4j
@RequiredArgsConstructor
public class CalendarController {

    private final CalenderService calenderService;

    @GetMapping
    public ResponseEntity<List<CalendarResDTO>> getAllCalendars(
        @PageableDefault(sort = "id") Pageable pageable
    ){
        log.info("학사일정 게시판 목록 조회");
        List<CalendarResDTO> calendarList = calenderService.getAllCalendars(pageable)
            .stream()
            .map(CalendarResDTO::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(calendarList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarResDTO> getDetailCalendar(
        @PathVariable Long id
    ){
        log.info("학사일정 상세 조회: {}", id);
        Calendar detailCalendar = calenderService.getDetailCalendar(id);
        return ResponseEntity.ok(CalendarResDTO.from(detailCalendar));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CalendarResDTO>> searchCalendar(
        @Param(value = "keyword") String keyword,
        Pageable pageable
    ) {
        log.info("학사일정 검색 내용 조회");
        List<CalendarResDTO> searchCalendarList = calenderService.searchCalendar(keyword, pageable)
            .stream()
            .map(CalendarResDTO::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok(searchCalendarList);
    }

    @PostMapping
    public ResponseEntity<CalendarResDTO> saveCalendar(
        @RequestPart("calendarReqDTO") CalendarReqDTO calendarReqDTO,
        @RequestPart("attachment") List<MultipartFile> attachment
    ) {
        log.info("학사일정 게시글 저장: {}", calendarReqDTO);
        calendarReqDTO.setAttachments(attachment);
        Calendar saveCalendar = calenderService.saveCalendar(calendarReqDTO);
        return ResponseEntity.ok(CalendarResDTO.from(saveCalendar));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalendarResDTO> updateCalendar(
        @PathVariable Long id,
        @RequestPart("calendarReqDTO") CalendarReqDTO calendarReqDTO,
        @RequestPart("attachment") List<MultipartFile> attachment
    ) {
        log.info("학사일정 게시글 수정: {}", id);
        calendarReqDTO.setAttachments(attachment);
        Calendar updateCalender = calenderService.updateCalendar(id, calendarReqDTO);
        return ResponseEntity.ok(CalendarResDTO.from(updateCalender));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CalendarResDTO> deleteCalendar(
        @PathVariable Long id
    ) {
        log.info("학사일정 게시글 삭제: {}", id);
        calenderService.deleteCalendar(id);
        return ResponseEntity.noContent().build();
    }


}
