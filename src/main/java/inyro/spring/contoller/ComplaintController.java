package inyro.spring.contoller;

import inyro.spring.Service.ComplaintService;
import inyro.spring.dto.ComplaintRequestsDto;
import inyro.spring.dto.ComplaintResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ComplaintController {
    private final ComplaintService complaintService;

    @PostMapping("/complaints/post")
    public ComplaintResponseDto createPost(@RequestBody ComplaintRequestsDto requestsDto){
        return complaintService.createPost(requestsDto);
    }
}
