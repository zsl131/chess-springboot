package com.zslin.bus.basic.dao;

import com.zslin.bus.basic.model.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IContactsDao extends JpaRepository<Contacts,Integer>,JpaSpecificationExecutor<Contacts> {
}
