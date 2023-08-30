package mos.mosback.service;
import lombok.RequiredArgsConstructor;
import mos.mosback.domain.posts.ToDoEntity;
import mos.mosback.domain.posts.ToDoRepository;
import mos.mosback.web.dto.ToDoRequestDto;
import mos.mosback.web.dto.ToDoResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ToDoService {
    private final ToDoRepository toDoRepository;

    @Transactional
    public void addToDo(String todoContent, boolean completed){
        ToDoEntity toDoEntity = ToDoEntity.builder()
                .todoContent(todoContent)
                .completed(completed)
                .build();
    }
  //TodoList 추가

    @Transactional(readOnly = true)
    public List<ToDoResponseDto> findAllDesc() {
        return toDoRepository.findAllDesc().stream()
                .map(ToDoResponseDto::new)
                .collect(Collectors.toList());
    }
    //TodoList 조회

    @Transactional
    public void updateToDo(Long TodoId, String todoContent, boolean completed) {
        ToDoEntity toDoEntity = toDoRepository.findById(TodoId)
                .orElseThrow(() -> new IllegalArgumentException(TodoId + " NOT FOUND"));

        toDoEntity.updateToDo(todoContent, completed);
    }
    //수정 기능

    @Transactional
    public  void delete (Long TodoId){
        ToDoEntity toDoEntity = toDoRepository.findById(TodoId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id =" + TodoId));

        toDoRepository.delete(toDoEntity);
        //엔티티를 파라미터로 삭제할 수도 있고, deleteById 메서드를 이용하면 id로 삭제할 수 있음
        //엔티티 조회 후 그대로 삭제
        //삭제 기능
    }

}
