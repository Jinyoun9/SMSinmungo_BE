package service;

import dto.PostDto;
import java.util.List;

public interface PostStaffService {
    List<PostDto> getList(String category, String department); //카테고리에 따른 민원 글 목록 조회
    PostDto getPost(Long post_id); //글 조회
    void updatePostStatus(Long post_id, String status); //change post status
}
