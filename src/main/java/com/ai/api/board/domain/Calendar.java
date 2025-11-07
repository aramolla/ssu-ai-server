package com.ai.api.board.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "calendar")
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("CALENDAR")
@Data
public class Calendar extends Post{

    private LocalDate startedAt;
    private LocalDate endedAt;

}
