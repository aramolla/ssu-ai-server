package com.ai.api.board.dataroom.service;

import com.ai.api.board.dataroom.dto.DataRoomReqDTO;
import com.ai.api.board.dataroom.repository.DataRoomRepository;
import com.ai.api.board.domain.DataRoom;
import com.ai.api.resource.service.AttachmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DataRoomService {

    private final DataRoomRepository dataRoomRepository;
    private final AttachmentService attachmentService;

    public List<DataRoom> getAllDataRooms(Pageable pageable) {
        return dataRoomRepository.findAll(pageable).getContent();
    }

    public DataRoom getDetailDataRoom(Long id) {
        dataRoomRepository.incrementViewCount(id);

        return dataRoomRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("DataRoom not found"));
    }

    public List<DataRoom> searchDataRoom(String keyword, Pageable pageable) {
        return dataRoomRepository.searchByTitle(keyword, pageable).getContent();

    }

    public DataRoom saveDataRoom(DataRoomReqDTO dataRoomReqDTO) {
        DataRoom dataRoom = DataRoom.builder()
            .title(dataRoomReqDTO.getTitle())
            .content(dataRoomReqDTO.getContent())
            .isNotice(dataRoomReqDTO.isNotice())
            .view_count(0)
            .build();

        DataRoom savedDataRoom = dataRoomRepository.save(dataRoom);

        if(dataRoomReqDTO.getAttachments() != null){
            attachmentService.savePostAttachments(null, dataRoomReqDTO.getAttachments(), savedDataRoom);
        }

        return savedDataRoom;

    }

    public DataRoom updateDataRoom(Long id, DataRoomReqDTO dataRoomReqDTO) {
        DataRoom updateDataRoom = dataRoomRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("DataRoom not found"));

        updateDataRoom.setTitle(dataRoomReqDTO.getTitle());
        updateDataRoom.setContent(dataRoomReqDTO.getContent());
        updateDataRoom.setNotice(dataRoomReqDTO.isNotice());

        if(dataRoomReqDTO.getAttachments() != null){
            attachmentService.deletePostAttachments(updateDataRoom);
            attachmentService.savePostAttachments(null, dataRoomReqDTO.getAttachments(), updateDataRoom);
        }

        return updateDataRoom;
    }

    public void deleteDataRoom(Long id) {
        DataRoom dataRoom = dataRoomRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("DataRoom not found"));

        if(dataRoom.getPostAttachments()!=null){
            attachmentService.deletePostAttachments(dataRoom);
        }

        dataRoomRepository.deleteById(id);
    }

}
