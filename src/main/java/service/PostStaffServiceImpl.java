package service;

import Repository.PostRepository;
import jakarta.transaction.Transactional;
import model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostStaffServiceImpl implements PostStaffService {
    private PostRepository postRepository;

    @Autowired
    public PostStaffServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public List<Post> getList(String category, String department){
        return postRepository.findAllByCategoryAndDepartment(category, department);
    }

    @Override
    @Transactional
    public Post getPost(Long post_id){
        //if 해당 부서 관리자라면
        Post post = postRepository.getOne(post_id);
        return post;
    }

    @Override
    @Transactional
    public void updatePostStatus(Long post_id, String status){
        Optional<Post> optionalPost = postRepository.findById(post_id);

        if (optionalPost.isPresent()){
            Post post = optionalPost.get();
            post.setStatus(status);
            postRepository.save(post);
        }

    }
}
