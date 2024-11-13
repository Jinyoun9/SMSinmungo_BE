package com.smsinmungo.service;

import com.smsinmungo.repository.PostRepository;
import com.smsinmungo.dto.PostDto;
import com.smsinmungo.exception.CustomException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import com.smsinmungo.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostStaffServiceImpl implements PostStaffService {
    private PostRepository postRepository;

    @Autowired
    public PostStaffServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public List<PostDto> getList(String category, String department){
        List<Post> posts = postRepository.findAllByCategoryAndDepartment(category, department);

        if (posts == null){
            throw new EntityNotFoundException("There is no post");
        }

        return posts.stream()
                .map(post -> PostDto.builder()
                        .post_id(post.getPost_id())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .write_datetime(post.getWrite_datetime())
                        .good(post.getGood())
                        .department(post.getDepartment())
                        .view(post.getView())
                        .status(post.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostDto getPost(Long post_id){
        Optional<Post> optionalPost = postRepository.findById(post_id);
        if (optionalPost.isEmpty()){
            throw new EntityNotFoundException("There is no post");
        }
        Post post = optionalPost.get(); //[요청] 상세조회할 때 화면에 보이는 요소(파일 위치, 제목 존재여부, 작성자 여부 등)
        return PostDto.builder()
                .post_id(post.getPost_id())
                .title(post.getTitle())
                .content(post.getContent())
                .department(post.getDepartment())
                .file(post.getFile())
                .status(post.getStatus()).build();
    }

    @Override
    @Transactional
    public void updatePostStatus(Long post_id, String status){ //[요청] 한번 완료로 update 된 글은 status 수정이 다시 안되는지
                                                                //그러면 관련 확인 및 예외처리 필요
        Optional<Post> optionalPost = postRepository.findById(post_id);
        if (optionalPost.isEmpty()) {
            throw new EntityNotFoundException("There is no post");
        }
        Post post = optionalPost.get();
        post.setStatus(status);
        try {
            postRepository.save(post);
        } catch (Exception e) {
            throw new CustomException("Failed to update post status");
        }

    }
}
