package inyro.spring.contoller;

import inyro.spring.Service.ComplaintService;
import inyro.spring.dto.ComplaintRequestsDto;
import inyro.spring.dto.ComplaintResponseDto;
import inyro.spring.dto.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ComplaintController {
    private final ComplaintService complaintService;

    @GetMapping("/complaints")
    public List<ComplaintResponseDto> getPosts() {
        return complaintService.getPosts();
    }

    @PostMapping("/complaints/post")
    public ComplaintResponseDto createPost(@RequestBody ComplaintRequestsDto requestsDto){
        return complaintService.createPost(requestsDto);
    }

    @GetMapping("/complaint/post/{id}")
    public ComplaintResponseDto getPost(@PathVariable Long id) {
        return complaintService.getPost(id);
    }

    @PutMapping("/complaint/modify/{id}")
    public ComplaintResponseDto updatePost(@PathVariable Long id, @RequestBody ComplaintRequestsDto requestsDto) throws Exception {
        return complaintService.updatePost(id, requestsDto);
    }

    @DeleteMapping("/complaint/delete/{id}")
    public SuccessResponseDto deletePost(@PathVariable Long id, @RequestBody ComplaintRequestsDto requestsDto) throws Exception {
        return complaintService.deletePost(id, requestsDto);
    }
}