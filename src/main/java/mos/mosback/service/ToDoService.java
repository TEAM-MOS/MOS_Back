package mos.mosback.service;
import lombok.RequiredArgsConstructor;
import mos.mosback.domain.stRoom.ToDoEntity;
import mos.mosback.repository.ToDoRepository;
import mos.mosback.stRoom.dto.ToDoDateDto;
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
    public ToDoEntity addTodo(String dayOfWeek, String todoContent) {
        LocalDate currentDate = LocalDate.now();

        // 주차 계산
        int weekOfMonth = getCurrentWeekOfMonth(currentDate);

        // 해당 요일 및 주차의 Todo를 조회
        List<ToDoEntity> existingTodos = toDoRepository.findByDayOfWeekAndWeekOfYear(dayOfWeek, weekOfMonth);


        ToDoEntity todo = new ToDoEntity(todoContent, false, dayOfWeek, weekOfMonth, currentDate.getYear(),currentDate.getMonthValue());
        return toDoRepository.save(todo);
    }

    private int getCurrentWeekOfMonth(LocalDate currentDate) {
        // 한 주의 시작은 월요일이고, 첫 주에 4일 이상이 포함되어야 첫 주 취급 (목/금/토/일)
        DayOfWeek firstDayOfWeek = currentDate.withDayOfMonth(1).getDayOfWeek();
        int daysToAdd = DayOfWeek.MONDAY.getValue() - firstDayOfWeek.getValue();

        if (daysToAdd < 0) {
            daysToAdd += 7; // If it's before Monday, move to the next Monday
        }

        LocalDate firstMondayOfMonth = currentDate.withDayOfMonth(1).plusDays(daysToAdd);

        int weekOfMonth = (currentDate.getDayOfMonth() - 1) / 7 + 1;

        // 마지막 주차의 경우
        if (weekOfMonth == 5) {
            DayOfWeek lastDayOfWeek = currentDate.withDayOfMonth(currentDate.lengthOfMonth()).getDayOfWeek();

            // 마지막 날이 월~수 사이이면 다음달 1주차로 계산
            if (lastDayOfWeek.getValue() <= DayOfWeek.WEDNESDAY.getValue()) {
                LocalDate firstDayOfNextMonth = currentDate.withDayOfMonth(1).plusMonths(1);
                DayOfWeek firstDayOfNextMonthOfWeek = firstDayOfNextMonth.getDayOfWeek();

                int daysToAddToNextMonth = DayOfWeek.MONDAY.getValue() - firstDayOfNextMonthOfWeek.getValue();

                if (daysToAddToNextMonth < 0) {
                    daysToAddToNextMonth += 7;
                }

                LocalDate firstMondayOfNextMonth = firstDayOfNextMonth.plusDays(daysToAddToNextMonth);

                weekOfMonth = 1; // 다음달의 첫 주로 설정
            }
        }

        return weekOfMonth;
    }
//  @Transactional
//    public ToDoResponseDto findById(Long todoId) {
//        Optional<ToDoEntity> optionalToDoEntity = toDoRepository.findById(todoId);
//
//        // ToDoEntity를 ToDoResponseDto로 변환하여 반환
//        return optionalToDoEntity.map(ToDoResponseDto::new).orElse(null); // 또는 예외 처리를 수행
//    }

    @Transactional
    public void deleteTodo(Long todoId) {
        ToDoEntity toDoEntity = toDoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        toDoRepository.delete(toDoEntity);
    }

    @Transactional
    public ToDoEntity updateTodo(Long todoId, String todoContent, boolean completed,String dayOfWeek) {
        ToDoEntity toDoEntity = toDoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        toDoEntity.updateToDo(todoContent, completed,dayOfWeek);
        return toDoEntity;
    }
    public List<ToDoEntity> findByWeekOfYearAndDayOfWeek(String weekOfYear, int dayOfWeek) {
        return toDoRepository.findByDayOfWeekAndWeekOfYear(weekOfYear, dayOfWeek);
    }

    public List<ToDoContentResponseDto> findByDate(int year, int month, int weekOfYear, String dayOfWeek) {
        return toDoRepository.findByDate(year, month, weekOfYear, dayOfWeek);
    }

    public List<ToDoDateDto> getTodoDate(){
        return toDoRepository.getTodoDate();
    }
}