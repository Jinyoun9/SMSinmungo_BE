package inyro.spring.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CursorResult<T> {
    private List<T> content;
    private Long nextCursor;
    private boolean hasNext;

    public CursorResult(List<T> content, Long nextCursor, boolean hasNext) {
        this.content = content;
        this.nextCursor = nextCursor;
        this.hasNext = hasNext;
    }
}
