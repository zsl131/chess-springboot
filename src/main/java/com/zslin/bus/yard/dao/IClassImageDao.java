package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.ClassImage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IClassImageDao extends BaseRepository<ClassImage, Integer>, JpaSpecificationExecutor<ClassImage> {

    @Query("FROM ClassImage c WHERE c.teaPhone=?1 AND c.createYear=?2 AND c.courseId=?3")
    List<ClassImage> findByTea(String phone, String year, Integer courseId, Sort sort);
}
