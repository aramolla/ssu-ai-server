package com.ai.api.board.dataroom.controller;

import com.ai.api.board.dataroom.dto.DataRoomReqDTO;
import com.ai.api.board.dataroom.dto.DataRoomResDTO;
import com.ai.api.board.dataroom.service.DataRoomService;
import com.ai.api.board.domain.DataRoom;
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
@Slf4j
@RequestMapping("/data-room")
@RequiredArgsConstructor
public class DataRoomController {

    private final DataRoomService dataRoomService;

    @GetMapping
    public ResponseEntity<List<DataRoomResDTO>> getAllDataRooms(
        @PageableDefault(sort = "id") Pageable pageable
    ) {
        log.info("자료실 게시판 목록 조회");
        List<DataRoomResDTO> dataRoomList = dataRoomService.getAllDataRooms(pageable)
            .stream()
            .map(DataRoomResDTO::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dataRoomList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataRoomResDTO> getDataRoomById(
        @PathVariable Long id
    ){
        log.info("자료실 상세 조회: {}", id);
        DataRoom detailDataRoom = dataRoomService.getDetailDataRoom(id);
        return ResponseEntity.ok(DataRoomResDTO.from(detailDataRoom));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DataRoomResDTO>> searchDataRooms(
        @Param(value = "keyword") String keyword,
        Pageable pageable
    ){
        log.info("자료실 검색 내용 조회");
        List<DataRoomResDTO> searchDataRoomList = dataRoomService.searchDataRoom(keyword, pageable)
            .stream()
            .map(DataRoomResDTO::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok(searchDataRoomList);
    }

    @PostMapping
    public ResponseEntity<DataRoomResDTO> saveDataRoom(
        @RequestPart("dataRoomReqDTO") DataRoomReqDTO dataRoomReqDTO,
        @RequestPart("attachment") List<MultipartFile> attachment
    ){
        log.info("자료실 게시글 저장: {}", dataRoomReqDTO);
        dataRoomReqDTO.setAttachments(attachment);
        DataRoom saveDataRoom = dataRoomService.saveDataRoom(dataRoomReqDTO);

        return ResponseEntity.ok(DataRoomResDTO.from(saveDataRoom));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataRoomResDTO> updateDataRoom(
        @PathVariable Long id,
        @RequestPart("dataRoomReqDTO") DataRoomReqDTO dataRoomReqDTO,
        @RequestPart("attachment") List<MultipartFile> attachment
    ){
        log.info("자료실 게시글 수정: {}", id);
        dataRoomReqDTO.setAttachments(attachment);
        DataRoom updateDataRoom = dataRoomService.updateDataRoom(id, dataRoomReqDTO);

        return ResponseEntity.ok(DataRoomResDTO.from(updateDataRoom));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataRoomResDTO> deleteDataRoom(
        @PathVariable Long id
    ){
        log.info("자료실 게시글 삭제: {}", id);
        dataRoomService.deleteDataRoom(id);

        return ResponseEntity.noContent().build();
    }




}
