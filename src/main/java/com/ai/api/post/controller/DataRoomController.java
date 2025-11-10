package com.ai.api.post.controller;

import com.ai.api.board.domain.BoardCategory;
import com.ai.api.post.service.PostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data-room")
public class DataRoomController extends BasePostController {

    public DataRoomController(PostService postService) {
        super(postService);
    }

    @Override
    protected BoardCategory getBoardCategory() {
        return BoardCategory.DATAROOM;
    }
}

