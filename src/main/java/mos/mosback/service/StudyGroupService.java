package mos.mosback.service;


import java.util.List;

public interface StudyGroupService {

    void createStudyGroup(mos.mosback.entity.StudyGroup studyGroup);

    List<mos.mosback.entity.StudyGroup> getAllStudyGroups();
}
