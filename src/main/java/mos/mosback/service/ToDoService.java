package mos.mosback.service;
import lombok.RequiredArgsConstructor;
import mos.mosback.domain.stRoom.ToDoEntity;
import mos.mosback.repository.ToDoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;
    @Transactional
    public ToDoEntity addTodo(int weekOfYear,String dayOfWeek,String TodoContent) {

        LocalDate currentDate = LocalDate.now();
        // 주차 계산
        weekOfYear = currentDate.get(WeekFields.of(Locale.KOREA).weekOfWeekBasedYear());
        // 해당 요일 및 주차의 Todo를 조회
        List<ToDoEntity> existingTodos = toDoRepository.findByDayOfWeekAndWeekOfYear(dayOfWeek, weekOfYear);

         ToDoEntity todo = new ToDoEntity("",false,dayOfWeek,weekOfYear);
         return toDoRepository.save(todo);

    }



    @Transactional
    public ToDoEntity updateTodo(Long todoId, String todoContent, boolean completed,String dayOfWeek, int weekOfYear) {
        ToDoEntity toDoEntity = toDoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        toDoEntity.updateToDo(todoContent, completed,dayOfWeek,weekOfYear);
        return toDoEntity;
    }

    @Transactional
    public void deleteTodo(Long todoId) {
        ToDoEntity toDoEntity = toDoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        toDoRepository.delete(toDoEntity);
    }


    public List<ToDoEntity> findByWeekOfYearAndDayOfWeek(String weekOfYear, int dayOfWeek) {
        return toDoRepository.findByDayOfWeekAndWeekOfYear(weekOfYear, dayOfWeek);
    }
}