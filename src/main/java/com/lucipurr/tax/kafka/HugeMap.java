package com.lucipurr.tax.kafka;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HugeMap {
    Integer id;
    Integer batchSize;
    Integer sliceId;
    Map<Integer, String> map;
}
