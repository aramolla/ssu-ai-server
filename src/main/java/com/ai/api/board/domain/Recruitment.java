package com.ai.api.board.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "recuitment")
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("RECUITMENT")
public class Recruitment extends Post{

}
