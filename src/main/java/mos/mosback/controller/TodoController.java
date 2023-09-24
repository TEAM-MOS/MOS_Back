package mos.mosback.controller;
import mos.mosback.domain.stRoom.ToDoEntity;
import mos.mosback.service.ToDoService;
import mos.mosback.stRoom.dto.ToDoDateDto;
import mos.mosback.stRoom.dto.ToDoRequestDto;
import mos.mosback.stRoom.dto.ToDoContentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/todo") //URL 패턴

public class TodoController {
    private final ToDoService toDoService;

    @Autowired
    public TodoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTodo(@RequestBody ToDoRequestDto requestDto) {

        ToDoEntity todo = toDoService.addTodo(
                requestDto.getDayOfWeek(), requestDto.getTodoContent());

        return ResponseEntity.status(HttpStatus.CREATED).body("TodoList 추가 완료. index : " +todo.getTodoId());
    }

    @GetMapping("/{year}/{month}/{weekOfYear}/{dayOfWeek}")
    public ResponseEntity<List<ToDoContentResponseDto>> FindByDate(@PathVariable int year, @PathVariable int month, @PathVariable int weekOfYear, @PathVariable String dayOfWeek) {

        List<ToDoContentResponseDto> todos = toDoService.findByDate(year, month, weekOfYear, dayOfWeek);
            return new ResponseEntity<>(todos, HttpStatus.OK);

    }
    @GetMapping("/date")
    public ResponseEntity<List<ToDoDateDto>> getTodoDate() {
        List<ToDoDateDto> todoDates = toDoService.getTodoDate();

        if (!todoDates.isEmpty()) {
            return new ResponseEntity<>(todoDates, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/update/{TodoId}")
    public ResponseEntity<String> updateTodo(@PathVariable Long TodoId, @RequestBody ToDoRequestDto requestDto) {
        try {
            ToDoEntity updatedToDo = toDoService.updateTodo(TodoId, requestDto.getTodoContent(),
                    requestDto.isCompleted(),requestDto.getDayOfWeek());
            return ResponseEntity.ok("ToDo 업데이트 완료. Index: " + TodoId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND TODO");
        }
    }

    @DeleteMapping("/delete/{TodoId}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long TodoId) {
        try {
            toDoService.deleteTodo(TodoId);
            return ResponseEntity.ok("ToDo 삭제 완료. Index: " + TodoId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND TODO");
        }
    }


}