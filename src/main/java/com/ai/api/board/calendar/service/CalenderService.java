package com.ai.api.board.calendar.service;

import com.ai.api.board.calendar.dto.CalendarReqDTO;
import com.ai.api.board.calendar.repository.CalendarRepository;
import com.ai.api.board.domain.Calendar;
import com.ai.api.resource.service.AttachmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CalenderService {

    private final CalendarRepository calendarRepository;
    private final AttachmentService attachmentService;

    public List<Calendar>  getAllCalendars(Pageable pageable) {
        return calendarRepository.findAll(pageable).getContent();
    }

    public Calendar getDetailCalendar(Long id) {
        calendarRepository.incrementViewCount(id);

        return calendarRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid calendar id: " + id));
    }

    public List<Calendar> searchCalendar(String keyword, Pageable pageable) {
        return calendarRepository.searchByTitle(keyword, pageable).getContent();
    }

    public Calendar saveCalendar(CalendarReqDTO calendarReqDTO) {
        Calendar calendar = Calendar.builder()
            .title(calendarReqDTO.getTitle())
            .content(calendarReqDTO.getContent())
            .isNotice(calendarReqDTO.isNotice())
            .startedAt(calendarReqDTO.getStartedAt())
            .endedAt(calendarReqDTO.getEndedAt())
            .view_count(0)
            .build();
        Calendar savedCalendar = calendarRepository.save(calendar);

        if(calendarReqDTO.getAttachments() != null) {
            attachmentService.savePostAttachments(null, calendarReqDTO.getAttachments(), savedCalendar);
        }
        return savedCalendar;
    }

    public Calendar updateCalendar(Long id, CalendarReqDTO calendarReqDTO) {
        Calendar calendar = calendarRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid calendar id: " + id));

        calendar.setTitle(calendarReqDTO.getTitle());
        calendar.setContent(calendarReqDTO.getContent());
        calendar.setNotice(calendarReqDTO.isNotice());
        calendar.setStartedAt(calendarReqDTO.getStartedAt());
        calendar.setEndedAt(calendarReqDTO.getEndedAt());

        if(calendarReqDTO.getAttachments() != null) {
            attachmentService.deletePostAttachments(calendar);
            attachmentService.savePostAttachments(null, calendarReqDTO.getAttachments(), calendar);
        }
        return calendar;
    }

    public void deleteCalendar(Long id) {
        Calendar calendar = calendarRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid calendar id: " + id));

        if(calendar.getPostAttachments()!=null){
            attachmentService.deletePostAttachments(calendar);
        }

        calendarRepository.deleteById(id);
    }



}
