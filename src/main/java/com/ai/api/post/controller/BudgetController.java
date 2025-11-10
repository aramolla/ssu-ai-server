package com.ai.api.post.controller;

import com.ai.api.board.domain.BoardCategory;
import com.ai.api.post.service.PostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budget")
public class BudgetController extends BasePostController {

    public BudgetController(PostService postService) {
        super(postService);
    }

    @Override
    protected BoardCategory getBoardCategory() {
        return BoardCategory.BUDGET;
    }
}