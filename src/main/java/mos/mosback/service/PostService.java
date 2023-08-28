package mos.mosback.service;
import lombok.RequiredArgsConstructor;
import mos.mosback.domain.posts.Posts;
import mos.mosback.domain.posts.PostsRepository;
import mos.mosback.web.dto.PostSaveRequestDto;
import mos.mosback.web.dto.PostsListResponseDto;
import mos.mosback.web.dto.PostsResponseDto;
import mos.mosback.web.dto.PostsUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PostService {
    private final PostsRepository postsRepository;

    @Transactional
    public  Long save(PostSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }



    @Transactional
    public void update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " NOT FOUND"));

        posts.update(requestDto.getTitle(), requestDto.getGoal(), requestDto.getRules(),
                requestDto.getQuest(), requestDto.getTend(), requestDto.getCategory(),
                requestDto.getDate(), requestDto.getIntro(), requestDto.getNum(),
                requestDto.getMod(), requestDto.isOnOff(), requestDto.getStartDate(),
                requestDto.getEndDate(), requestDto.getRcstart(), requestDto.getRcend());
    }  //postsRepository를 사용하여 데이터베이스에서 주어진 id에 해당하는 게시물을 찾기


    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new) //.map(post->new PostResponse(posts))랑 같음
                // postsRepository 결과로 넘어온 Posts의 Stream을
                // map을 통해 PostsListResponseDto변환 ->List로 반환하는 메소드

                .collect(Collectors.toList());
    }

    @Transactional
    public  void delete (Long id){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id =" + id));

        postsRepository.delete(posts); //JpaRepository 에서 이미 delete 메소드를 지원하고 있으므로 활용
        //엔티티를 파라미터로 살제할 수도 있고, deleteById 메서드를 이용하면 id로 삭제할 수 있음
        // 존재하는 Posts인지 확인을 위해 엔티티 조회 후 그대로 삭제함
        // --> 서비스에서 delete 메서드를 만들면 컨트롤러가 사용하도록 컨트롤러에 코드 추가하기.
    }
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findByTitleContaining(String keyword) {
        List<Posts> posts = postsRepository.findByTitleContaining(keyword);
        if (posts.isEmpty()) {
            throw new IllegalArgumentException("해당 스터디가 없습니다.");
        }
        return posts.stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    } //스터디 title 로 검색하기


    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findPopularPosts() {
        // 클릭된 조회수 순으로 게시물을 조회하는 비즈니스 로직을 호출
        List<Posts> popularPosts = postsRepository.findPopularPosts();
        return popularPosts.stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
}
