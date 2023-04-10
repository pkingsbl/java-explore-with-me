package ru.practicum.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class GetStatsRequest {
    private String start;
    private String end;
    private List<String> uris;
    private Boolean unique;

    public static GetStatsRequest of(String start, String end, List<String> uris, Boolean unique) {
        GetStatsRequest request = new GetStatsRequest();
        request.setStart(start);
        request.setEnd(end);
        request.setUnique(unique);
        if (uris != null) {
            request.setUris(uris);
        }
        return request;
    }
}
