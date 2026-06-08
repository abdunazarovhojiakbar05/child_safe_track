package uz.hojiakbar.child_tracking.dto.parentDto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.dto.response.GeofenceResponseDto;
import uz.hojiakbar.child_tracking.dto.response.LocationResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.enums.Status;

import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChildDashboardResponseDto {


    Child child;

    UUID id;

    String full_name;

    String avatar_url;

    Date date_of_birth;

    Status verified;

    LocationResponseDto location;

    GeofenceResponseDto geofence;

    DeviceResponseDto device;


}
