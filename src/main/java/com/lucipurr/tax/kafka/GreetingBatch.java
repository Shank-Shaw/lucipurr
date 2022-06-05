package com.lucipurr.tax.kafka;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GreetingBatch {
    int id;
    int batchId;
    int firstId;
    int lastId;
    List<Greeting> greeting;
}
