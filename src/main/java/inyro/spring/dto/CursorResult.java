package inyro.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CursorResult<T> {
    private List<T> content;
    private Long nextCursor;
    private boolean hasNext;
}
