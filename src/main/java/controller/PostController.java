package controller;

import model.Alarm;
import model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AlarmServiceImpl;
import service.PostStaffServiceImpl;

import java.net.URI;
import java.util.List;

@RestController
public class PostController {
    private PostStaffServiceImpl postStaffService;
    private AlarmServiceImpl alarmService;

    @Autowired
    PostController(PostStaffServiceImpl postStaffService, AlarmServiceImpl alarmService){
        this.postStaffService = postStaffService;
        this.alarmService = alarmService;
    }

    @GetMapping("/alarms") //알람 리스트 조회
    public ResponseEntity<List<Alarm>> getAlarms(){
        List<Alarm> alarms = alarmService.getAlarms();
        return ResponseEntity.ok(alarms);
    }

    @GetMapping("/alarms/{alarm_id}") //특정 알람에 대한 리다이렉션 처리 **관리자 권한 봐야함
    public ResponseEntity<Void> handleAlarm(@PathVariable Long alarm_id){
        URI redirectUri = URI.create("/list/"+alarm_id);

        return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
    }

    @GetMapping("/{category}/list") //스태프용 카테고리별 목록 조회, **근데 관리자 권한은 어떻게 줄건지
    public ResponseEntity<List<Post>> getList(@PathVariable String category,
                                              @RequestParam String department){
        List<Post> posts = postStaffService.getList(category, department);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/list/{post_id}") //스태프용 글 조회, **관리자 권한 어떻게 줄건지 **카테고리 uri 에 따로 없어도 괜찮을지
    public ResponseEntity<Void> getPost(@PathVariable Long post_id){
        Post post = postStaffService.getPost(post_id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/list/{post_id}/status") //글 상태 변경
    public ResponseEntity<Void> updatePostStatus(@PathVariable Long post_id,
                                                 @RequestParam String status){
        postStaffService.updatePostStatus(post_id, status);

        return ResponseEntity.noContent().build();
    }

}
