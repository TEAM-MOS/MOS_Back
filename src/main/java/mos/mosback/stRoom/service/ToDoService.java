package mos.mosback.stRoom.service;
import lombok.RequiredArgsConstructor;
import mos.mosback.login.domain.user.User;
import mos.mosback.login.domain.user.repository.UserRepository;
import mos.mosback.login.domain.user.service.UserService;
import mos.mosback.stRoom.domain.stRoom.*;
import mos.mosback.stRoom.dto.*;
import mos.mosback.stRoom.repository.MemberTodoRepository;
import mos.mosback.stRoom.repository.StRoomRepository;
import mos.mosback.stRoom.repository.ToDoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;
    private final StRoomRepository stRoomRepository;
    private final MemberTodoRepository studyMemberToDoRepository;
    private final UserService userService;
    private final StRoomService stRoomService;
    private final UserRepository userRepository;


    @Transactional
    public ToDoEntity addTodo(stRoomToDoRequestDto requestDto, Long roomId) {
        ToDoEntity toDoEntity = new ToDoEntity();
        toDoEntity.setTodoContent(requestDto.getTodoContent());
        toDoEntity.setStatus(TodoStatus.Waiting);
        StRoomEntity stRoomEntity = stRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id =" + roomId));

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
    public StudyMemberTodoEntity updateMemberTodo(Long todoIdx, String todoContent, TodoStatus status, String currentEmail) throws Exception {
        User user = userService.getUserByEmail(currentEmail);
        StudyMemberTodoEntity studyMemberTodoEntity = studyMemberToDoRepository.findById(todoIdx)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        studyMemberTodoEntity.update(todoContent, status);
        return studyMemberTodoEntity;
    }

    @Transactional
    public void deleteTodo(Long todoId) {
        ToDoEntity toDoEntity = toDoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        toDoRepository.delete(toDoEntity);
    }

    @Transactional
    public void deleteMemberTodo(Long todoIdx, String currentEmail) throws Exception {
        User user = userService.getUserByEmail(currentEmail);
        StudyMemberTodoEntity studyMemberTodoEntity = studyMemberToDoRepository.findById(todoIdx)
                .orElseThrow(() -> new IllegalArgumentException("해당 ToDo를 찾을 수 없습니다."));

        studyMemberToDoRepository.delete(studyMemberTodoEntity);
    }

    @Transactional
    public List<StRoomToDoResponseDto> findStRoomTodoByRoomId(Long roomId) {
        return toDoRepository.findByStRoomId(roomId);
    }

    public StudyMemberTodoEntity addMemberTodo(StudyMemberToDoRequestDto requestDto) throws Exception {
        StudyMemberTodoEntity toDoEntity = new StudyMemberTodoEntity();
        StRoomEntity stRoomEntity = stRoomRepository.findById(requestDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id =" + requestDto.getRoomId()));
        // 사용자 이메일 조회해서 save 전에 주입
        User user = userService.getUserByEmail(requestDto.getCurrentEmail());
        toDoEntity.setMemberId(user.getId());
        toDoEntity.setStatus(TodoStatus.Waiting);
        toDoEntity.setStRoom(stRoomEntity);
        toDoEntity.setTodoContent(requestDto.getTodoContent());

        return studyMemberToDoRepository.save(toDoEntity);
    }

    public StudyMemberRoomInfoResponseDto getMemberRoomInfo(Long roomId, String currentEmail) throws Exception {
        StudyMemberRoomInfoResponseDto result = new StudyMemberRoomInfoResponseDto();
        StRoomEntity stRoomEntity = stRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id =" + roomId));

        User user = userService.getUserByEmail(currentEmail);

        List<StudyMemberTodoEntity> todoEntityList = studyMemberToDoRepository.findAllByStRoom(roomId, user.getId());
        List<StRoomMemberToDoResponseDto> todoList = new ArrayList<>();
        for (StudyMemberTodoEntity item : todoEntityList) {
            todoList.add(new StRoomMemberToDoResponseDto(item));
        }
        // 스터디 룸에 대한 현재 투두평균값

        StudyRoomTodoInfoDto studyRoomTodoAverage = null;
        double average = 0;
        try {
            studyRoomTodoAverage = studyMemberToDoRepository.getStudyRoomTodoAverage(roomId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (studyRoomTodoAverage != null) {
            // DB에 데이터가 존재할 때에만 해당 로직 수행
            average = (studyRoomTodoAverage.getTotalCount() - studyRoomTodoAverage.getCompletedCount()
                    / (double) studyRoomTodoAverage.getTotalCount()) * 100;
        }
        List<StudyRoomDayDto> roomDayList = new ArrayList<>();
        Date now;
        Calendar cal1 = Calendar.getInstance();

        for (int i=0; i<7; i++) {
            StudyRoomDayDto dayDto = new StudyRoomDayDto();
            now = new Date(cal1.getTimeInMillis());
            dayDto.setDate(now); // 날짜 객체 셋팅
            dayDto.setDayVal(cal1.get(Calendar.DATE)); // 날짜 셋팅
            dayDto.setDayOfWeek(getDayOfKoreanWeek(cal1.get(Calendar.DAY_OF_WEEK))); // "월"~"일" 셋팅
            roomDayList.add(dayDto);

            cal1.add(Calendar.DATE, 1); // 일 계산 하루씩 추가
        }

        result.setTodoList(todoList);
        result.setStudyRoomTodoAverage(average); // 평균값
        result.setRoomDayList(roomDayList);

        return result;
    }

    private String getDayOfKoreanWeek(int i) {
        if (i == 1) {
            return "월";
        } else if (i == 2) {
            return "화";
        } else if (i == 3) {
            return "수";
        } else if (i == 4) {
            return "목";
        } else if (i == 5) {
            return "금";
        } else if (i == 6) {
            return "토";
        } else if (i == 7) {
            return "일";
        } else {
            return "";
        }
    }

    public List<MemberTodoRankResponseDto> getMemberTodoProgress(Long roomId,String currentEmail) throws Exception {
        List<MemberTodoRankResponseDto> progressList = new ArrayList<>();
        List<MemberTodoRankProjection> progressProjections = studyMemberToDoRepository.getRankByStRoom(roomId);
        List<StRoomMemberResponseDto> memberList = stRoomService.getStudyRoomMemberList(roomId);

        for (MemberTodoRankProjection progressProjection : progressProjections) {
            User user = stRoomService.getUserInfo(progressProjection.getMemberId());

            MemberTodoRankResponseDto progress = new MemberTodoRankResponseDto();
            progress.setProgress(progressProjection.getProgress());
            progress.setNickname(user.getNickname());
            progressList.add(progress);
        }

        return progressList;
    }

    public MemberTodoProgressResponseDto getProgressInfo(Long roomId, String currentEmail) throws Exception {
        MemberTodoProgressResponseDto todoProgress = new MemberTodoProgressResponseDto();
        User user = userService.getUserByEmail(currentEmail);
        todoProgress.setUserNick(user.getNickname());

        StudyRoomTodoInfoDto studyRoomTodoAverage = null;
        try {
            studyRoomTodoAverage = studyMemberToDoRepository.getStudyRoomTodoAverage(roomId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (studyRoomTodoAverage != null) {
            // DB에 데이터가 존재할 때에만 해당 로직 수행
            double totalCount = studyRoomTodoAverage.getTotalCount();
            double completedCount = studyRoomTodoAverage.getCompletedCount();
            double average = (totalCount - completedCount) / totalCount * 100;
            todoProgress.setAvg(average);
        } else {
            todoProgress.setAvg(0.0);
        }

        return todoProgress;
    }
}
