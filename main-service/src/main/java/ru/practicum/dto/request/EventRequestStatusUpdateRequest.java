package ru.practicum.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private StatusEnum status;

}
