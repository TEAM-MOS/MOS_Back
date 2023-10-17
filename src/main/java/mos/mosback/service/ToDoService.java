package mos.mosback.service;
import lombok.RequiredArgsConstructor;
import mos.mosback.domain.stRoom.ToDoEntity;
import mos.mosback.repository.ToDoRepository;
import mos.mosback.stRoom.dto.ToDoContentResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;

    @Transactional
    public ToDoEntity addTodo(String todoContent) {
        ToDoEntity toDoEntity = new ToDoEntity();
        return toDoRepository.save(toDoEntity);
    }


    @Transactional
    public ToDoEntity updateTodo(Long todoId, String todoContent) {
        ToDoEntity toDoEntity = toDoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        toDoEntity.update(todoContent);
        return toDoEntity;
    }

    @Transactional
    public void deleteTodo(Long todoId) {
        ToDoEntity toDoEntity = toDoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        toDoRepository.delete(toDoEntity);
    }
}
