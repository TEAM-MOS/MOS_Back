package mos.mosback.stRoom.controller;
import lombok.RequiredArgsConstructor;
import mos.mosback.stRoom.domain.stRoom.StudyMemberTodoEntity;
import mos.mosback.stRoom.domain.stRoom.ToDoEntity;
import mos.mosback.stRoom.dto.*;
import mos.mosback.stRoom.service.ToDoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
public class TodoController {

    private final ToDoService toDoService;

    @PostMapping("todo/add/{roomId}")
    public ResponseEntity<Map<String, Object>> addTodo(@RequestBody stRoomToDoRequestDto requestDto, @PathVariable Long roomId) {
        try{
            ToDoEntity todo = toDoService.addTodo(requestDto, roomId);
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.OK.value());
            response.put("success",true);
            response.put("index",todo.getTodoId());
            response.put("message", "todo추가완료");
            return ResponseEntity.ok(response);
        }catch(IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("success",false);
            response.put("message","서버내부오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
    @PostMapping("/member/todo/add")
    public ResponseEntity<Map<String, Object>> addMemberTodo(@RequestBody StudyMemberToDoRequestDto requestDto) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        requestDto.setCurrentEmail(currentEmail);
        StudyMemberTodoEntity todo = toDoService.addMemberTodo(requestDto);
        Map<String, Object> response = new HashMap<>();
        response.put("status:", HttpStatus.OK.value());
        response.put("success", true);
        response.put("index", todo.getIdx());
        response.put("message", "todo추가완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PutMapping("todo/{todoId}")
    public ResponseEntity<Map<String, Object>> updateTodo(@PathVariable Long todoId, @RequestBody stRoomToDoRequestDto requestDto) {
        try {
            ToDoEntity updatedToDo = toDoService.updateTodo(todoId, requestDto.getTodoContent(),requestDto.getStatus());
            Map<String, Object> response = new HashMap<>();
            if(updatedToDo != null) {
                response.put("status:", HttpStatus.OK.value());
                response.put("success", true);
                response.put("message", "Todo수정완료");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }else {
                response.put("status:", HttpStatus.NOT_FOUND.value());
                response.put("success", false);
                response.put("message", "해당Todo없음");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("success",false);
            response.put("message","서버내부오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/member/todo/{todoIdx}")
    public ResponseEntity<Map<String, Object>> updateMemberTodo(@PathVariable Long todoIdx,
                                                   @RequestBody StudyMemberToDoRequestDto requestDto) throws Exception {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        requestDto.setCurrentEmail(currentEmail);
        try {
            StudyMemberTodoEntity updatedToDo = toDoService.updateMemberTodo(todoIdx, requestDto.getTodoContent(),
                    requestDto.getStatus(), currentEmail);
            Map<String, Object> response = new HashMap<>();
            if(updatedToDo != null) {
                response.put("status:", HttpStatus.OK.value());
                response.put("success", true);
                response.put("message", "Todo수정완료");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }else {
                response.put("status:", HttpStatus.NOT_FOUND.value());
                response.put("success", false);
                response.put("message", "해당Todo없음");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("success",false);
            response.put("message","서버내부오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("todo/{todoId}")
    public ResponseEntity<Map<String, Object>> deleteTodo(@PathVariable Long todoId) {
        try {
            toDoService.deleteTodo(todoId);
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.OK.value());
            response.put("success", true);
            response.put("message", "Todo삭제완료");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("success",false);
            response.put("message","서버내부오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/member/todo/{todoIdx}")
    public ResponseEntity<Map<String, Object>> deleteMemberTodo(@PathVariable Long todoIdx) throws Exception{
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        try {
            toDoService.deleteMemberTodo(todoIdx, currentEmail);
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.OK.value());
            response.put("success", true);
            response.put("message", "Todo삭제완료");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status:", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("success",false);
            response.put("message","서버내부오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @GetMapping("todo/{roomId}")
    public ResponseEntity<Map<String, Object>> findStRoomTodoByRoomId(@PathVariable Long roomId) {
        List<StRoomToDoResponseDto> todo = toDoService.findStRoomTodoByRoomId(roomId);
        Map<String, Object> response = new HashMap<>();

        if (!todo.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("success", true);
            response.put("todo", todo);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("success", false);
            response.put("message", "Todo를 찾을 수 없음");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
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
    public ResponseEntity<Map<String, Object>> getMemberTodoRank(@PathVariable Long roomId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // 현재 사용자의 이메일
        List<MemberTodoRankResponseDto> todoList = toDoService.getMemberTodoProgress(roomId, currentEmail);
        MemberTodoProgressResponseDto todoProgress = toDoService.getProgressInfo(roomId,currentEmail);
        Map<String, Object> response = new HashMap<>();
        response.put("data", todoProgress);
        response.put("TodoRank", todoList);
        return ResponseEntity.status(HttpStatus.OK).body(response);


    }
    @GetMapping("/myTodo/{roomId}/{date}")
    public ResponseEntity<Map<String, Object>> getTodoByDateAndMemberIdAndRoomId
            (@PathVariable Long roomId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // 현재 사용자의 이메일
        List<MemberTodoResponseDto> todoList = toDoService.getTodoByDateAndMemberIdAndRoomId(date, roomId,email);

        Map<String, Object> response = new HashMap<>();
        response.put("todoList", todoList);

        return ResponseEntity.ok(response);
    }
}

