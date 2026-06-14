package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Device;
import uz.hojiakbar.child_tracking.entity.Users;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Device findDeviceById(UUID id);

    Device findDeviceByChild_Id(UUID id);

    Optional<Device> findByChild(Child child);

    @Query("SELECT d FROM device d WHERE d.user = :user ORDER BY d.last_seen_at DESC LIMIT 1")
    Optional<Device> findLatestByUser(@Param("user") Users user);

    @Query("SELECT d FROM device d WHERE d.child = :child ORDER BY d.last_seen_at DESC LIMIT 1")
    Optional<Device> findLatestByChild(@Param("child") Child child);
}
