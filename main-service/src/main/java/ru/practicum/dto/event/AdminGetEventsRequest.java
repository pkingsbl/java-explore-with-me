package ru.practicum.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AdminGetEventsRequest {
    private List<Long> users;
    private List<String> states;
    private List<Long> categories;
    private String rangeStart;
    private String rangeEnd;
    private Integer from;
    private Integer size;

    public static AdminGetEventsRequest of(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size
    ) {
        AdminGetEventsRequest request = new AdminGetEventsRequest();
        request.setUsers(users);
        request.setStates(states);
        request.setCategories(categories);
        request.setRangeStart(rangeStart);
        request.setRangeEnd(rangeEnd);
        request.setFrom(from);
        request.setSize(size);

        return request;
    }
}
