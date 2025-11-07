package com.ai.api.board.calendar.repository;

import com.ai.api.board.domain.Calendar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    Page<Calendar> findAll(Pageable pageable);
    Page<Calendar> searchByTitle(@Param("keyword") String title, Pageable pageable);

    @Modifying
    @Query("UPDATE Calendar c SET c.view_count = COALESCE(c.view_count, 0) + 1 WHERE c.id = :id")
    void incrementViewCount(@Param("id") Long id);

}
