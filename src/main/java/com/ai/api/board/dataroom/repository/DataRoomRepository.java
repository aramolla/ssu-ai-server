package com.ai.api.board.dataroom.repository;

import com.ai.api.board.domain.DataRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRoomRepository extends JpaRepository<DataRoom, Long> {

    @Modifying
    @Query("UPDATE DataRoom d SET d.view_count = COALESCE(d.view_count, 0) + 1 WHERE d.id = :id")
    void incrementViewCount(@Param("id") Long id);

    Page<DataRoom> findAll (Pageable pageable);
    Page<DataRoom> searchByTitle(@Param("keyword") String keyword, Pageable pageable);


}
