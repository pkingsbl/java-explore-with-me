package ru.practicum.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PublicGetEventsRequest {

    private String text;
    private List<Long> categories;
    private Boolean paid;
    private String rangeStart;
    private String rangeEnd;
    private Boolean onlyAvailable;
    private String sort;
    private Integer from;
    private Integer size;

    public static PublicGetEventsRequest of(
            String text,
            List<Long> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size
    ) {
        PublicGetEventsRequest request = new PublicGetEventsRequest();
        request.setText(text);
        request.setCategories(categories);
        request.setPaid(paid);
        request.setRangeStart(rangeStart);
        request.setRangeEnd(rangeEnd);
        request.setOnlyAvailable(onlyAvailable);
        request.setSort(sort);
        request.setFrom(from);
        request.setSize(size);

        return request;
    }
}
