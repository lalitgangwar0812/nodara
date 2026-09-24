package com.nodara.platform.device.repository;

import com.nodara.platform.device.entity.DeviceTelemetry;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTelemetryRepository extends JpaRepository<DeviceTelemetry, Long> {
    Optional<DeviceTelemetry> findFirstByDevice_IdOrderByRecordedAtDesc(Long deviceId);
}
