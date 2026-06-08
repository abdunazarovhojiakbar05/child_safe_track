package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.hojiakbar.child_tracking.entity.Device;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Device findDeviceById(UUID id);

    Device findDeviceByChild_Id(UUID id);


}
