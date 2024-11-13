package inyro.spring.dto;
import inyro.spring.entity.Post;
import lombok.Getter;

@Getter
public class LikeResponseDto {
    private Long postId;
    private int good;  
    
    public LikeResponseDto(Post post) {
        this.postId = post.getId();
        this.good = post.getGood();
    }
}