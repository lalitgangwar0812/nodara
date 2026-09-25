package com.nodara.platform.device.repository;

import com.nodara.platform.device.entity.Device;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByHostname(String hostname);
    Optional<Device> findByDeviceUuid(String deviceUuid);
}
