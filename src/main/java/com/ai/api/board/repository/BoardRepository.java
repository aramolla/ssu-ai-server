package com.ai.api.board.repository;


import com.ai.api.board.domain.Board;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByBoardEnName(String boardEnName);
    boolean existsByBoardEnName(String boardEnName);
    void deleteByBoardEnName(String boardEnName);

}
