package uz.hojiakbar.child_tracking.dto.parentDto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.dto.response.GeofenceResponseDto;
import uz.hojiakbar.child_tracking.dto.response.LocationResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChildDashboardResponseDto {


    Child child;

    LocationResponseDto location;

    GeofenceResponseDto geofence;

    DeviceResponseDto device;



}
