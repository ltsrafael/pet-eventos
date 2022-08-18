package com.rafael.peteventos.integration.repository.impl;

import com.rafael.peteventos.domain.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT a FROM Attendance a WHERE a.event.idEvent = ?1")
    List<Attendance> findAttendanceByEvent(Long idEvent);

    @Modifying
    @Transactional
    @Query("DELETE FROM Attendance WHERE event.idEvent = ?1")
    void deleteAttendanceByEvent(Long idEvent);
}
