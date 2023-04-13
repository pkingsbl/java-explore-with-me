package ru.practicum.dto.event;

import java.util.List;

public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private StatusEnum status;

}
