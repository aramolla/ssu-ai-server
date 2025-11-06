package com.ai.api.board.domain;

import com.ai.api.board.notice.domain.Category;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "notice")
@DiscriminatorValue("NOTICE")
public class Notice extends Post{

    private Category category;

}
