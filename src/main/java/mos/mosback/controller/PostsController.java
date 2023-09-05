package mos.mosback.controller;
import mos.mosback.web.dto.PostSaveRequestDto;
import mos.mosback.web.dto.PostsListResponseDto;
import mos.mosback.web.dto.PostsResponseDto;
import mos.mosback.web.dto.PostsUpdateRequestDto;
import mos.mosback.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studyGroup") //URL 패턴
public class PostsController {

    private final PostService postService; // PostService를 주입.

    @Autowired
    public PostsController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> savePost(@RequestBody PostSaveRequestDto requestDto) {
        Long postId = postService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("created successfully. ID: " + postId);
    }

    @GetMapping("get/{groupID}")
    public ResponseEntity<PostsResponseDto> FindByID (@PathVariable Long groupID) {
        PostsResponseDto post = postService.findById(groupID);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostsListResponseDto>> searchPosts(@RequestParam String keyword) {
        List<PostsListResponseDto> posts = postService.findByTitleContaining(keyword);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PutMapping("update/{groupID}")
    public ResponseEntity<String> updatePost(@PathVariable Long groupID, @RequestBody PostsUpdateRequestDto requestDto) {
        try {
            postService.update(groupID, requestDto);
            return ResponseEntity.ok("updated.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND"+"'"+groupID+"'");

        }
    }

    @DeleteMapping("delete/{groupID}")
    public ResponseEntity<Void> deletePost(@PathVariable Long groupID) {
        postService.delete(groupID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("get/all")
    public ResponseEntity<List<PostsListResponseDto>> getAllPosts() {
        List<PostsListResponseDto> posts = postService.findAllDesc();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("get/popular")
    public ResponseEntity<List<PostsListResponseDto>> getPopularPosts() {
        List<PostsListResponseDto> popularPosts = postService.findPopularPosts();
        return new ResponseEntity<>(popularPosts, HttpStatus.OK);
    }

   @GetMapping("/get/recruitment")
    public ResponseEntity<List<PostsListResponseDto>> getRecruitment(){
        List<PostsListResponseDto> RecruitmentPeriod = postService.findGroupRecruitmentPeriod();
        return new ResponseEntity<>(RecruitmentPeriod, HttpStatus.OK);
   }

}
//핸들러 메서드 :
//
//create: 새 게시물을 생성하는 엔드포인트.
//getPost: 특정 groupID에 해당하는 게시물을 조회하는 엔드포인트.( 스터디그룹 조회 )
//searchPosts: 키워드가 포함된 게시물을 검색하는 엔드포인트.
//updatePost: 게시물을 업데이트하는 엔드포인트.
//deletePost: 게시물을 삭제하는 엔드포인트.
//getAllPosts: 모든 게시물을 조회하는 엔드포인트.
//getPopularPosts: 조회순으로 게시물을 조회하는 엔드포인트. (인기순 조회시)
//getRecruitment : 모집기간인 게시물을 조회하는 엔드포인트.
