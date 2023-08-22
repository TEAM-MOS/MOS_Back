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
@RequestMapping("/api/posts") //URL 패턴
public class PostsController {

    private final PostService postService; // PostService를 주입.

    @Autowired
    public PostsController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostSaveRequestDto requestDto) {
        Long postId = postService.save(requestDto);
        return new ResponseEntity<>(postId, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostsResponseDto> getPost(@PathVariable Long id) {
        PostsResponseDto post = postService.findById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostsListResponseDto>> searchPosts(@RequestParam String keyword) {
        List<PostsListResponseDto> posts = postService.findByTitleContaining(keyword);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePost(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        Long updatedId = postService.update(id, requestDto);
        return new ResponseEntity<>(updatedId, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostsListResponseDto>> getAllPosts() {
        List<PostsListResponseDto> posts = postService.findAllDesc();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PostsListResponseDto>> getPopularPosts() {
        List<PostsListResponseDto> popularPosts = postService.findPopularPosts();
        return new ResponseEntity<>(popularPosts, HttpStatus.OK);
    }
}
//핸들러 메서드 :
//
//createPost: 새 게시물을 생성하는 엔드포인트.
//getPost: 특정 ID에 해당하는 게시물을 조회하는 엔드포인트.
//searchPosts: 제목에 키워드가 포함된 게시물을 검색하는 엔드포인트.
//updatePost: 게시물을 업데이트하는 엔드포인트.
//deletePost: 게시물을 삭제하는 엔드포인트.
//getAllPosts: 모든 게시물을 조회하는 엔드포인트.
//getPopularPosts: 조회순으로 게시물을 조회하는 엔드포인트. (인기순 조회시)
