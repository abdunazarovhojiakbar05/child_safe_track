package uz.hojiakbar.child_tracking.dto.parentDto;

import lombok.*;
import uz.hojiakbar.child_tracking.dto.response.AlertResponseDto;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParentData {

    private int total_children;
    private int active_children;
    private int pending_children;
    private int unread_alerts;

    private List<ChildData> children;

    private DeviceResponseDto parent_device;

 }
