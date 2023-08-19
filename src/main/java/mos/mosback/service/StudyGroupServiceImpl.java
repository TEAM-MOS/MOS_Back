package mos.mosback.service;


import mos.mosback.entity.StudyGroup;
import mos.mosback.repository.StudyGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudyGroupServiceImpl implements StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;

    @Autowired
    public StudyGroupServiceImpl(StudyGroupRepository studyGroupRepository) {
        this.studyGroupRepository = studyGroupRepository;
    }

    @Override
    public void createStudyGroup(StudyGroup studyGroup) {
        studyGroupRepository.save(studyGroup);
    }

    @Override
    public void createStudyGroup(mos.mosback.service.StudyGroup studyGroup) {

    }


    @Override
    public List<StudyGroup> getAllStudyGroups() {
        return studyGroupRepository.findAll();
    }
}