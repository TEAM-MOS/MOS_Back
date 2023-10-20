package mos.mosback.service;
import lombok.RequiredArgsConstructor;
import mos.mosback.domain.stRoom.StRoomEntity;
import mos.mosback.domain.stRoom.StudyMemberTodoEntity;
import mos.mosback.domain.stRoom.ToDoEntity;
import mos.mosback.domain.stRoom.TodoStatus;
import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.service.UserService;
import mos.mosback.repository.MemberTodoRepository;
import mos.mosback.repository.StRoomRepository;
import mos.mosback.repository.ToDoRepository;
import mos.mosback.stRoom.dto.stRoomToDoRequestDto;
import mos.mosback.stRoom.dto.StudyMemberToDoRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;
    private final StRoomRepository stRoomRepository;
    private final MemberTodoRepository studyMemberToDoRepository;
    private final UserService userService;

    @Transactional
    public ToDoEntity addTodo(stRoomToDoRequestDto requestDto, Long roomID) {
        ToDoEntity toDoEntity = new ToDoEntity();
        toDoEntity.setTodoContent(requestDto.getTodoContent());
        toDoEntity.setStatus(TodoStatus.Waiting);
        StRoomEntity stRoomEntity = stRoomRepository.findById(roomID)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id =" + roomID));

        toDoEntity.setStRoom(stRoomEntity);
        return toDoRepository.save(toDoEntity);
    }


    @Transactional
    public ToDoEntity updateTodo(Long todoId, String todoContent,TodoStatus status) {
        ToDoEntity toDoEntity = toDoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        toDoEntity.update(todoContent, status);
        return toDoEntity;
    }

    @Transactional
    public void deleteTodo(Long todoId) {
        ToDoEntity toDoEntity = toDoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        toDoRepository.delete(toDoEntity);
    }
    @Transactional
    public void getTodo(Long todoId){
        ToDoEntity toDoEntity = toDoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        toDoRepository.findById(todoId);

    }

    @Transactional
    public StudyMemberTodoEntity addMemberTodo(StudyMemberToDoRequestDto requestDto) throws Exception {
        StudyMemberTodoEntity toDoEntity = new StudyMemberTodoEntity();
        toDoEntity.setTodoContent(requestDto.getTodoContent());

        StRoomEntity stRoomEntity = stRoomRepository.findById(requestDto.getRoomID())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id =" + requestDto.getRoomID()));

        // 사용자 이메일 조회해서 save 전에 주입
        User user = userService.getUserByEmail(requestDto.getCurrentEmail());
        toDoEntity.setMemberId(user.getId());
        toDoEntity.setStatus(TodoStatus.Waiting);
        toDoEntity.setStRoom(stRoomEntity);
        return studyMemberToDoRepository.save(toDoEntity);
    }
}
