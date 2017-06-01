package com.ziroom.ferrari;

import lombok.*;

/**
 * @author dongh38@ziroom.com
 * @version 1.0.0
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataChangeMessage {

    private Long id;

    private Long msgId;

    private String msgTopic;

    private String msgTag;

    private String msgKey;

    private Integer msgStatus;

    private Integer changeType;

    private String msgData;

    private Long changeTime;

    private Long produceTime;

    private Long consumeTime;

}
