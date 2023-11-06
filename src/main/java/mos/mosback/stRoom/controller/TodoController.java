package mos.mosback.stRoom.controller;
import mos.mosback.stRoom.domain.stRoom.StudyMemberTodoEntity;
import mos.mosback.stRoom.domain.stRoom.ToDoEntity;
import mos.mosback.stRoom.dto.*;
import mos.mosback.stRoom.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController

public class TodoController {
    private final ToDoService toDoService;

    @Autowired
    public TodoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @PostMapping("todo/add/{roomId}")
    public ResponseEntity<String> addTodo(@RequestBody stRoomToDoRequestDto requestDto, @PathVariable Long roomId) {
        try{
            ToDoEntity todo = toDoService.addTodo(requestDto, roomId);
            return ResponseEntity.status(HttpStatus.CREATED).body
                    ("TodoList 추가 완료." +
                            "\ntodoIndex : " + todo.getTodoId() +
                            "\nstatus:201" +
                            "\nsuccess:true");
        }catch(IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body
                    ("TodoList 추가 실패" +
                    "\nstatus:404" +
                    "\nsuccess:false");
        }

    }
    @PostMapping("/member/todo/add")
    public ResponseEntity<String> addMemberTodo(@RequestBody StudyMemberToDoRequestDto requestDto) throws Exception{
        try { // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        requestDto.setCurrentEmail(currentEmail);
        StudyMemberTodoEntity todo = toDoService.addMemberTodo(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("TodoList 추가 완료. index : " +todo.getIdx());
    }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ServerError"+
                            "\nstatus:500" +
                            "\nsuccess:false");
        }
    }
 //todo개수만큼 프론트에서 호출해줘야함
//


    @PutMapping("todo/{todoId}")
    public ResponseEntity<String> updateTodo(@PathVariable Long todoId, @RequestBody stRoomToDoRequestDto requestDto) {
        try {
            ToDoEntity updatedToDo = toDoService.updateTodo(todoId, requestDto.getTodoContent(),requestDto.getStatus());
            return ResponseEntity.ok
                    ("ToDo 업데이트 완료. \nIndex: " + todoId+
                    "\nstatus:200" +
                    "\nsuccess:true");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND TODO"+
                            "\nstatus:401" +
                            "\nsuccess:false");
        }
    }

    @PutMapping("/member/todo/{todoIdx}")
    public ResponseEntity<String> updateMemberTodo(@PathVariable Long todoIdx,
                                                   @RequestBody StudyMemberToDoRequestDto requestDto) throws Exception {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        requestDto.setCurrentEmail(currentEmail);
        try {
            StudyMemberTodoEntity updatedToDo = toDoService.updateMemberTodo(todoIdx, requestDto.getTodoContent(),
                    requestDto.getStatus(), currentEmail);
            return ResponseEntity.ok
                    ("Study Member ToDo 업데이트 완료. \nIndex: " + todoIdx +
                            "\nstatus:200" +
                            "\nsuccess:true");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND TODO"+
                            "\nstatus:401" +
                            "\nsuccess:false");
        }
    }

    @DeleteMapping("todo/{todoId}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long todoId) {
        try {
            toDoService.deleteTodo(todoId);
            return ResponseEntity.ok
                    ("ToDo 삭제 완료. Index: " + todoId+
                    "\nstatus:200" +
                    "\nsuccess:true");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND TODO"+
                            "\nstatus:401" +
                            "\nsuccess:false");
        }
    }

    @DeleteMapping("/member/todo/{todoIdx}")
    public ResponseEntity<String> deleteMemberTodo(@PathVariable Long todoIdx) throws Exception{
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        try {
            toDoService.deleteMemberTodo(todoIdx, currentEmail);
            return ResponseEntity.ok
                    ("Study Member ToDo 삭제 완료. Index: " + todoIdx+
                            "\nstatus:200" +
                            "\nsuccess:true");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("NOT FOUND TODO"+
                            "\nstatus:401" +
                            "\nsuccess:false");
        }
    }

    @GetMapping("todo/{roomId}")
    public ResponseEntity<List<StRoomToDoResponseDto>> findStRoomTodoByRoomId(@PathVariable Long roomId) {
        List<StRoomToDoResponseDto> todo = toDoService.findStRoomTodoByRoomId(roomId);
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }


    @GetMapping("/member/room/{roomId}")
    public ResponseEntity<StudyMemberRoomInfoResponseDto> getMemberRoomInfo(@PathVariable Long roomId) throws Exception {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        StudyMemberRoomInfoResponseDto todo = toDoService.getMemberRoomInfo(roomId, currentEmail);
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @GetMapping("/todoRank/{roomId}")
    public ResponseEntity<List<MemberTodoRankResponseDto>> getMemberTodoRank(@PathVariable Long roomId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일


        List<MemberTodoRankResponseDto> todoList = toDoService.getMemberTodoProgress(roomId, currentEmail);
        return new ResponseEntity<>(todoList, HttpStatus.OK);
    }


}