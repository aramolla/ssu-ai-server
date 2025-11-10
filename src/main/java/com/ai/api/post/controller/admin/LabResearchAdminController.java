package com.ai.api.post.controller.admin;

import com.ai.api.board.domain.BoardCategory;
import com.ai.api.post.service.PostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/lab-research")
public class LabResearchAdminController extends BasePostAdminController {

    public LabResearchAdminController(PostService postService) {
        super(postService);
    }

    @Override
    protected BoardCategory getBoardCategory() {
        return BoardCategory.GATHERING;
    }
}
