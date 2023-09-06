package mos.mosback.service;


import mos.mosback.data.entity.StudyGroup;

import java.util.List;

public interface StudyGroupService {

    void createStudyGroup(StudyGroup studyGroup);

    List<StudyGroup> getAllStudyGroups();
}
