package uz.hojiakbar.child_tracking.dto.parentDto;

import lombok.*;
import uz.hojiakbar.child_tracking.dto.response.LocationResponseDto;
import uz.hojiakbar.child_tracking.enums.Status;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildData {

    private UUID id;
    private String full_name;
    private String email;
    private String phone;
    private Status status;
    private boolean is_online;
    private DeviceResponseDto child_device;
}