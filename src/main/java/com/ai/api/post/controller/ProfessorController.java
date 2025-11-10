package com.ai.api.post.controller;

import com.ai.api.board.domain.BoardCategory;
import com.ai.api.post.service.PostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/professor")
public class ProfessorController extends BasePostController {

    public ProfessorController(PostService postService) {
        super(postService);
    }

    @Override
    protected BoardCategory getBoardCategory() {
        return BoardCategory.PROFESSOR;
    }
}