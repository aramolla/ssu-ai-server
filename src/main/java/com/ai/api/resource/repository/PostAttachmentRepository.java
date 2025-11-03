package com.ai.api.resource.repository;

import com.ai.api.resource.domain.PostAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostAttachmentRepository extends JpaRepository<PostAttachment, Long> {
}