package controller;

import dto.AlarmDto;
import dto.PostDto;
import exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AlarmServiceImpl;
import service.PostStaffServiceImpl;

import java.net.URI;
import java.util.List;

@RestController
public class PostingController {
    private PostStaffServiceImpl postStaffService;
    private AlarmServiceImpl alarmService;

    @Autowired
    private PostingController(PostStaffServiceImpl postStaffService, AlarmServiceImpl alarmService){
        this.postStaffService = postStaffService;
        this.alarmService = alarmService;
    }

    //[요청] security config 에서 경로 인가 관련 /admin/**으로 수정 요청 -> 관리자만 인가됨
    //->모든 PostingController API 는 /admin 으로 시작


    //알람 관련
    @GetMapping("/admin/alarms") //알람 리스트 조회
    public ResponseEntity<List<AlarmDto>> getAlarms(){
        //부서 토큰에서 get 해와서 부서를 서비스로 넘겨주기
        List<AlarmDto> alarms = alarmService.getAlarms(department);
        return ResponseEntity.ok(alarms);
    }

    @GetMapping("/admin/alarms/{alarm_id}") //특정 알람에 대한 리다이렉션 처리
    public ResponseEntity<Void> handleAlarm(@PathVariable Long alarm_id){
        //alarm_id = post_id
        PostDto postDto = postStaffService.getPost(alarm_id);
        if (postDto.getCategory() == null || postDto.getCategory().isEmpty()) {
            throw new CustomException("Failed to get category");
        }
        URI redirectUri = URI.create("/admin/"+ postDto.getCategory()+"/list/"+alarm_id);

        return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
    }

    //스태프 포스팅 관련
    @GetMapping("/admin/{category}/list") //스태프용 카테고리별 목록 조회,
    public ResponseEntity<List<PostDto>> getList(@PathVariable String category){
        //[요청] department 필드도 토큰에 넣어서 생성하고 get 할 수 있도록 요청
        //부서 토큰에서 get 해와서 부서와 category 를 서비스로 넘겨주기
        List<PostDto> postDto = postStaffService.getList(category, department);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/admin/{category}/list/{post_id}") //스태프용 글 조회,
    public ResponseEntity<PostDto> getPost(@PathVariable Long post_id){
        PostDto postDto = postStaffService.getPost(post_id);
        return ResponseEntity.ok(postDto);
    }

    @PutMapping("/admin/{category}/list/{post_id}") //글 상태 변경
    public ResponseEntity<Void> updatePostStatus(@PathVariable Long post_id,
                                                 @RequestParam String status){
        postStaffService.updatePostStatus(post_id, status);
        return ResponseEntity.noContent().build();
    }

}
