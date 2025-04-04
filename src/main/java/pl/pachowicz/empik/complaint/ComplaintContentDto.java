package pl.pachowicz.empik.complaint;

import java.util.Objects;

record ComplaintContentDto(String content) {
    ComplaintContentDto {
        if (Objects.isNull(content)) {
            throw new IllegalArgumentException ("content cannot be null");
        }
    }
}
