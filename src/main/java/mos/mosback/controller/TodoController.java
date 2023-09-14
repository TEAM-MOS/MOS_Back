package mos.mosback.controller;
import mos.mosback.domain.posts.ToDoEntity;
import mos.mosback.service.ToDoService;
import mos.mosback.web.dto.ToDoRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todo") //URL 패턴
public class TodoController {
    private final ToDoService toDoService;

    @Autowired
    public TodoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTodo(@RequestBody ToDoRequestDto requestDto){
        ToDoEntity toDoEntity = toDoService.add(requestDto.getTodoContent());
        return ResponseEntity.status(HttpStatus.CREATED).body("TodoList 추가 완료. index : " +toDoEntity.getTodoIdx());
    }
    @PutMapping("/update/{TodoIdx}")
    public ResponseEntity<String> updateTodo(@PathVariable Long TodoIdx, @RequestBody ToDoRequestDto requestDto) {
        try {
            ToDoEntity updatedToDo = toDoService.update(TodoIdx, requestDto.getTodoContent(), requestDto.isCompleted());
            return ResponseEntity.ok("ToDo 업데이트 완료. Index: " + TodoIdx);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND TODO");
        }
    }

    @DeleteMapping("/delete/{TodoIdx}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long TodoIdx) {
        try {
            toDoService.delete(TodoIdx);
            return ResponseEntity.ok("ToDo 삭제 완료. Index: " + TodoIdx);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND TODO");
        }
    }


}