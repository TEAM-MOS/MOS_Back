package mos.mosback.service;
import lombok.RequiredArgsConstructor;
import mos.mosback.domain.posts.ToDoEntity;
import mos.mosback.repository.ToDoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@RequiredArgsConstructor
@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;
    @Transactional
    public ToDoEntity add(String todoContent) {
        ToDoEntity toDoEntity = new ToDoEntity(todoContent, false);
        return toDoRepository.save(toDoEntity);
    }


    @Transactional
    public ToDoEntity update(Long todoId, String todoContent, boolean completed) {
        ToDoEntity toDoEntity = toDoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        toDoEntity.updateToDo(todoContent, completed);
        return toDoEntity;
    }

    @Transactional
    public void delete(Long todoId) {
        ToDoEntity toDoEntity = toDoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        toDoRepository.delete(toDoEntity);
    }


}