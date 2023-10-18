package mos.mosback.controller;
import mos.mosback.domain.stRoom.StudyMemberTodoEntity;
import mos.mosback.domain.stRoom.ToDoEntity;
import mos.mosback.service.ToDoService;
import mos.mosback.stRoom.dto.MemberToDoRequestDto;
import mos.mosback.stRoom.dto.StudyMemberToDoRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo") //URL 패턴

public class TodoController {
    private final ToDoService toDoService;

    @Autowired
    public TodoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTodo(@RequestBody MemberToDoRequestDto requestDto) {
        ToDoEntity todo = toDoService.addTodo(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("TodoList 추가 완료. index : " +todo.getTodoId());
    }

    @PostMapping("/member/add")
    public ResponseEntity<String> addMemberTodo(@RequestBody StudyMemberToDoRequestDto requestDto) throws Exception {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        requestDto.setCurrentEmail(currentEmail);
        StudyMemberTodoEntity todo = toDoService.addMemberTodo(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("TodoList 추가 완료. index : " +todo.getMemberId());
    }
 //todo개수만큼 프론트에서 호출해줘야함



    @PutMapping("/{todoId}")
    public ResponseEntity<String> updateTodo(@PathVariable Long todoId, @RequestBody MemberToDoRequestDto requestDto) {
        try {
            ToDoEntity updatedToDo = toDoService.updateTodo(todoId, requestDto.getTodoContent());
            return ResponseEntity.ok("ToDo 업데이트 완료. Index: " + todoId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND TODO");
        }
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long todoId) {
        try {
            toDoService.deleteTodo(todoId);
            return ResponseEntity.ok("ToDo 삭제 완료. Index: " + todoId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND TODO");
        }
    }


}