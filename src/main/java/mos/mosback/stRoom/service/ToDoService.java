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

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
        toDoEntity.setDate(requestDto.getDate());
        return studyMemberToDoRepository.save(toDoEntity);
    }

    public StudyMemberRoomInfoResponseDto getMemberRoomInfo(Long roomId, String currentEmail) throws Exception {
        StudyMemberRoomInfoResponseDto result = new StudyMemberRoomInfoResponseDto();

        StRoomEntity stRoom = stRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id =" + roomId));

        User user = userService.getUserByEmail(currentEmail);

        List<StudyMemberTodoEntity> todoEntityList = studyMemberToDoRepository.findAllByStRoom(roomId, user.getId());
        List<StRoomMemberToDoResponseDto> todoList = new ArrayList<>();
        for (StudyMemberTodoEntity item : todoEntityList) {
            todoList.add(new StRoomMemberToDoResponseDto(item));
        }
        List<StRoomTodoInfoProjection> studyRoomTodoAverage = null;
        double average = 0;
        try {
            studyRoomTodoAverage = studyMemberToDoRepository.getStudyRoomTodoAverage(roomId); // studyRoomTodoAverage를 초기화
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (studyRoomTodoAverage != null && !studyRoomTodoAverage.isEmpty()) {
            StRoomTodoInfoProjection projection = studyRoomTodoAverage.get(0);
            // DB에 데이터가 존재할 때에만 해당 로직 수행
            int totalCount = projection.getTotalCount();
            int completedCount = projection.getCompletedCount();
            if (totalCount > 0) {
                average = ((double) completedCount / totalCount) * 100;
            }
        }
        List<StudyRoomDayDto> roomDayList = new ArrayList<>();

        Date now = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(now);
        LocalDate date = LocalDate.now();

        for (int i=0; i<7; i++) {
            StudyRoomDayDto dayDto = new StudyRoomDayDto();

            dayDto.setYear(date.getYear()); // 날짜 객체 셋팅
            dayDto.setMonth(date.getMonthValue());
            dayDto.setDayVal(cal1.get(Calendar.DATE)); // 날짜 셋팅
            dayDto.setDayOfWeek(getDayOfKoreanWeek(cal1.get(Calendar.DAY_OF_WEEK))); // "월"~"일" 셋팅
            roomDayList.add(dayDto);

            cal1.add(Calendar.DATE, 1); // 일 계산 하루씩 추가
        }

        result.setCategory(stRoom.getCategory());
        /*   result.setTodoList(todoList);*/
        result.setStudyRoomTodoAverage(average); // 평균값
        result.setRoomDayList(roomDayList);
        return result;
    }

    public String getDayOfKoreanWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "일";
            case Calendar.MONDAY:
                return "월";
            case Calendar.TUESDAY:
                return "화";
            case Calendar.WEDNESDAY:
                return "수";
            case Calendar.THURSDAY:
                return "목";
            case Calendar.FRIDAY:
                return "금";
            case Calendar.SATURDAY:
                return "토";
            default:
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
            progress.setImageUrl(user.getImageUrl());
            progressList.add(progress);
        }

        return progressList;
    }

    public MemberTodoProgressResponseDto getProgressInfo(Long roomId, String currentEmail) throws Exception {
        MemberTodoProgressResponseDto todoProgress = new MemberTodoProgressResponseDto();
        User user = userService.getUserByEmail(currentEmail);
        todoProgress.setUserNick(user.getNickname());

        List<StRoomTodoInfoProjection> studyRoomTodoAverage = null;
        double average = 0;
        try {
            studyRoomTodoAverage = studyMemberToDoRepository.getStudyRoomTodoAverage(roomId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (studyRoomTodoAverage != null && !studyRoomTodoAverage.isEmpty()) {
            StRoomTodoInfoProjection projection = studyRoomTodoAverage.get(0);
            // DB에 데이터가 존재할 때에만 해당 로직 수행
            int totalCount = projection.getTotalCount();
            int completedCount = projection.getCompletedCount();
            if (totalCount > 0) {
                average = ((double) completedCount / totalCount) * 100;
            }
        }

        todoProgress.setAvg(average);
        return todoProgress;
    }

    public List<MemberTodoResponseDto> getTodoByDateAndMemberIdAndRoomId(LocalDate date, Long roomId,String email) throws Exception{
        List<MemberTodoResponseDto> dto = new ArrayList<>();
        User user= userService.getUserByEmail(email);
        List<StRoomTodoFindProjection> projection = studyMemberToDoRepository.findTodoByDateAndMemberIdAndRoomId(date,user.getId(),roomId);
        //projection null check
        if(projection == null || projection.isEmpty())
        {
            System.out.println("**********");
        }
        for (StRoomTodoFindProjection projections : projection) {
            MemberTodoResponseDto responseDto = new MemberTodoResponseDto();
            responseDto.setStatus(projections.getStatus());
            responseDto.setTodoContent(projections.getTodoContent());
            responseDto.setIdx(projections.getIdx());

            //projection select result check
            System.out.println("=========[TO DO LIST"+responseDto.getIdx()+"]===========");
            System.out.println(responseDto.getStatus());
            System.out.println(responseDto.getTodoContent());

            dto.add(responseDto);
        }

        return dto;
    }

}