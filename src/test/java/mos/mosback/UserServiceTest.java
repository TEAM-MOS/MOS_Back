package mos.mosback;

import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.repository.StRoomRepository;
import mos.mosback.login.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private StRoomRepository stRoomRepository;

    @Test
    public void testGetStudyGroupsForUserByMemberId() {
        // Arrange: 테스트용 memberId와 연관된 스터디 그룹 데이터를 미리 데이터베이스에 저장합니다.
        Long memberId = 1L; // 테스트용 memberId
        // TODO: 스터디 그룹 데이터베이스에 저장 (예: StRoomEntity 객체 생성 및 stRoomRepository.save())

        // Act: getStudyGroupsForUserByMemberId 메서드를 호출하여 실제 반환값을 가져옵니다.
        List<StRoomEntity> studyGroups = userService.getStudyGroupsForUserByMemberId(memberId);
        List<StRoomEntity> expectedStudyGroups = new ArrayList<>();

// 예상되는 스터디 그룹을 추가합니다.
        StRoomEntity studyGroup1 = new StRoomEntity();
        studyGroup1.setTitle("Study Group 1");
// TODO: studyGroup1의 다른 필드를 설정

        StRoomEntity studyGroup2 = new StRoomEntity();
        studyGroup2.setTitle("Study Group 2");
// TODO: studyGroup2의 다른 필드를 설정

        expectedStudyGroups.add(studyGroup1);
        expectedStudyGroups.add(studyGroup2);
        // TODO: expectedStudyGroups를 설정 (실제로 기대하는 스터디 그룹 데이터를 데이터베이스에 저장하고 가져와서 설정)

        // Assert: 반환된 스터디 그룹 목록이 예상한 값과 일치하는지를 검증합니다.
        // 예상한 스터디 그룹 개수와 일치하는지 검증
        assertEquals(expectedStudyGroups.size(), studyGroups.size());

        // 각 스터디 그룹에 대한 추가적인 검증 (예: 제목, 설명 등)
        for (int i = 0; i < expectedStudyGroups.size(); i++) {
            assertEquals(expectedStudyGroups.get(i).getTitle(), studyGroups.get(i).getTitle());
            // TODO: 다른 필드들을 비교하여 일치하는지 확인
        }
    }
}
