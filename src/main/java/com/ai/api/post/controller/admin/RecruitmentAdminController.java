package com.ai.api.post.controller.admin;

import com.ai.api.board.domain.BoardCategory;
import com.ai.api.post.service.PostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/recruitment")
public class RecruitmentAdminController extends BasePostAdminController {

    public RecruitmentAdminController(PostService postService) {
        super(postService);
    }

    @Override
    protected BoardCategory getBoardCategory() {
        return BoardCategory.RECRUITMENT;
    }
}
