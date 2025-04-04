package pl.pachowicz.empik.common;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor(staticName = "of")
public class CustomerId implements Serializable {
    private final Long value;
}
